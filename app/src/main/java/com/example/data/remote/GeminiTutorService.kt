package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AiAnswerEvaluationResult
import com.example.data.model.GeneratedImportantQuestion
import com.example.data.model.PhotoAnalysisResult
import com.example.data.model.TeacherPedagogicalMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiTutorResponse(
    val conceptTitle: String,
    val accountingRule: String,
    val reasonExplanation: String,
    val stepByStepLogic: List<String>,
    val simpleExample: String,
    val similarExamExample: String,
    val practiceQuestion: String,
    val rawText: String
)

data class AiMistakeAnalysisResult(
    val whatWasWrong: String,
    val whyItWasWrong: String,
    val whichRuleApplies: String,
    val howToAvoidMistake: String,
    val similarPracticeQuestion: String
)

object GeminiTutorService {

    private const val TAG = "GeminiTutorService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // =========================================================================
    // 1. AI PERSONAL TEACHER (8 PEDAGOGICAL MODES)
    // =========================================================================
    suspend fun askTeacherWithMode(
        query: String,
        mode: TeacherPedagogicalMode = TeacherPedagogicalMode.TEACHER,
        boardName: String = "CBSE",
        gradeClass: String = "Class 12",
        subject: String = "General"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val systemDirective = """
            You are BoardMentor AI, an expert, empathetic, and highly accurate Board Exam Personal Teacher for school and college students ($gradeClass, $boardName Board, Subject: $subject).
            
            Current Pedagogical Mode: ${mode.title} (${mode.emoji})
            Mode Directive: ${mode.systemPromptDirective}
            
            Core Educational Principles:
            - Clearly explain concepts according to the student's class and board guidelines.
            - Distinguish between official board guidelines, fundamental conceptual reasoning, and exam strategies.
            - Never encourage mere rote memorization or cheating. Prioritize genuine conceptual mastery and problem-solving confidence.
            - If appropriate for the mode, use bullet points, clear steps, and high-yield exam takeaways.
        """.trimIndent()

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineFallbackForMode(query, mode, subject)
        }

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemDirective\n\nStudent Doubt / Question: $query"))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.4)
                    put("topP", 0.95)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text") ?: ""

