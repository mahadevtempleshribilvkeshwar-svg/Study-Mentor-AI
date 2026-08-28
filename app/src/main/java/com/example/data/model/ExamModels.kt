package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ExamCategory(val title: String, val totalMarks: Int, val durationMinutes: Int, val description: String) {
    DAILY_REVISION("Daily Revision Test", 20, 20, "10 targeted questions for daily retention and weak spot detection"),
    UNIT_TEST("Unit Test (Partnership / Shares)", 25, 30, "Strict 25-mark curriculum unit evaluation with blank workspaces"),
    HALF_YEARLY("Half-Yearly Examination", 50, 90, "Comprehensive mid-term evaluation covering major topics"),
    FULL_BOARD_MOCK("Mission 100 Board Mock Exam", 80, 180, "Full authentic Board simulation with Question Palette & Rubric Scoring")
}

enum class QuestionType {
    MCQ,
    MCQ_1M,
    NUMERICAL_INPUT,
    JOURNAL_WORKSPACE,
    LEDGER_WORKSPACE,
    FINANCIAL_STATEMENT,
    THEORY_EXPLANATION,
    CASE_BASED
}

data class ExamQuestion(
    val id: String,
    val chapterId: String,
    val chapterName: String,
    val questionNumber: Int,
    val marks: Int,
    val type: QuestionType,
    val questionText: String,
    val instructions: String = "",
    val mcqOptions: List<String> = emptyList(),
    val correctMcqIndex: Int = -1,
    val expectedNumericalAnswer: Double? = null,
    val modelAnswerJournal: List<JournalEntryRow> = emptyList(),
    val modelAnswerExplanation: String = "",
    val markingRubricSteps: List<String> = emptyList(),
    val yearTag: String = "",
    val boardTag: String = "CBSE"
)

data class StudentQuestionResponse(
    val questionId: String,
    val selectedMcqOption: Int = -1,
    val writtenAnswerText: String = "",
    val numericalInput: String = "",
    val journalRows: List<JournalEntryRow> = emptyList(),
    val ledgerRowsDr: List<LedgerEntryRow> = emptyList(),
    val ledgerRowsCr: List<LedgerEntryRow> = emptyList(),
    val isMarkedForReview: Boolean = false,
    val isAnswered: Boolean = false
)

@Entity(tableName = "test_attempts")
data class TestAttemptRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examCategory: ExamCategory,
    val totalMarks: Int,
    val scoredMarks: Double,
    val percentage: Double,
    val timeSpentSeconds: Long,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val correctCount: Int,
    val totalQuestions: Int,
    val weakTopicsCsv: String,
    val strongTopicsCsv: String,
    val aiFeedbackSummary: String
)

data class ExamAnalysisReport(
    val examTitle: String,
    val scoredMarks: Double,
    val totalMarks: Int,
    val percentage: Double,
    val accuracyPercentage: Int,
    val timeSpentFormatted: String,
    val strongChapters: List<String>,
    val weakChapters: List<String>,
    val whatWentWell: List<String>,
    val whatWasWrong: List<String>,
    val whyItWasWrong: List<String>,
    val rulesApplicable: List<String>,
    val howToFix: List<String>,
    val recommendedNextSteps: List<String>,
    val mission100ReadinessDelta: String
)

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD
}

val QuestionType.isMcq: Boolean get() = this == QuestionType.MCQ || this == QuestionType.MCQ_1M

val ExamQuestion.chapter: String get() = chapterName
val ExamQuestion.options: List<String> get() = mcqOptions
val ExamQuestion.correctAnswer: String get() = if (correctMcqIndex in mcqOptions.indices) mcqOptions[correctMcqIndex] else ""
val ExamQuestion.explanation: String get() = modelAnswerExplanation
val ExamQuestion.difficulty: DifficultyLevel get() = when {
    marks <= 1 -> DifficultyLevel.EASY
    marks <= 4 -> DifficultyLevel.MEDIUM
    else -> DifficultyLevel.HARD
}
