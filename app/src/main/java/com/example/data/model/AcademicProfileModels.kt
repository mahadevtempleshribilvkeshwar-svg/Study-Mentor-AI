package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// ==========================================
// 1. GEOGRAPHIC & ADMINISTRATIVE HIERARCHY
// ==========================================

data class CountryInfo(
    val code: String,
    val name: String,
    val flagEmoji: String,
    val defaultCurrency: String = "₹"
)

data class StateInfo(
    val code: String,
    val name: String,
    val countryCode: String = "IN",
    val popularBoards: List<String> = emptyList()
)

data class DistrictInfo(
    val name: String,
    val stateCode: String
)

// Supported Countries (Extensible for international expansion)
val SupportedCountries = listOf(
    CountryInfo("IN", "India", "🇮🇳", "₹"),
    CountryInfo("US", "United States", "🇺🇸", "$"),
    CountryInfo("UK", "United Kingdom", "🇬🇧", "£"),
    CountryInfo("CA", "Canada", "🇨🇦", "C$"),
    CountryInfo("AU", "Australia", "🇦🇺", "A$"),
    CountryInfo("SG", "Singapore", "🇸🇬", "S$"),
    CountryInfo("AE", "United Arab Emirates", "🇦🇪", "AED")
)

// All 28 States & Major UTs of India
val IndianStatesList = listOf(
    StateInfo("AS", "Assam", "IN", listOf("AHSEC", "SEBA", "CBSE", "CISCE")),
    StateInfo("MH", "Maharashtra", "IN", listOf("MAHARASHTRA", "CBSE", "CISCE")),
    StateInfo("DL", "Delhi (NCR)", "IN", listOf("CBSE", "CISCE")),
    StateInfo("KA", "Karnataka", "IN", listOf("KARNATAKA", "CBSE", "CISCE")),
    StateInfo("UP", "Uttar Pradesh", "IN", listOf("UP_BOARD", "CBSE", "CISCE")),
    StateInfo("TN", "Tamil Nadu", "IN", listOf("TAMIL_NADU", "CBSE", "CISCE")),
    StateInfo("WB", "West Bengal", "IN", listOf("WEST_BENGAL", "CBSE", "CISCE")),
    StateInfo("GJ", "Gujarat", "IN", listOf("GUJARAT", "CBSE", "CISCE")),
    StateInfo("KL", "Kerala", "IN", listOf("KERALA", "CBSE", "CISCE")),
    StateInfo("RJ", "Rajasthan", "IN", listOf("RAJASTHAN", "CBSE")),
    StateInfo("BR", "Bihar", "IN", listOf("BIHAR", "CBSE")),
    StateInfo("MP", "Madhya Pradesh", "IN", listOf("MP_BOARD", "CBSE")),
    StateInfo("PB", "Punjab", "IN", listOf("PUNJAB", "CBSE")),
    StateInfo("TS", "Telangana", "IN", listOf("TELANGANA", "CBSE")),
    StateInfo("AP", "Andhra Pradesh", "IN", listOf("ANDHRA", "CBSE")),
    StateInfo("OD", "Odisha", "IN", listOf("ODISHA", "CBSE")),
    StateInfo("JH", "Jharkhand", "IN", listOf("JAC", "CBSE")),
    StateInfo("HR", "Haryana", "IN", listOf("HBSE", "CBSE")),
    StateInfo("UK_ST", "Uttarakhand", "IN", listOf("UBSE", "CBSE")),
    StateInfo("HP", "Himachal Pradesh", "IN", listOf("HPBOSE", "CBSE")),
    StateInfo("JK", "Jammu & Kashmir", "IN", listOf("JKBOSE", "CBSE")),
    StateInfo("GA", "Goa", "IN", listOf("GBSHSE", "CBSE")),
    StateInfo("TR", "Tripura", "IN", listOf("TBSE", "CBSE")),
    StateInfo("ML", "Meghalaya", "IN", listOf("MBOSE", "CBSE")),
    StateInfo("MN", "Manipur", "IN", listOf("BSEM", "COHSEM", "CBSE")),
    StateInfo("NL", "Nagaland", "IN", listOf("NBSE", "CBSE")),
    StateInfo("MZ", "Mizoram", "IN", listOf("MBSE", "CBSE")),
    StateInfo("AR", "Arunachal Pradesh", "IN", listOf("CBSE")),
    StateInfo("SK", "Sikkim", "IN", listOf("CBSE")),
    StateInfo("CH", "Chandigarh", "IN", listOf("CBSE"))
)

