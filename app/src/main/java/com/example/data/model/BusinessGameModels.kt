package com.example.data.model

data class VirtualBusiness(
    val id: String,
    val name: String,
    val typeName: String,
    val description: String,
    val iconEmoji: String,
    val cashBalance: Double,
    val bankBalance: Double,
    val totalRevenue: Double,
    val totalExpense: Double,
    val levelRequired: Int = 1
)

data class GameTransactionScenario(
    val id: String,
    val businessId: String,
    val title: String,
    val transactionText: String,
    val date: String,
    val contextInfo: String,
    val expectedDebitAccount: String,
    val expectedCreditAccount: String,
    val expectedAmount: Double,
    val expectedNarration: String,
    val ruleApplied: String,
    val xpReward: Int = 50,
    val coinReward: Int = 20,
    val explanation: String
)

data class BusinessGameState(
    val selectedBusiness: VirtualBusiness,
    val solvedCount: Int = 0,
    val currentStreak: Int = 0,
    val sessionXpEarned: Int = 0,
    val lastFeedback: String? = null,
    val isLastAnswerCorrect: Boolean? = null
)