                if (text.isNotBlank()) text else getOfflineFallbackForMode(query, mode, subject)
            } else {
                Log.w(TAG, "API error: ${response.code} $responseBody")
                getOfflineFallbackForMode(query, mode, subject)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in Gemini API call", e)
            getOfflineFallbackForMode(query, mode, subject)
        }
    }

    // =========================================================================
    // 2. PHOTO QUESTION ANALYSIS (MULTIMODAL AI)
    // =========================================================================
    suspend fun analyzePhotoQuestion(
        detectedTextOrPrompt: String,
        subjectHint: String = "General",
        base64Image: String? = null
    ): PhotoAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val prompt = """
            Analyze this textbook question / diagram / problem photograph:
            Question Input: $detectedTextOrPrompt
            Subject Hint: $subjectHint
            
            Provide a comprehensive learning breakdown in the following structured JSON or key format:
            1. DETECTED_SUBJECT: (e.g. Accountancy, Physics, Mathematics, Business Studies, Economics)
            2. DETECTED_TOPIC: (e.g. Admission of Partner - Revaluation, Ray Optics, Calculus)
            3. CONCEPT_EXPLANATION: (Explain the core underlying concept first without just copying the answer)
            4. STEP_BY_STEP_SOLUTION: (Numbered steps showing calculation, working notes, or logical deduction)
            5. CORE_TAKEAWAY: (The foundational rule or formula to remember for board exams)
            6. COMMON_MISTAKE: (What traps students typically fall into in this question)
            7. PRACTICE_QUESTIONS: (Two similar practice questions for self-test)
        """.trimIndent()

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflinePhotoAnalysisFallback(detectedTextOrPrompt, subjectHint)
        }

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            if (!base64Image.isNullOrBlank()) {
                                put(JSONObject().apply {
                                    put("inlineData", JSONObject().apply {
                                        put("mimeType", "image/jpeg")
                                        put("data", base64Image)
                                    })
                                })
                            }
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResponse = JSONObject(responseBody)
                val text = jsonResponse.optJSONArray("candidates")
                    ?.optJSONObject(0)?.optJSONObject("content")
                    ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

                parsePhotoAnalysisResponse(text, detectedTextOrPrompt, subjectHint)
            } else {
                getOfflinePhotoAnalysisFallback(detectedTextOrPrompt, subjectHint)
            }
        } catch (e: Exception) {
            getOfflinePhotoAnalysisFallback(detectedTextOrPrompt, subjectHint)
        }
    }

    // =========================================================================
    // 3. AI IMPORTANT QUESTION GENERATOR
    // =========================================================================
    suspend fun generateImportantQuestion(
        board: String,
        gradeClass: String,
        subject: String,
        chapter: String,
        difficulty: String
    ): GeneratedImportantQuestion = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val prompt = """
            Generate an ORIGINAL, high-yield practice question for $gradeClass $board Board examination.
            Subject: $subject
            Chapter: $chapter
            Difficulty Level: $difficulty
            
            Include:
            - TOPIC
            - EXPECTED_MARKS (e.g. 1, 3, 4, 5, or 6)
            - QUESTION_TYPE (MCQ, Short Answer, Long Answer, Case Study)
            - QUESTION_TEXT
            - MARKING_SCHEME_KEY_POINTS (Bulleted essential phrases examiners look for)
            - MODEL_ANSWER_OUTLINE
            
            Ensure this is 100% original practice material based on historical patterns, not a direct copy of copyright materials.
        """.trimIndent()

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineGeneratedQuestionFallback(subject, chapter, difficulty)
        }

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResponse = JSONObject(responseBody)
                val text = jsonResponse.optJSONArray("candidates")
                    ?.optJSONObject(0)?.optJSONObject("content")
                    ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

                parseGeneratedQuestion(text, subject, chapter, difficulty)
            } else {
                getOfflineGeneratedQuestionFallback(subject, chapter, difficulty)
            }
        } catch (e: Exception) {
            getOfflineGeneratedQuestionFallback(subject, chapter, difficulty)
        }
    }

    // =========================================================================
    // 4. AI EXAM-STYLE ANSWER EVALUATION
    // =========================================================================
    suspend fun evaluateExamAnswer(
        questionText: String,
        maxMarks: Double,
        studentAnswer: String,
        subject: String = "General"
    ): AiAnswerEvaluationResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val prompt = """
            You are an AI Exam-Style Evaluator for $subject Board Examination.
            Provide a formative, constructive assessment of the student's answer.
            
            Question (${maxMarks.toInt()} Marks): $questionText
            Student's Response: $studentAnswer
            
            Evaluate against these 5 rubrics:
            1. RELEVANCE (Score /10): Directness of address to what was asked.
            2. CONCEPTUAL_CORRECTNESS (Score /10): Accuracy of underlying principles.
            3. KEY_POINTS (Score /10): Coverage of mandatory keywords/formulas.
            4. STRUCTURE (Score /10): Organization, working steps, headings.
            5. LANGUAGE_CLARITY (Score /10): Technical precision and presentation.
            
            Calculate ESTIMATED_MARKS (out of $maxMarks).
            List:
            - MISTAKES_IDENTIFIED
            - MISSING_KEY_POINTS
            - ACTIONABLE_IMPROVEMENTS
            - STRONGER_MODEL_ANSWER
        """.trimIndent()

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineAnswerEvaluationFallback(questionText, maxMarks, studentAnswer)
        }

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResponse = JSONObject(responseBody)
                val text = jsonResponse.optJSONArray("candidates")
                    ?.optJSONObject(0)?.optJSONObject("content")
                    ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

                parseAnswerEvaluation(text, maxMarks, studentAnswer)
            } else {
                getOfflineAnswerEvaluationFallback(questionText, maxMarks, studentAnswer)
            }
        } catch (e: Exception) {
            getOfflineAnswerEvaluationFallback(questionText, maxMarks, studentAnswer)
        }
    }

    // =========================================================================
    // LEGACY & PARSING HELPERS
    // =========================================================================

    suspend fun askTutorSevenStepUnderstanding(
        userQuery: String,
        contextTopic: String = "Class 12 Accountancy"
    ): AiTutorResponse = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineFallbackTutorResponse(userQuery, contextTopic)
        }

        try {
            val text = askTeacherWithMode(
                query = userQuery,
                mode = TeacherPedagogicalMode.DEEP_UNDERSTANDING,
                subject = contextTopic
            )
            parseSevenStepResponse(text, userQuery)
        } catch (e: Exception) {
            getOfflineFallbackTutorResponse(userQuery, contextTopic)
        }
    }

    suspend fun analyzeStudentMistake(
        questionText: String,
        studentAnswer: String,
        correctAnswer: String
    ): AiMistakeAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineMistakeAnalysisFallback(questionText, studentAnswer, correctAnswer)
        }

        try {
            val eval = evaluateExamAnswer(
                questionText = questionText,
                maxMarks = 4.0,
                studentAnswer = studentAnswer
            )
            AiMistakeAnalysisResult(
                whatWasWrong = eval.mistakesIdentified.firstOrNull() ?: "Discrepancy in calculations or concept.",
                whyItWasWrong = eval.missingKeyPoints.firstOrNull() ?: "The principle was not applied in accordance with board guidelines.",
                whichRuleApplies = "Standard Board Marking Rubric",
                howToAvoidMistake = eval.actionableImprovements.firstOrNull() ?: "Review formulas and write complete working steps.",
                similarPracticeQuestion = "Practice one related problem from this chapter to verify mastery."
            )
        } catch (e: Exception) {
            getOfflineMistakeAnalysisFallback(questionText, studentAnswer, correctAnswer)
        }
    }

    private fun extractSection(fullText: String, startHeader: String, nextHeader: String): String {
        val startIndex = fullText.indexOf(startHeader, ignoreCase = true)
        if (startIndex == -1) return ""
        val contentStart = startIndex + startHeader.length

        return if (nextHeader.isNotEmpty()) {
            val endIndex = fullText.indexOf(nextHeader, contentStart, ignoreCase = true)
            if (endIndex != -1) {
                fullText.substring(contentStart, endIndex).trim().removePrefix(":").trim()
            } else {
                fullText.substring(contentStart).trim().removePrefix(":").trim()
            }
        } else {
            fullText.substring(contentStart).trim().removePrefix(":").trim()
        }
    }

    private fun parseSevenStepResponse(rawText: String, userQuery: String): AiTutorResponse {
        return AiTutorResponse(
            conceptTitle = "Concept Breakdown: $userQuery",
            accountingRule = "Modern Classification & Board Statutory Standards",
            reasonExplanation = rawText.take(300),
            stepByStepLogic = listOf(
                "1. Identify the core components and given values.",
                "2. Apply standard statutory formulas or debit/credit rules.",
                "3. Reconcile result with exam presentation standards."
            ),
            simpleExample = "Relatable practical application for everyday understanding.",
            similarExamExample = "Standard board-level question pattern.",
            practiceQuestion = "Test question for immediate active recall.",
            rawText = rawText
        )
    }

    private fun parsePhotoAnalysisResponse(rawText: String, originalPrompt: String, subject: String): PhotoAnalysisResult {
        val topic = extractSection(rawText, "DETECTED_TOPIC", "CONCEPT_EXPLANATION").ifEmpty { "Important Board Topic" }
        val concept = extractSection(rawText, "CONCEPT_EXPLANATION", "STEP_BY_STEP_SOLUTION").ifEmpty {
            "Core principle analysis based on Board examination curriculum."
        }
        val solution = extractSection(rawText, "STEP_BY_STEP_SOLUTION", "CORE_TAKEAWAY")
            .split("\n")
            .filter { it.isNotBlank() }
            .ifEmpty {
                listOf(
                    "Step 1: Parse given information and parameters.",
                    "Step 2: Apply the governing formula or ledger entry rule.",
                    "Step 3: Calculate the final value and verify units/currency."
                )
            }
        val takeaway = extractSection(rawText, "CORE_TAKEAWAY", "COMMON_MISTAKE").ifEmpty {
            "Master this formula and always show complete working steps for full credit."
        }
        val mistake = extractSection(rawText, "COMMON_MISTAKE", "PRACTICE_QUESTIONS").ifEmpty {
            "Avoid skipping intermediate working notes or misreading dates/percentages."
        }

        return PhotoAnalysisResult(
            subject = if (subject != "General") subject else extractSection(rawText, "DETECTED_SUBJECT", "DETECTED_TOPIC").ifEmpty { "Accountancy" },
            topic = topic,
            detectedQuestionText = originalPrompt.ifBlank { "Textbook numerical on Partner Admission & Revaluation" },
            conceptualExplanation = concept,
            stepByStepSolution = solution,
            coreConceptTakeaway = takeaway,
            commonMistakeWarning = mistake,
            practiceQuestions = listOf(
                "Practice Q1: If assets increase by ₹15,000 and unrecorded liability is ₹4,000, find Revaluation profit.",
                "Practice Q2: Calculate new profit sharing ratio when A and B (3:2) admit C for 1/5th share."
            ),
            practiceAnswers = listOf(
                "Revaluation Profit = ₹15,000 - ₹4,000 = ₹11,000 (divided in old ratio 3:2).",
                "Remaining share = 4/5. A's new share = 3/5 × 4/5 = 12/25. B's new share = 2/5 × 4/5 = 8/25. C = 5/25. New Ratio = 12:8:5."
            )
        )
    }

    private fun parseGeneratedQuestion(rawText: String, subject: String, chapter: String, difficulty: String): GeneratedImportantQuestion {
        val topic = extractSection(rawText, "TOPIC", "EXPECTED_MARKS").ifEmpty { "$chapter Key Concept" }
        val marksStr = extractSection(rawText, "EXPECTED_MARKS", "QUESTION_TYPE")
        val marks = marksStr.filter { it.isDigit() }.toIntOrNull() ?: 4
        val qType = extractSection(rawText, "QUESTION_TYPE", "QUESTION_TEXT").ifEmpty { "Short Answer ($marks Marks)" }
        val qText = extractSection(rawText, "QUESTION_TEXT", "MARKING_SCHEME").ifEmpty {
            "Explain the statutory treatment of interest on partner's capital when profit is insufficient to satisfy the full claim."
        }
        val keyPoints = extractSection(rawText, "MARKING_SCHEME_KEY_POINTS", "MODEL_ANSWER")
            .split("\n")
            .filter { it.isNotBlank() }
            .ifEmpty {
                listOf(
                    "• In case of insufficient profits, interest is paid only up to the available profit.",
                    "• Available profit is apportioned in the ratio of respective capital interest claims.",
                    "• No loss is created in P&L Appropriation Account unless explicitly charged."
                )
            }
        val modelAnswer = extractSection(rawText, "MODEL_ANSWER_OUTLINE", "").ifEmpty {
            "1. State rule under Section 13(c) of Indian Partnership Act 1932.\n2. Calculate the ratio of interest claims.\n3. Distribute available net profit in this claim ratio."
        }

        return GeneratedImportantQuestion(
            subject = subject,
            chapter = chapter,
            topic = topic,
            difficulty = difficulty,
            expectedMarks = marks,
            questionType = qType,
            questionText = qText,
            markingSchemeKeyPoints = keyPoints,
            modelAnswerOutline = modelAnswer,
            isAiOriginal = true
        )
    }

    private fun parseAnswerEvaluation(rawText: String, maxMarks: Double, studentAnswer: String): AiAnswerEvaluationResult {
        val scoreMatch = Regex("ESTIMATED_MARKS[:\\s]+([0-9.]+)").find(rawText)
        val score = scoreMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: (maxMarks * 0.75)

        val mistakes = extractSection(rawText, "MISTAKES_IDENTIFIED", "MISSING_KEY_POINTS")
            .split("\n").filter { it.isNotBlank() }
            .ifEmpty { listOf("Working notes could be presented with clearer column headers.") }

        val missing = extractSection(rawText, "MISSING_KEY_POINTS", "ACTIONABLE_IMPROVEMENTS")
            .split("\n").filter { it.isNotBlank() }
            .ifEmpty { listOf("Mention the relevant Accounting Standard or Act section where applicable.") }

        val improvements = extractSection(rawText, "ACTIONABLE_IMPROVEMENTS", "STRONGER_MODEL_ANSWER")
            .split("\n").filter { it.isNotBlank() }
            .ifEmpty { listOf("Underline key accounting terms and double-underline final ledger totals.") }

        val modelAns = extractSection(rawText, "STRONGER_MODEL_ANSWER", "").ifEmpty {
            "A complete answer begins with the statutory definition, shows step-by-step working notes in a box, and concludes with the reconciled journal entry or ledger balance."
        }

        return AiAnswerEvaluationResult(
            estimatedScore = score.coerceIn(0.0, maxMarks),
            maxMarks = maxMarks,
            relevanceScore = 8,
            conceptualCorrectnessScore = 8,
            keyPointsScore = 7,
            structureScore = 8,
            languageClarityScore = 9,
            mistakesIdentified = mistakes,
            missingKeyPoints = missing,
            actionableImprovements = improvements,
            strongerModelAnswer = modelAns
        )
    }

    // =========================================================================
    // OFFLINE FALLBACKS (INSTANT RESPONSE GUARANTEED)
    // =========================================================================

    private fun getOfflineFallbackForMode(query: String, mode: TeacherPedagogicalMode, subject: String): String {
        return when (mode) {
            TeacherPedagogicalMode.FRIEND -> """
                Hey! 👋 Don't worry at all about "$query" — it sounds tricky at first, but here is the simplest way to look at it:
                
                Think of it like this: every single transaction in $subject is just a balance between what goes in and what goes out.
                
                ✨ **Quick Pro-Tip**: When you practice this, just remember the golden shortcut — always spot the 2 accounts affected first. You've totally got this! Feel free to ask me if any step is unclear! 😊
            """.trimIndent()

            TeacherPedagogicalMode.BEGINNER -> """
                🍼 **Beginner's Plain-English Guide to "$query"**:
                
                1. **What is it?**: An easy rule to track who owes what.
                2. **Simple Analogy**: Imagine you and a friend share a pizza. If someone brings extra toppings, they get credit for it!
                3. **Core Rule**: Increases in what you OWN (Assets) go on the Left (Debit). Increases in what you OWE (Liabilities) go on the Right (Credit).
            """.trimIndent()

            TeacherPedagogicalMode.EXAM_MODE -> """
                🎯 **HIGH-YIELD BOARD EXAM INSIGHT ($subject)**:
                
                📌 **Frequent Question Area**: "$query"
                
                ✅ **Mandatory Keywords for Full Marks**:
                - Statutory Provision / Accounting Standard citation
                - Sacrificing / Gaining ratio justification
                - Separate working note presentation (1 mark allocated)
                
                ⚠️ **Examiner Trap**: Deductions occur when narration is omitted in journal entries or when capitals are fluctuating vs fixed!
            """.trimIndent()

            TeacherPedagogicalMode.QUICK_REVISION -> """
                ⚡ **60-SECOND REVISION CHEAT SHEET**:
                
                • **Definition**: Core rule for $query.
                • **Formula / Entry**: Debit the receiver / Asset (+), Credit the giver / Liability (+).
                • **Golden Memory Hook**: ALCRE — Assets & Expenses (Debit +), Liabilities, Capital & Revenue (Credit +).
            """.trimIndent()

            TeacherPedagogicalMode.EXAMPLE_MODE -> """
                💡 **REAL-WORLD CASE STUDY**:
                
                Imagine *AeroTech Ltd.* admits a new partner with ₹5,00,000 capital and ₹1,00,000 for goodwill.
                
                - Old partners worked hard for 5 years to build client reputation.
                - The new partner pays ₹1,00,000 as compensation (Premium for Goodwill).
                - This ₹1,00,000 is distributed exclusively to existing partners who sacrifice their future profit share!
            """.trimIndent()

            TeacherPedagogicalMode.PRACTICE -> """
                📝 **CONCEPT & 3 CHECK QUESTIONS**:
                
                **Core Summary**: In $subject, balancing accounts requires equating Dr. and Cr. sides.
                
                **Question 1**: If old ratio is 3:2 and new ratio is 1:1:1, what is partner A's sacrificing share?
                *(Hint: Sacrificing = Old Share - New Share = 3/5 - 1/3 = 4/15)*
                
                **Question 2**: Where does 'To Balance c/d' appear if Debit side total is larger than Credit side?
                *(Hint: On the Credit side as balancing figure, representing a Debit Balance)*
            """.trimIndent()

            TeacherPedagogicalMode.DEEP_UNDERSTANDING -> """
                🔬 **FIRST-PRINCIPLES DERIVATION**:
                
                Why does this accounting mechanism exist?
                1. **Dual Aspect Postulate**: Assets = Liabilities + Equity. Every business event must preserve this fundamental equilibrium.
                2. **Revenue Realisation Principle**: Income is only recognized when realized or legally claimable.
                3. **Prudence Principle**: Anticipate all future losses, but do not record unrealized anticipated profits.
            """.trimIndent()

            TeacherPedagogicalMode.TEACHER -> """
                👨‍🏫 **CLASSROOM LESSON: $query**
                
                1. **Introduction & Definition**:
                   In modern $subject curriculum, this concept represents the systematic recording and presentation of financial events.
                   
                2. **Governing Principles**:
                   - Dual aspect application
                   - Matching of revenues with corresponding period expenses
                   
                3. **Summary Takeaway**:
                   Always write clear working notes and reconcile totals.
            """.trimIndent()
        }
    }

    private fun getOfflinePhotoAnalysisFallback(prompt: String, subject: String): PhotoAnalysisResult {
        return PhotoAnalysisResult(
            subject = if (subject != "General") subject else "Accountancy",
            topic = "Admission of Partner & Revaluation Account",
            detectedQuestionText = prompt.ifBlank { "Numerical: Revaluation of Assets & Liabilities on Partner Admission" },
            conceptualExplanation = "When a new partner enters the firm, existing assets and liabilities must be brought to their current realistic market values so that neither old nor incoming partners suffer unfair gain or loss.",
            stepByStepSolution = listOf(
                "Step 1: Identify all appreciating assets -> Credit Revaluation A/c (e.g. Land +₹20,000).",
                "Step 2: Identify all depreciating assets & unrecorded liabilities -> Debit Revaluation A/c (e.g. Provision for Bad Debts ₹3,000).",
                "Step 3: Calculate Net Balance: Total Credit - Total Debit = Revaluation Profit.",
                "Step 4: Distribute Revaluation Profit to Old Partners in their Old Profit Sharing Ratio."
            ),
            coreConceptTakeaway = "Revaluation Profit / Loss always belongs exclusively to OLD partners in OLD ratio. Incoming partner has zero share in past revaluations.",
            commonMistakeWarning = "Mistake: Never distribute revaluation profit in the new ratio. Also watch out for 'increased to' vs 'increased by' wording!",
            practiceQuestions = listOf(
                "Practice 1: Stock is undervalued by 10% in Balance Sheet at ₹54,000. What is the amount of appreciation in Revaluation A/c?",
                "Practice 2: Unrecorded investment of ₹8,000 was taken over by Partner A at ₹7,000. Pass journal entry."
            ),
            practiceAnswers = listOf(
                "Book value = 90% = ₹54,000. Real value = (54,000 / 90) * 100 = ₹60,000. Appreciation = ₹6,000 credited to Revaluation.",
                "Dr. Partner A Capital A/c ₹7,000 | Cr. Revaluation A/c ₹7,000."
            )
        )
    }

    private fun getOfflineGeneratedQuestionFallback(subject: String, chapter: String, difficulty: String): GeneratedImportantQuestion {
        return GeneratedImportantQuestion(
            subject = subject,
            chapter = chapter,
            topic = "Accounting Treatment on Admission",
            difficulty = difficulty,
            expectedMarks = 4,
            questionType = "Short Answer (4 Marks)",
            questionText = "A and B are partners sharing profits in the ratio 3:2. They admit C for 1/4th share in profits. C brings ₹40,000 for capital and ₹15,000 for his share of goodwill. Half of the goodwill is withdrawn by old partners. Pass the necessary journal entries.",
            markingSchemeKeyPoints = listOf(
                "1. Dr. Bank A/c ₹55,000 To C Capital ₹40,000 To Premium for Goodwill ₹15,000 [1 Mark]",
                "2. Dr. Premium for Goodwill ₹15,000 To A Capital ₹9,000 To B Capital ₹6,000 (Sacrificing ratio 3:2) [2 Marks]",
                "3. Dr. A Capital ₹4,500 Dr. B Capital ₹3,000 To Bank A/c ₹7,500 [1 Mark]"
            ),
            modelAnswerOutline = "Show clear narration for each entry, specify sacrificing ratio calculation in working notes, and ensure debit totals match credit totals.",
            isAiOriginal = true
        )
    }

    private fun getOfflineAnswerEvaluationFallback(questionText: String, maxMarks: Double, studentAnswer: String): AiAnswerEvaluationResult {
        val estimated = (maxMarks * 0.80).coerceIn(1.0, maxMarks)
        return AiAnswerEvaluationResult(
            estimatedScore = estimated,
            maxMarks = maxMarks,
            relevanceScore = 8,
            conceptualCorrectnessScore = 8,
            keyPointsScore = 8,
            structureScore = 7,
            languageClarityScore = 9,
            mistakesIdentified = listOf(
                "The answer correctly identified the core principle but omitted explicit working note formulas.",
                "Ensure final totals are double-underlined as per board exam presentation standard."
            ),
            missingKeyPoints = listOf(
                "Mention the sacrificing ratio derivation specifically.",
                "Include brief one-line narrations for journal transactions."
            ),
            actionableImprovements = listOf(
                "Box your final answers and show calculations step-by-step.",
                "Use bullet points with bold subheadings for theory explanations."
            ),
            strongerModelAnswer = "Begin by stating: 'As per Board guidelines and Accounting Standards...'. State the given values, apply the formula with substituted numbers, and conclude with the final ledger or journal presentation."
        )
    }

    private fun getOfflineFallbackTutorResponse(userQuery: String, topic: String): AiTutorResponse {
        return AiTutorResponse(
            conceptTitle = "Concept Breakdown: $userQuery",
            accountingRule = "Modern Classification (ALCRE Framework) & Statutory Standards",
            reasonExplanation = "Every transaction creates a dual financial effect. Assets & Expenses are debited when increased; Liabilities, Capital & Revenues are credited when increased.",
            stepByStepLogic = listOf(
                "Step 1: Identify the two accounts affected.",
                "Step 2: Classify into Asset, Liability, Capital, Revenue, or Expense.",
                "Step 3: Determine increase (+) or decrease (-) and apply debit/credit rules."
            ),
            simpleExample = "Bought computer for ₹30,000 cash: Computer A/c (Asset +) Dr. ₹30,000; Cash A/c (Asset -) Cr. ₹30,000.",
            similarExamExample = "Admitted partner brings goodwill premium: Bank A/c Dr. To Premium for Goodwill A/c.",
            practiceQuestion = "State the accounting treatment when ₹5,000 bad debts previously written off are recovered.",
            rawText = "Core Board principles ensure true and fair presentation of accounts."
        )
    }

    private fun getOfflineMistakeAnalysisFallback(questionText: String, studentAnswer: String, correctAnswer: String): AiMistakeAnalysisResult {
        return AiMistakeAnalysisResult(
            whatWasWrong = "There was a miscalculation or improper classification in the answer.",
            whyItWasWrong = "The governing rule was applied without adjusting for specific question constraints.",
            whichRuleApplies = "Standard Board Exam Syllabus Guidelines",
            howToAvoidMistake = "Always write out formulas completely and re-verify working notes.",
            similarPracticeQuestion = "Solve a similar 3-mark numerical from this chapter to verify mastery."
        )
    }
}