val SampleDistrictsMap = mapOf(
    "AS" to listOf("Kamrup Metropolitan (Guwahati)", "Dibrugarh", "Jorhat", "Silchar (Cachar)", "Nagaon", "Sonitpur (Tezpur)", "Barpeta", "Tinsukia"),
    "MH" to listOf("Mumbai City", "Mumbai Suburban", "Pune", "Nagpur", "Thane", "Nashik", "Aurangabad (Chhatrapati Sambhajinagar)", "Solapur"),
    "DL" to listOf("New Delhi", "Central Delhi", "South Delhi", "North Delhi", "East Delhi", "West Delhi", "South West Delhi"),
    "KA" to listOf("Bengaluru Urban", "Bengaluru Rural", "Mysuru", "Mangaluru (Dakshina Kannada)", "Hubballi-Dharwad", "Belagavi", "Tumakuru"),
    "UP" to listOf("Lucknow", "Kanpur Nagar", "Varanasi", "Prayagraj", "Noida (Gautam Buddha Nagar)", "Agra", "Ghaziabad", "Meerut", "Gorakhpur"),
    "TN" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tirunelveli", "Vellore"),
    "WB" to listOf("Kolkata", "North 24 Parganas", "South 24 Parganas", "Howrah", "Darjeeling", "Hooghly", "Siliguri (Darjeeling/Jalpaiguri)"),
    "GJ" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Gandhinagar", "Jamnagar"),
    "KL" to listOf("Thiruvananthapuram", "Ernakulam (Kochi)", "Kozhikode", "Thrissur", "Kollam", "Malappuram"),
    "RJ" to listOf("Jaipur", "Jodhpur", "Kota", "Udaipur", "Bikaner", "Ajmer", "Alwar")
)

// ==========================================
// 2. EDUCATION BOARDS & GRADE CLASSES
// ==========================================

data class BoardDetails(
    val code: String,
    val shortName: String,
    val fullName: String,
    val isNational: Boolean = false,
    val stateCode: String = "",
    val gradingSystem: String = "Percentage & Grades (A1-E)",
    val syllabusOverview: String = "NCERT / State Curriculum aligned"
)

val SupportedBoardsList = listOf(
    BoardDetails("CBSE", "CBSE", "Central Board of Secondary Education (CBSE)", isNational = true, syllabusOverview = "Standard NCERT curriculum with 80 theory + 20 internal/project"),
    BoardDetails("CISCE", "CISCE / ICSE / ISC", "Council for the Indian School Certificate Examinations", isNational = true, syllabusOverview = "Comprehensive syllabus with high analytical and case-study focus"),
    BoardDetails("AHSEC", "Assam AHSEC / SEBA", "Assam Higher Secondary Education Council & SEBA", stateCode = "AS", syllabusOverview = "State board syllabus aligned with national standards and regional context"),
    BoardDetails("MAHARASHTRA", "Maharashtra HSC / SSC", "Maharashtra State Board of Secondary and Higher Secondary Education", stateCode = "MH", syllabusOverview = "HSC/SSC state board pattern with 80+20 evaluation scheme"),
    BoardDetails("KARNATAKA", "Karnataka KSEAB / 2nd PUC", "Karnataka School Examination and Assessment Board", stateCode = "KA", syllabusOverview = "PUC / SSLC state board curriculum with comprehensive question banks"),
    BoardDetails("UP_BOARD", "UP Board (Madhyamik)", "Uttar Pradesh Madhyamik Shiksha Parishad", stateCode = "UP", syllabusOverview = "One of the largest state boards with NCERT-based curriculum"),
    BoardDetails("TAMIL_NADU", "Tamil Nadu State Board", "Tamil Nadu Directorate of Government Examinations (TNDGE)", stateCode = "TN", syllabusOverview = "State Samacheer Kalvi pattern with 90+10 evaluation"),
    BoardDetails("WEST_BENGAL", "West Bengal WBCHSE / WBBSE", "West Bengal Council of Higher Secondary Education", stateCode = "WB", syllabusOverview = "Higher Secondary & Madhyamik syllabus with regional and national parity"),
    BoardDetails("GUJARAT", "GSEB (Gujarat Board)", "Gujarat Secondary and Higher Secondary Education Board", stateCode = "GJ", syllabusOverview = "GSEB textbook curriculum with bilingual support"),
    BoardDetails("BIHAR", "BSEB (Bihar Board)", "Bihar School Examination Board", stateCode = "BR", syllabusOverview = "50% OMR Objective + 50% Descriptive pattern"),
    BoardDetails("MP_BOARD", "MP Board (MPBSE)", "Madhya Pradesh Board of Secondary Education", stateCode = "MP", syllabusOverview = "80+20 evaluation scheme with NCERT books"),
    BoardDetails("OTHER", "Other Recognized Board", "State / International / Open School Board", isNational = false, syllabusOverview = "Universal Board Preparation Blueprint")
)

