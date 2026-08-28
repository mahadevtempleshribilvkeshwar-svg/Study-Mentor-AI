package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EducationBoard(val code: String, val displayName: String, val patternNotes: String) {
    CBSE("CBSE", "CBSE (Central Board)", "Theory: 80 Marks, Project: 20 Marks. Part A: Partnership & Company, Part B: Financial Analysis"),
    ISC("ISC", "ISC (CISCE Council)", "Theory: 80 Marks, Project: 20 Marks. Section A compulsory, Section B/C optional"),
    MAHARASHTRA("MAHARASHTRA", "Maharashtra HSC Board", "Theory: 80 Marks, Internal: 20 Marks. Bookkeeping & Accountancy"),
    KARNATAKA("KARNATAKA", "Karnataka 2nd PUC", "Theory: 80/100 Marks. Accountancy with partnership, shares & NPO/computerized"),
    TAMIL_NADU("TAMIL_NADU", "Tamil Nadu State Board", "Theory: 90 Marks, Practical/Internal: 10 Marks. Class 12 Accountancy"),
    UP_BOARD("UP_BOARD", "UP Board (Madhyamik)", "100 Marks pattern. Lekhashastra / Accountancy"),
    MP_BOARD("MP_BOARD", "MP Board (Secondary)", "Theory: 80 Marks, Project: 20 Marks. Lekha Shastra"),
    GUJARAT("GUJARAT", "GSEB (Gujarat Board)", "Elements of Accountancy Part 1 & Part 2"),
    BIHAR("BIHAR", "BSEB (Bihar Board)", "100 Marks pattern (50% Objective, 50% Subjective)"),
    WEST_BENGAL("WEST_BENGAL", "WBCHSE (West Bengal)", "Accountancy Higher Secondary pattern"),
    ALL_INDIA("ALL_INDIA", "All-India Standard Commerce", "Universal Standard Commerce 12 Pattern")
}

enum class CommerceSubject(val id: String, val displayName: String, val isAvailable: Boolean = true) {
    ACCOUNTANCY("ACC", "Accountancy", true),
    BUSINESS_STUDIES("BST", "Business Studies", true),
    ECONOMICS("ECO", "Economics & Finance", true),
    ENGLISH("ENG", "English Core", true),
    APPLIED_MATHS("MATH", "Applied Mathematics", true)
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "primary_user",
    val fullName: String = "Commerce Aspirant",
    val displayName: String = "Topper2026",
    val email: String = "student@example.com",
    val board: EducationBoard = EducationBoard.CBSE,
    val studentClass: String = "Class 12",
    val academicSession: String = "2025-2026",
    val selectedSubject: CommerceSubject = CommerceSubject.ACCOUNTANCY,
    val examDateMillis: Long = System.currentTimeMillis() + (65L * 24 * 60 * 60 * 1000), // ~65 days default
    val xp: Int = 450,
    val coins: Int = 120,
    val level: Int = 3,
    val streakDays: Int = 5,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val isOnlineSyncEnabled: Boolean = true,
    val showOnLeaderboard: Boolean = true
)

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val unlockedDate: String = ""
)
