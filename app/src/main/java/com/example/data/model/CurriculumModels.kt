package com.example.data.model

enum class PriorityLevel {
    VERY_IMPORTANT, // 🔥
    IMPORTANT,      // ⭐
    MUST_REVISE     // 📌
}

enum class AccountCategory {
    ASSET,       // Modern: Debit when increases, Credit when decreases
    LIABILITY,   // Modern: Credit when increases, Debit when decreases
    CAPITAL,     // Modern: Credit when increases, Debit when decreases
    REVENUE,     // Modern: Credit when increases, Debit when decreases
    EXPENSE      // Modern: Debit when increases, Debit when decreases
}

data class ImportantPoint(
    val id: String,
    val title: String,
    val description: String,
    val priority: PriorityLevel,
    val ruleOrFormula: String = "",
    val commonMistakeTrap: String = "",
    val boardExamTip: String = ""
)

data class AccountingConcept(
    val id: String,
    val title: String,
    val overview: String,
    val keyRules: List<String>,
    val formulaOrFormat: String = "",
    val practicalExample: String = "",
    val commonPitfall: String = "",
    val goldenRuleExplanation: String = ""
)

data class ChapterModule(
    val id: String,
    val chapterNumber: Int,
    val title: String,
    val unitName: String,
    val subject: CommerceSubject = CommerceSubject.ACCOUNTANCY,
    val weightageMarks: String,
    val summary: String,
    val concepts: List<AccountingConcept>,
    val importantPoints: List<ImportantPoint>,
    val isCompleted: Boolean = false,
    val masteryPercentage: Int = 0
)