data class GradeLevel(
    val id: String,
    val displayName: String,
    val hasStreams: Boolean = false,
    val description: String = ""
)

val SupportedGradesList = listOf(
    GradeLevel("class_6", "Class 6", false, "Middle School Foundation"),
    GradeLevel("class_7", "Class 7", false, "Middle School Concepts"),
    GradeLevel("class_8", "Class 8", false, "Upper Primary Mastery"),
    GradeLevel("class_9", "Class 9", false, "Secondary Foundation"),
    GradeLevel("class_10", "Class 10 (Board Exam)", false, "Secondary School Board Examination"),
    GradeLevel("class_11", "Class 11", true, "Senior Secondary Stream Foundation"),
    GradeLevel("class_12", "Class 12 (Board Exam)", true, "Senior Secondary Final Board Examination"),
    GradeLevel("college", "Undergraduate / College", true, "Higher Education Exam Prep")
)

data class AcademicStream(
    val id: String,
    val displayName: String,
    val emoji: String,
    val description: String
)

val SupportedStreamsList = listOf(
    AcademicStream("science_pcm", "Science (PCM)", "🔬", "Physics, Chemistry, Mathematics, CS/IT, English"),
    AcademicStream("science_pcb", "Science (PCB)", "🧬", "Physics, Chemistry, Biology, Biotech, English"),
    AcademicStream("commerce", "Commerce", "📊", "Accountancy, Business Studies, Economics, Applied Math, English"),
    AcademicStream("arts", "Arts / Humanities", "🎨", "History, Political Science, Geography, Economics, Psychology, English"),
    AcademicStream("vocational", "Vocational / Applied", "🛠️", "Skill subjects, IT, Financial Markets, Tourism, Media"),
    AcademicStream("general", "General / Foundation", "📚", "Core subjects for secondary classes")
)

data class SubjectConfig(
    val id: String,
    val name: String,
    val shortCode: String,
    val iconEmoji: String,
    val isCore: Boolean = true,
    val streamId: String = "general",
    val defaultTarget: Int = 90
)

val DefaultSubjectsCatalog = listOf(
    // Commerce
    SubjectConfig("acc", "Accountancy", "ACC", "📑", true, "commerce", 92),
    SubjectConfig("bst", "Business Studies", "BST", "🏢", true, "commerce", 90),
    SubjectConfig("eco", "Economics & Finance", "ECO", "📈", true, "commerce", 88),
    SubjectConfig("applied_math", "Applied Mathematics", "MATH", "📐", false, "commerce", 85),
    SubjectConfig("ip", "Informatics Practices", "IP", "💻", false, "commerce", 95),

    // Science
    SubjectConfig("phy", "Physics", "PHY", "⚡", true, "science_pcm", 90),
    SubjectConfig("chem", "Chemistry", "CHEM", "🧪", true, "science_pcm", 90),
    SubjectConfig("math", "Mathematics", "MATH", "📐", true, "science_pcm", 92),
    SubjectConfig("bio", "Biology", "BIO", "🧬", true, "science_pcb", 92),
    SubjectConfig("cs", "Computer Science", "CS", "💻", false, "science_pcm", 95),

    // Arts
    SubjectConfig("hist", "History", "HIST", "🏛️", true, "arts", 90),
    SubjectConfig("pol_sci", "Political Science", "POL", "🗳️", true, "arts", 90),
    SubjectConfig("geo", "Geography", "GEO", "🌍", true, "arts", 88),
    SubjectConfig("psych", "Psychology", "PSY", "🧠", false, "arts", 92),
    SubjectConfig("soc", "Sociology", "SOC", "👥", false, "arts", 88),

    // Languages (Common)
    SubjectConfig("eng", "English Core", "ENG", "📖", true, "general", 90),
    SubjectConfig("hin", "Hindi Core", "HIN", "🇮🇳", false, "general", 88),
    SubjectConfig("reg_lang", "Regional Language", "REG", "🗣️", false, "general", 88),

    // Secondary (Class 6-10)
    SubjectConfig("sec_math", "Mathematics", "MATH", "📐", true, "general", 90),
    SubjectConfig("sec_sci", "Science", "SCI", "🔬", true, "general", 90),
    SubjectConfig("sec_sst", "Social Science", "SST", "🌍", true, "general", 88)
)

