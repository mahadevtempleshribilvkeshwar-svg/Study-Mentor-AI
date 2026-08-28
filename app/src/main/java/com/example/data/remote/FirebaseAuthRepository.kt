package com.example.data.remote

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class FirebaseAuthRepository(
    context: Context? = null
) {
    private val auth: FirebaseAuth? = try {
        if (context != null && com.google.firebase.FirebaseApp.getApps(context).isEmpty()) {
            com.google.firebase.FirebaseApp.initializeApp(context)
        }
        FirebaseAuth.getInstance()
    } catch (e: Throwable) {
        null
    }

    val currentUser: FirebaseUser?
        get() = auth?.currentUser

    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        if (auth == null) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase is not initialized. Please ensure google-services.json is configured."))
        return try {
            val result = awaitTask(firebaseAuth.createUserWithEmailAndPassword(email.trim(), password))
            val user = result.user ?: return Result.failure(Exception("Registration failed: User not created"))

            // Update user profile display name
            if (displayName.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName.trim())
                    .build()
                awaitTask(user.updateProfile(profileUpdates))
                // Send email verification if possible
                try {
                    awaitTask(user.sendEmailVerification())
                } catch (_: Exception) {
                    // non-fatal
                }
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(mapAuthException(e))
        }
    }

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase is not initialized. Please configure google-services.json."))
        return try {
            val result = awaitTask(firebaseAuth.signInWithEmailAndPassword(email.trim(), password))
            val user = result.user ?: return Result.failure(Exception("Sign in failed: User is null"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(mapAuthException(e))
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase is not configured."))
        return try {
            awaitTask(firebaseAuth.sendPasswordResetEmail(email.trim()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(mapAuthException(e))
        }
    }

    suspend fun signInWithGoogleCredentialManager(
        context: Context,
        webClientId: String? = null
    ): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase is not configured."))
        return try {
            val credentialManager = CredentialManager.create(context)
            
            // Build Google ID Option
            val googleIdOptionBuilder = GetGoogleIdOption.Builder()
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)

            val clientIdToUse = webClientId?.takeIf { it.isNotBlank() }
                ?: "default-client-id.apps.googleusercontent.com"

            googleIdOptionBuilder.setServerClientId(clientIdToUse)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionBuilder.build())
                .build()

            val response = credentialManager.getCredential(context = context, request = request)
            val credential = response.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = awaitTask(firebaseAuth.signInWithCredential(authCredential))
                val user = authResult.user ?: return Result.failure(Exception("Google Sign-In returned no user"))
                Result.success(user)
            } else {
                Result.failure(Exception("Unsupported credential type: ${credential.type}"))
            }
        } catch (e: GetCredentialCancellationException) {
            Result.failure(Exception("Google Sign-In was cancelled."))
        } catch (e: GetCredentialException) {
            Result.failure(Exception("Google Sign-In failed: ${e.localizedMessage ?: "Please configure Google Web Client ID or use Email/Password."}"))
        } catch (e: Exception) {
            Result.failure(mapAuthException(e))
        }
    }

    fun signOut() {
        auth?.signOut()
    }

    private suspend fun <T> awaitTask(task: Task<T>): T = suspendCancellableCoroutine { cont ->
        task.addOnSuccessListener { result ->
            cont.resume(result)
        }
        task.addOnFailureListener { exception ->
            cont.resumeWith(Result.failure(exception))
        }
    }

    private fun mapAuthException(e: Exception): Exception {
        val msg = e.localizedMessage ?: e.message ?: "Authentication error"
        return when {
            msg.contains("password", ignoreCase = true) && msg.contains("weak", ignoreCase = true) ->
                Exception("Password is too weak. Please use at least 6 characters.")
            msg.contains("already in use", ignoreCase = true) || msg.contains("email address is already", ignoreCase = true) ->
                Exception("An account with this email already exists. Please log in.")
            msg.contains("badly formatted", ignoreCase = true) || msg.contains("invalid email", ignoreCase = true) ->
                Exception("Please enter a valid email address.")
            msg.contains("no user record", ignoreCase = true) || msg.contains("user-not-found", ignoreCase = true) ->
                Exception("No account found with this email. Please sign up.")
            msg.contains("wrong-password", ignoreCase = true) || msg.contains("invalid-credential", ignoreCase = true) ->
                Exception("Incorrect password or credentials. Please try again.")
            msg.contains("network", ignoreCase = true) ->
                Exception("Network connection error. Check your internet connection.")
            msg.contains("too many requests", ignoreCase = true) ->
                Exception("Too many failed attempts. Please wait a moment and try again.")
            else -> Exception(msg)
        }
    }
}
