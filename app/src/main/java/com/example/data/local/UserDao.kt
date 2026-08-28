package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudentAcademicProfile
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET xp = xp + :gainedXp, coins = coins + :gainedCoins WHERE id = 'primary_user'")
    suspend fun addXpAndCoins(gainedXp: Int, gainedCoins: Int)

    @Query("UPDATE user_profile SET streakDays = :streak, lastActiveDate = :timestamp WHERE id = 'primary_user'")
    suspend fun updateStreak(streak: Int, timestamp: Long)

    // BoardMentor AI Academic Profile
    @Query("SELECT * FROM student_academic_profile WHERE id = 'primary_student' LIMIT 1")
    fun getStudentProfile(): Flow<StudentAcademicProfile?>

    @Query("SELECT * FROM student_academic_profile WHERE id = 'primary_student' LIMIT 1")
    suspend fun getStudentProfileSync(): StudentAcademicProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStudentProfile(profile: StudentAcademicProfile)

    @Query("UPDATE student_academic_profile SET totalXp = totalXp + :xpReward WHERE id = 'primary_student'")
    suspend fun addStudentXp(xpReward: Int)

    @Query("UPDATE student_academic_profile SET targetPercentage = :target WHERE id = 'primary_student'")
    suspend fun updateTargetPercentage(target: Int)

    @Query("UPDATE student_academic_profile SET schoolVerificationStatus = :status WHERE id = 'primary_student'")
    suspend fun updateSchoolVerification(status: com.example.data.model.SchoolVerificationStatus)
}