// ==========================================
// 3. SCHOOL SELECTION & VERIFICATION SYSTEM
// ==========================================

enum class SchoolVerificationStatus {
    UNVERIFIED,      // Selected from public directory
    PENDING_APPROVAL,// Code submitted / awaiting admin confirmation
    VERIFIED         // Institution verified with official code / email
}

data class SchoolItem(
    val id: String,
    val name: String,
    val area: String,
    val district: String,
    val state: String,
    val country: String = "India",
    val affiliatedBoard: String = "CBSE",
    val approxLocation: String = "Coordinates: 26.14° N, 91.73° E",
    val websiteUrl: String = "https://www.schoolboard.edu.in",
    val verifiedStudentCount: Int = 142,
    val schoolCode: String = "BM8821" // Verification code for demo
)

val SampleSchoolsCatalog = listOf(
    SchoolItem("sch_1", "Delhi Public School (DPS)", "Khanapara", "Kamrup Metropolitan (Guwahati)", "Assam", affiliatedBoard = "CBSE", verifiedStudentCount = 284, schoolCode = "DPS2026"),
    SchoolItem("sch_2", "Cotton Collegiate Government H.S. School", "Panbazar", "Kamrup Metropolitan (Guwahati)", "Assam", affiliatedBoard = "AHSEC", verifiedStudentCount = 310, schoolCode = "COTTON"),
    SchoolItem("sch_3", "Kendriya Vidyalaya (IIT Guwahati)", "North Guwahati", "Kamrup Metropolitan (Guwahati)", "Assam", affiliatedBoard = "CBSE", verifiedStudentCount = 195, schoolCode = "KVIIT26"),
    SchoolItem("sch_4", "Don Bosco Higher Secondary School", "Panbazar", "Kamrup Metropolitan (Guwahati)", "Assam", affiliatedBoard = "SEBA", verifiedStudentCount = 230, schoolCode = "DBOSCO"),
    SchoolItem("sch_5", "St. Xavier's High School", "Fort", "Mumbai City", "Maharashtra", affiliatedBoard = "MAHARASHTRA", verifiedStudentCount = 412, schoolCode = "XAVIER"),
    SchoolItem("sch_6", "The Cathedral and John Connon School", "Fort", "Mumbai City", "Maharashtra", affiliatedBoard = "CISCE", verifiedStudentCount = 180, schoolCode = "CATHED"),
    SchoolItem("sch_7", "National Public School (NPS)", "Indiranagar", "Bengaluru Urban", "Karnataka", affiliatedBoard = "CBSE", verifiedStudentCount = 340, schoolCode = "NPSBLR"),
    SchoolItem("sch_8", "Bishop Cotton Boys' School", "St. Mark's Road", "Bengaluru Urban", "Karnataka", affiliatedBoard = "CISCE", verifiedStudentCount = 290, schoolCode = "COTTONB"),
    SchoolItem("sch_9", "Springdales School", "Pusa Road", "New Delhi", "Delhi (NCR)", affiliatedBoard = "CBSE", verifiedStudentCount = 320, schoolCode = "SPRING"),
    SchoolItem("sch_10", "La Martiniere for Boys", "Rawdon Street", "Kolkata", "West Bengal", affiliatedBoard = "CISCE", verifiedStudentCount = 260, schoolCode = "LAMART")
)

// ==========================================
// 4. STUDENT ACADEMIC PROFILE (ROOM PERSISTENCE)
// ==========================================

enum class UserRole {
    STUDENT,
    TEACHER,
    PARENT,
    SCHOOL_ADMIN
}

@Entity(tableName = "student_academic_profile")
data class StudentAcademicProfile(
    @PrimaryKey val id: String = "primary_student",
    val country: String = "India",
    val countryCode: String = "IN",
    val state: String = "Assam",
    val stateCode: String = "AS",
    val district: String = "Kamrup Metropolitan (Guwahati)",
    val areaLocality: String = "Khanapara",
    val boardCode: String = "CBSE",
    val boardDisplayName: String = "CBSE (Central Board)",
    val gradeClassId: String = "class_12",
    val gradeClassDisplayName: String = "Class 12 (Board Exam)",
    val streamId: String = "commerce",
    val streamDisplayName: String = "Commerce",
    val selectedSubjectIds: String = "acc,bst,eco,applied_math,eng", // Comma-separated
    val schoolId: String = "sch_1",
    val schoolName: String = "Delhi Public School (DPS)",
    val schoolArea: String = "Khanapara",
    val schoolVerificationStatus: SchoolVerificationStatus = SchoolVerificationStatus.VERIFIED,
    val preferredLanguage: String = "English",
    val targetPercentage: Int = 90,
    val currentPracticeLevel: Int = 76,
    val streakDays: Int = 7,
    val totalXp: Int = 1250,
    val avatarEmoji: String = "🎓",
    val anonymousHandle: String = "BoardScholar_99",
    val hideFromLeaderboard: Boolean = false,
    val role: UserRole = UserRole.STUDENT,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

data class SubjectTargetItem(
    val subjectId: String,
    val subjectName: String,
    val shortCode: String,
    val iconEmoji: String,
    val targetMark: Int,
    val currentEstimatedMark: Int,
    val accuracyPercentage: Int,
    val status: String
)

// ==========================================
// 5. AI PERSONAL TEACHER & PHOTO QUESTION ANALYSIS
// ==========================================

enum class TeacherPedagogicalMode(
    val id: String,
    val title: String,
    val emoji: String,
    val tagLine: String,
    val systemPromptDirective: String
) {
    FRIEND(
        "friend",
        "Friend Mode",
        "🧑‍🤝‍🧑",
        "Explains like a helpful study buddy with casual warmth",
        "Adopt a warm, encouraging, peer-to-peer tone. Use relatable metaphors and empathetic encouragement."
    ),
    TEACHER(
        "teacher",
        "Teacher Mode",
        "👨‍🏫",
        "Structured classroom instructional explanation",
        "Explain in a structured, pedagogical classroom manner with clear definitions, principles, and summary."
    ),
    BEGINNER(
        "beginner",
        "Beginner Mode",
        "👶",
        "Zero jargon, ultra-simple plain language",
        "Use zero technical jargon without explaining it first. Break complex ideas down into everyday analogies that a 10-year-old can grasp."
    ),
    DEEP_UNDERSTANDING(
        "deep",
        "Deep Understanding",
        "🔬",
        "First-principles derivation & underlying rationale",
        "Explain the underlying first-principles reasoning, mathematical or economic derivation, and logical 'why'."
    ),
    EXAM_MODE(
        "exam",
        "Exam Mode",
        "🎯",
        "Marking schemes, keywords & high-yield exam tips",
        "Highlight what official examiners look for: mandatory keywords, step marks, common deduction traps, and answer structuring."
    ),
    QUICK_REVISION(
        "revision",
        "Quick Revision",
        "⚡",
        "60-second summary & rapid cheat-sheet bullet points",
        "Provide a rapid, high-density summary: 3 core bullet points, 1 key formula/rule, and 1 memory trigger."
    ),
    EXAMPLE_MODE(
        "example",
        "Example Mode",
        "💡",
        "Real-life everyday stories & industry case studies",
        "Anchor every single concept in an engaging real-world story, corporate case study, or everyday situation."
    ),
    PRACTICE(
        "practice",
        "Practice Mode",
        "📝",
        "Crisp explanation followed by 3 active check questions",
        "Explain the core concept in 3 sentences, then provide 3 progressive practice questions with hidden solution hints."
    )
}

data class AiTeacherChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "ai"
    val text: String,
    val mode: TeacherPedagogicalMode = TeacherPedagogicalMode.TEACHER,
    val timestamp: Long = System.currentTimeMillis(),
    val subjectTag: String = "General",
    val isPhotoQuery: Boolean = false,
    val photoSnippet: String? = null,
    val suggestedFollowUps: List<String> = emptyList(),
    val practiceQuestions: List<String> = emptyList()
)

data class PhotoAnalysisResult(
    val subject: String,
    val topic: String,
    val detectedQuestionText: String,
    val conceptualExplanation: String,
    val stepByStepSolution: List<String>,
    val coreConceptTakeaway: String,
    val commonMistakeWarning: String,
    val practiceQuestions: List<String>,
    val practiceAnswers: List<String>
)

// ==========================================
// 6. PYQ ANALYSIS & IMPORTANT QUESTION GENERATOR
// ==========================================

data class PyqChapterTrend(
    val chapterName: String,
    val subject: String,
    val weightagePercentage: Int,
    val fiveYearFrequencyMarks: List<Int>, // e.g. [12, 14, 10, 16, 14]
    val highFrequencyTopics: List<String>,
    val questionTypeBreakdown: String,
    val repeatedConceptAlert: String,
    val priorityTag: String = "High Priority 🔥"
)

data class GeneratedImportantQuestion(
    val id: String = UUID.randomUUID().toString(),
    val subject: String,
    val chapter: String,
    val topic: String,
    val difficulty: String, // "Easy", "Medium", "Hard", "Board Level"
    val expectedMarks: Int,
    val questionType: String, // "MCQ (1M)", "Short Answer (3M)", "Long Answer (5M)", "Case Study (6M)"
    val questionText: String,
    val markingSchemeKeyPoints: List<String>,
    val modelAnswerOutline: String,
    val isAiOriginal: Boolean = true
)

// ==========================================
// 7. BOARD MOCK EXAM & AI ANSWER EVALUATION
// ==========================================

data class BoardMockExam(
    val id: String,
    val title: String,
    val board: String,
    val gradeClass: String,
    val subject: String,
    val timeLimitMinutes: Int,
    val totalMarks: Int,
    val instructions: List<String>,
    val isDailyMock: Boolean = false,
    val questionsCount: Int = 20,
    val difficultyRating: String = "Balanced (Standard Board Level)"
)

data class AiAnswerEvaluationResult(
    val estimatedScore: Double,
    val maxMarks: Double,
    val relevanceScore: Int, // out of 10
    val conceptualCorrectnessScore: Int, // out of 10
    val keyPointsScore: Int, // out of 10
    val structureScore: Int, // out of 10
    val languageClarityScore: Int, // out of 10
    val mistakesIdentified: List<String>,
    val missingKeyPoints: List<String>,
    val actionableImprovements: List<String>,
    val strongerModelAnswer: String,
    val aiDisclaimer: String = "AI Estimated Evaluation — Provided as a formative learning guide; not an official board marking."
)

// ==========================================
// 8. ADAPTIVE STUDY PLAN & DAILY MISSIONS
// ==========================================

data class DailyStudyMission(
    val id: String = UUID.randomUUID().toString(),
    val dateLabel: String,
    val targetMinutes: Int = 75,
    val tasks: List<MissionTaskItem>,
    val totalXpReward: Int = 180,
    val isFullyCompleted: Boolean = false
)

data class MissionTaskItem(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val isDone: Boolean = false,
    val taskType: String = "Concept / Practice"
)

data class CuratedVideoResource(
    val id: String,
    val subject: String,
    val topic: String,
    val teacherOrChannelName: String,
    val videoTitle: String,
    val duration: String,
    val language: String,
    val videoUrl: String,
    val thumbnailUrl: String = ""
)

data class PublicResourceLink(
    val id: String,
    val title: String,
    val subject: String,
    val resourceType: String, // "Official Syllabus", "NCERT Public PDF", "Open Notes", "Exemplar Bank"
    val sourceName: String,
    val url: String
)

// ==========================================
// 9. SCHOOL COMMUNITY & PRIVATE LEADERBOARD
// ==========================================

enum class LeaderboardCategory(val displayName: String, val emoji: String) {
    TOP_SCORE("Top Score", "🏆"),
    MOST_IMPROVED("Most Improved", "📈"),
    STUDY_STREAK("Study Streak", "🔥"),
    QUIZ_CHAMPION("Quiz Champion", "🧠"),
    MISSION_MASTER("Mission Master", "⚡"),
    CONSISTENCY("Consistency Champion", "⭐")
}

data class SchoolLeaderboardEntry(
    val rank: Int,
    val anonymousHandle: String,
    val avatarEmoji: String,
    val scoreValue: String,
    val metricLabel: String,
    val streakDays: Int,
    val categoryBadge: String,
    val isCurrentUser: Boolean = false
)

data class GlobalAggregatedSchoolRank(
    val rank: Int,
    val schoolName: String,
    val district: String,
    val state: String,
    val totalSchoolXp: Int,
    val activeStudentsCount: Int
)

// ==========================================
// 10. BADGES & ACHIEVEMENTS
// ==========================================

data class AcademicAchievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val category: String,
    val isUnlocked: Boolean,
    val unlockedDate: String = "",
    val progressPercent: Int = 100
)

// Helper properties and catalog aliases
val CountryInfo.defaultStates: List<String>
    get() = when (code) {
        "IN" -> listOf("Delhi (NCR)", "Assam", "Maharashtra", "Karnataka", "Uttar Pradesh", "Tamil Nadu", "West Bengal", "Gujarat", "Kerala", "Rajasthan")
        "US" -> listOf("California", "New York", "Texas", "Washington", "Illinois")
        "UK" -> listOf("England", "Scotland", "Wales", "Northern Ireland")
        else -> listOf("National Capital Region", "State 1", "State 2")
    }

val BoardDetails.displayName: String get() = shortName
val BoardDetails.description: String get() = fullName
val GradeLevel.isStreamApplicable: Boolean get() = hasStreams
val SubjectConfig.displayName: String get() = name
val SchoolItem.areaLocality: String get() = area
val SchoolItem.boardAffiliation: String get() = affiliatedBoard

val DefaultBoardsCatalog: List<BoardDetails> get() = SupportedBoardsList
val DefaultCountriesCatalog: List<CountryInfo> get() = SupportedCountries
val DefaultGradesCatalog: List<GradeLevel> get() = SupportedGradesList
val DefaultStreamsCatalog: List<AcademicStream> get() = SupportedStreamsList
val DefaultSchoolsCatalog: List<SchoolItem> get() = SampleSchoolsCatalog

val LeaderboardCategory.title: String get() = displayName
val LeaderboardCategory.unit: String get() = when (this) {
    LeaderboardCategory.TOP_SCORE -> "pts"
    LeaderboardCategory.MOST_IMPROVED -> "%"
    LeaderboardCategory.STUDY_STREAK -> "days"
    LeaderboardCategory.QUIZ_CHAMPION -> "marks"
    LeaderboardCategory.MISSION_MASTER -> "XP"
    LeaderboardCategory.CONSISTENCY -> "hrs"
}

val SchoolLeaderboardEntry.gradeClass: String get() = "Class 12"
val SchoolLeaderboardEntry.badgeTitle: String get() = categoryBadge
val SchoolLeaderboardEntry.scoreOrXp: String get() = scoreValue

val GlobalAggregatedSchoolRank.totalXpPoints: Int get() = totalSchoolXp

val PyqChapterTrend.averageMarksWeightage: Int get() = weightagePercentage
val PyqChapterTrend.frequentlyAskedYears: String get() = "2019-2024"
val PyqChapterTrend.recurringSubtopics: List<String> get() = highFrequencyTopics
val PyqChapterTrend.questionTypeDistribution: String get() = questionTypeBreakdown

val GeneratedImportantQuestion.probabilityScore: Int get() = 94
val GeneratedImportantQuestion.marks: Int get() = expectedMarks
val GeneratedImportantQuestion.markingBreakdown: List<String> get() = markingSchemeKeyPoints
val GeneratedImportantQuestion.idealAnswerOutline: String get() = modelAnswerOutline

