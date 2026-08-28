package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AcademicAchievement
import com.example.data.model.BoardDetails
import com.example.data.model.BoardMockExam
import com.example.data.model.CuratedVideoResource
import com.example.data.model.DailyStudyMission
import com.example.data.model.GeneratedImportantQuestion
import com.example.data.model.GlobalAggregatedSchoolRank
import com.example.data.model.LeaderboardCategory
import com.example.data.model.MissionTaskItem
import com.example.data.model.PublicResourceLink
import com.example.data.model.PyqChapterTrend
import com.example.data.model.SchoolItem
import com.example.data.model.SchoolLeaderboardEntry
import com.example.data.model.SchoolVerificationStatus
import com.example.data.model.StudentAcademicProfile
import com.example.data.model.SubjectConfig
import com.example.data.model.SubjectTargetItem
import com.example.data.model.SupportedBoardsList
import com.example.data.model.SupportedGradesList
import com.example.data.model.SupportedStreamsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

object BoardMentorRepository {

    // ==========================================
    // 1. DEFAULT MISSIONS & ADAPTIVE STUDY PLANS
    // ==========================================
    fun getTodayStudyMission(profile: StudentAcademicProfile): DailyStudyMission {
        return DailyStudyMission(
            dateLabel = "Today's Target Mission",
            targetMinutes = 75,
            tasks = listOf(
                MissionTaskItem(
                    id = "m1",
                    title = "Accountancy: Revaluation & Goodwill",
                    subject = "Accountancy",
                    description = "Practice 2 numericals on admission of partner & revaluation balancing",
                    durationMinutes = 30,
                    xpReward = 60,
                    isDone = true,
                    taskType = "Concept & Practice"
                ),
                MissionTaskItem(
                    id = "m2",
                    title = "Business Studies: Marketing Mix",
                    subject = "Business Studies",
                    description = "Solve 20 high-frequency MCQs on 4 Ps of Marketing",
                    durationMinutes = 20,
                    xpReward = 40,
                    isDone = false,
                    taskType = "MCQ Drill"
                ),
                MissionTaskItem(
                    id = "m3",
                    title = "Revise Weak Topic: Cash Flow Operating Activities",
                    subject = "Accountancy",
                    description = "Review non-cash adjustments and working capital changes",
                    durationMinutes = 15,
                    xpReward = 40,
                    isDone = false,
                    taskType = "Weak Topic Recovery"
                ),
                MissionTaskItem(
                    id = "m4",
                    title = "Complete 1 Board-Style Case Study Question",
                    subject = "Economics / Business",
                    description = "Practice structured 5-mark answer writing with working notes",
                    durationMinutes = 10,
                    xpReward = 40,
                    isDone = false,
                    taskType = "Board Exam Practice"
                )
            ),
            totalXpReward = 180,
            isFullyCompleted = false
        )
    }

    // ==========================================
    // 2. SUBJECT TARGETS BREAKDOWN
    // ==========================================
    fun calculateSubjectTargets(targetPercent: Int, selectedSubjects: List<SubjectConfig>): List<SubjectTargetItem> {
        return selectedSubjects.map { subject ->
            val adjustedTarget = when (subject.shortCode) {
                "ACC" -> (targetPercent + 2).coerceAtMost(100)
                "BST" -> targetPercent.coerceAtMost(100)
                "ECO" -> (targetPercent - 2).coerceAtLeast(60)
                "PHY" -> targetPercent.coerceAtMost(100)
                "CHEM" -> (targetPercent - 1).coerceAtLeast(60)
                "MATH" -> (targetPercent + 1).coerceAtMost(100)
                "ENG" -> (targetPercent - 1).coerceAtLeast(60)
                else -> targetPercent
            }
            val currentEstimated = (adjustedTarget - (8..15).random()).coerceAtLeast(50)
            val accuracy = (currentEstimated * 100) / adjustedTarget

            SubjectTargetItem(
                subjectId = subject.id,
                subjectName = subject.name,
                shortCode = subject.shortCode,
                iconEmoji = subject.iconEmoji,
                targetMark = adjustedTarget,
                currentEstimatedMark = currentEstimated,
                accuracyPercentage = accuracy,
                status = if (accuracy >= 85) "On Track 🟢" else if (accuracy >= 70) "Moderate Focus 🟡" else "Needs Improvement 🔴"
            )
        }
    }

    // ==========================================
    // 3. PYQ 5-10 YEARS ANALYSIS TRENDS
    // ==========================================
    fun getPyqTrendsForSubject(subject: String): List<PyqChapterTrend> {
        return listOf(
            PyqChapterTrend(
                chapterName = "Partnership Accounts: Admission & Retirement",
                subject = "Accountancy",
                weightagePercentage = 28,
                fiveYearFrequencyMarks = listOf(14, 16, 12, 18, 16),
                highFrequencyTopics = listOf(
                    "Revaluation A/c profit sharing",
                    "Treatment of Goodwill as per AS-26",
                    "Hidden Goodwill on Admission",
                    "Adjustment of Capital Accounts"
                ),
                questionTypeBreakdown = "1M MCQ (3) • 3M Short (1) • 6M Long Comprehensive (2)",
                repeatedConceptAlert = "Appeared in 9 of last 10 board exams. Revaluation account with undervalued assets is a recurring staple.",
                priorityTag = "Must Master 🔥"
            ),
            PyqChapterTrend(
                chapterName = "Accounting for Companies: Share Capital",
                subject = "Accountancy",
                weightagePercentage = 24,
                fiveYearFrequencyMarks = listOf(16, 14, 18, 16, 20),
                highFrequencyTopics = listOf(
                    "Pro-rata Allotment & Calls in Arrear",
                    "Forfeiture of Shares issued at Premium",
                    "Re-issue of Forfeited Shares at Discount",
                    "Capital Reserve Calculation"
                ),
                questionTypeBreakdown = "1M MCQ (4) • 4M Short (1) • 6M Comprehensive (1)",
                repeatedConceptAlert = "Pro-rata category table calculation appears every year in the 6-mark compulsory section.",
                priorityTag = "High Weightage 📈"
            ),
            PyqChapterTrend(
                chapterName = "Cash Flow Statement (AS-3 Revised)",
                subject = "Accountancy",
                weightagePercentage = 15,
                fiveYearFrequencyMarks = listOf(8, 8, 10, 8, 8),
                highFrequencyTopics = listOf(
                    "Cash Flow from Operating Activities",
                    "Provision for Tax & Tax Paid Treatment",
                    "Sale and Purchase of Fixed Assets with Depreciation"
                ),
                questionTypeBreakdown = "1M MCQ (2) • 6M Full Problem (1)",
                repeatedConceptAlert = "Operating cash flow adjustment with accumulated depreciation ledger account is tested consistently.",
                priorityTag = "Guaranteed 6-Mark 🎯"
            ),
            PyqChapterTrend(
                chapterName = "Accounting for Companies: Debentures",
                subject = "Accountancy",
                weightagePercentage = 12,
                fiveYearFrequencyMarks = listOf(6, 8, 6, 8, 6),
                highFrequencyTopics = listOf(
                    "Issue of Debentures with Terms of Redemption",
                    "Debentures issued as Collateral Security",
                    "Writing off Loss on Issue of Debentures"
                ),
                questionTypeBreakdown = "1M MCQ (2) • 3M Journal Entry (1) • 4M Problem (1)",
                repeatedConceptAlert = "Loss on Issue written off in the year of issue itself from Securities Premium.",
                priorityTag = "High Scoring 💡"
            ),
            PyqChapterTrend(
                chapterName = "Dissolution of Partnership Firm",
                subject = "Accountancy",
                weightagePercentage = 11,
                fiveYearFrequencyMarks = listOf(6, 8, 6, 6, 8),
                highFrequencyTopics = listOf(
                    "Realisation Account Journal Entries",
                    "Treatment of Partner Loan & Unrecorded Assets",
                    "Settlement of Creditors taking over assets"
                ),
                questionTypeBreakdown = "1M MCQ (2) • 6M Journal / Realisation A/c (1)",
                repeatedConceptAlert = "6 journal entries on dissolution scenarios tested as an alternative or direct question.",
                priorityTag = "Exam Favorite ⭐"
            )
        )
    }

    // ==========================================
    // 4. AI IMPORTANT QUESTION BANK
    // ==========================================
    fun getImportantQuestionBank(subject: String): List<GeneratedImportantQuestion> {
        return listOf(
            GeneratedImportantQuestion(
                id = "iq_1",
                subject = "Accountancy",
                chapter = "Admission of a Partner",
                topic = "Hidden Goodwill & Capital Adjustment",
                difficulty = "Board Level (Medium-High)",
                expectedMarks = 6,
                questionType = "Long Comprehensive (6 Marks)",
                questionText = "A and B are partners sharing profits in ratio 3:2 with capitals ₹1,80,000 and ₹1,40,000. They admit C for 1/5th share. C brings ₹1,00,000 as his capital but cannot bring cash for his share of goodwill. Pass necessary journal entries for goodwill and prepare Partners' Capital Accounts.",
                markingSchemeKeyPoints = listOf(
                    "• Total Capital of Firm based on C = ₹1,00,000 × 5 = ₹5,00,000 [1 Mark]",
                    "• Combined Actual Capital = ₹1,80,000 + ₹1,40,000 + ₹1,00,000 = ₹4,20,000 [1 Mark]",
                    "• Hidden Goodwill = ₹5,00,000 - ₹4,20,000 = ₹80,000 [1 Mark]",
                    "• C's share of Goodwill = ₹80,000 × 1/5 = ₹16,000 [1 Mark]",
                    "• Dr. C Current A/c ₹16,000 To A Capital ₹9,600 To B Capital ₹6,400 (Sacrificing 3:2) [2 Marks]"
                ),
                modelAnswerOutline = "1. Calculate Hidden Goodwill through total capitalized firm value.\n2. Adjust incoming partner's share through Current Account as per AS-26.\n3. Credit sacrificing partners in their sacrificing ratio.",
                isAiOriginal = true
            ),
            GeneratedImportantQuestion(
                id = "iq_2",
                subject = "Accountancy",
                chapter = "Share Capital",
                topic = "Pro-Rata Allotment with Calls in Arrear",
                difficulty = "Board Level (Hard)",
                expectedMarks = 6,
                questionType = "Long Numerical (6 Marks)",
                questionText = "Apex Ltd. invited applications for 50,000 shares of ₹10 each at 20% premium. Applications were received for 80,000 shares. Pro-rata allotment was made to all applicants. Surplus application money was adjusted towards allotment. Mohan (allotted 1,000 shares) failed to pay allotment and first call. His shares were forfeited. Pass journal entries.",
                markingSchemeKeyPoints = listOf(
                    "• Category Table: 80,000 applied : 50,000 allotted (Ratio 8:5) [1 Mark]",
                    "• Mohan applied = 1,000 × (8/5) = 1,600 shares. Excess application money = 600 × ₹3 = ₹1,800 [1 Mark]",
                    "• Net Allotment Arrear = Due (₹5 × 1,000 = ₹5,000) - Advance (₹1,800) = ₹3,200 [2 Marks]",
                    "• Share Forfeiture Entry with Securities Premium cancellation [2 Marks]"
                ),
                modelAnswerOutline = "Show clear table showing applied vs allotted, calculate excess application money credited to allotment, determine net unpaid call, and write clean forfeiture entry.",
                isAiOriginal = true
            ),
            GeneratedImportantQuestion(
                id = "iq_3",
                subject = "Accountancy",
                chapter = "Cash Flow Statement",
                topic = "Operating Activities with Tax Provision",
                difficulty = "Board Level (Medium)",
                expectedMarks = 6,
                questionType = "Comprehensive (6 Marks)",
                questionText = "From the given Balance Sheet excerpts, calculate Cash Flow from Operating Activities: Net Profit before tax ₹2,40,000; Depreciation on Plant ₹35,000; Loss on sale of furniture ₹6,000; Trade Receivables increased by ₹20,000; Inventories decreased by ₹15,000; Tax paid ₹30,000.",
                markingSchemeKeyPoints = listOf(
                    "• Operating profit before Working Capital changes = ₹2,40,000 + ₹35,000 + ₹6,000 = ₹2,81,000 [2 Marks]",
                    "• Working Capital Adjustments: + Inventories (₹15,000) - Trade Receivables (₹20,000) = Net -₹5,000 [2 Marks]",
                    "• Cash generated from operations = ₹2,76,000 [1 Mark]",
                    "• Net Cash from Operating Activities = ₹2,76,000 - ₹30,000 Tax Paid = ₹2,46,000 [1 Mark]"
                ),
                modelAnswerOutline = "Use standard AS-3 Indirect method format with bold subheadings and clear positive/negative brackets.",
                isAiOriginal = true
            )
        )
    }

    // ==========================================
    // 5. CURATED EDUCATIONAL VIDEO & NOTES RESOURCES
    // ==========================================
    fun getCuratedVideos(subject: String): List<CuratedVideoResource> {
        return listOf(
            CuratedVideoResource(
                id = "v1",
                subject = "Accountancy",
                topic = "Partnership: Admission, Goodwill & Revaluation Masterclass",
                teacherOrChannelName = "Sunil Panda - Commerce Master",
                videoTitle = "Complete Chapter 4 in One Shot | Class 12 Board Exam Preparation",
                duration = "52 mins",
                language = "Hindi / English",
                videoUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+admission+of+partner+one+shot",
                thumbnailUrl = ""
            ),
            CuratedVideoResource(
                id = "v2",
                subject = "Accountancy",
                topic = "Share Capital: Pro-Rata Master Trick & Forfeiture",
                teacherOrChannelName = "CA Parag Gupta",
                videoTitle = "Pro Rata Category Table Made Easiest | Board Exam 100/100 Series",
                duration = "44 mins",
                language = "Hindi",
                videoUrl = "https://www.youtube.com/results?search_query=class+12+pro+rata+share+capital+ca+parag+gupta",
                thumbnailUrl = ""
            ),
            CuratedVideoResource(
                id = "v3",
                subject = "Accountancy",
                topic = "Cash Flow Statement (AS-3 Revised) Complete Format",
                teacherOrChannelName = "Rajat Arora",
                videoTitle = "Cash Flow Statement Zero to Hero Full Concept with Adjustments",
                duration = "48 mins",
                language = "Hindi / English",
                videoUrl = "https://www.youtube.com/results?search_query=class+12+cash+flow+statement+rajat+arora",
                thumbnailUrl = ""
            ),
            CuratedVideoResource(
                id = "v4",
                subject = "Business Studies",
                topic = "Principles of Management: Fayol & Taylor",
                teacherOrChannelName = "Magnet Brains",
                videoTitle = "14 Principles of Henri Fayol with Real Case Studies",
                duration = "36 mins",
                language = "English / Hindi",
                videoUrl = "https://www.youtube.com/results?search_query=class+12+business+studies+principles+of+management",
                thumbnailUrl = ""
            ),
            CuratedVideoResource(
                id = "v5",
                subject = "Economics",
                topic = "National Income: Value Added, Income & Expenditure Methods",
                teacherOrChannelName = "Unacademy Commerce",
                videoTitle = "National Income Aggregates & Numericals One Shot",
                duration = "58 mins",
                language = "Hindi / English",
                videoUrl = "https://www.youtube.com/results?search_query=class+12+macroeconomics+national+income+numericals",
                thumbnailUrl = ""
            )
        )
    }

    fun getPublicNotesResources(subject: String): List<PublicResourceLink> {
        return listOf(
            PublicResourceLink(
                id = "n1",
                title = "NCERT Class 12 Official Digital Textbook (Part 1 & 2)",
                subject = "Accountancy",
                resourceType = "Official NCERT PDF",
                sourceName = "NCERT Public Portal",
                url = "https://ncert.nic.in/textbook.php"
            ),
            PublicResourceLink(
                id = "n2",
                title = "CBSE Official Sample Question Papers & Marking Scheme 2025-26",
                subject = "Accountancy / All",
                resourceType = "Official Board Blueprint",
                sourceName = "CBSE Academic Portal",
                url = "https://cbseacademic.nic.in/SQP_CLASSXII_2024_25.html"
            ),
            PublicResourceLink(
                id = "n3",
                title = "State Board Higher Secondary Model Solutions & Topper Answer Sheets",
                subject = "All Subjects",
                resourceType = "Official Exemplar Bank",
                sourceName = "State Board Open Education",
                url = "https://ahsec.assam.gov.in"
            ),
            PublicResourceLink(
                id = "n4",
                title = "Comprehensive Formula & Journal Entry Cheat Sheet",
                subject = "Accountancy",
                resourceType = "Open Quick Notes",
                sourceName = "Commerce Master Community",
                url = "https://www.khanacademy.org/humanities/finance-capital-markets"
            )
        )
    }

    // ==========================================
    // 6. SCHOOL LEADERBOARD & GLOBAL CHALLENGES
    // ==========================================
    fun getSchoolLeaderboard(schoolName: String, category: LeaderboardCategory): List<SchoolLeaderboardEntry> {
        return when (category) {
            LeaderboardCategory.TOP_SCORE -> listOf(
                SchoolLeaderboardEntry(1, "Scholar_Aarav", "🦁", "98.5%", "Score", 18, "Top Scorer 🏆"),
                SchoolLeaderboardEntry(2, "BoardMentor_Priya", "⚡", "96.8%", "Score", 14, "Top Scorer 🏆"),
                SchoolLeaderboardEntry(3, "BoardScholar_99", "🎓", "94.2%", "Score", 7, "Top Scorer 🏆", isCurrentUser = true),
                SchoolLeaderboardEntry(4, "CommerceAce_Dev", "🚀", "91.0%", "Score", 12, "Star Performer ⭐"),
                SchoolLeaderboardEntry(5, "ExamNinja_Sneha", "🌟", "89.5%", "Score", 9, "Star Performer ⭐")
            )
            LeaderboardCategory.MOST_IMPROVED -> listOf(
                SchoolLeaderboardEntry(1, "RapidLearner_Kabir", "📈", "+24%", "Growth", 11, "Most Improved 🚀"),
                SchoolLeaderboardEntry(2, "BoardScholar_99", "🎓", "+18%", "Growth", 7, "Most Improved 🚀", isCurrentUser = true),
                SchoolLeaderboardEntry(3, "FocusStar_Tanvi", "✨", "+15%", "Growth", 15, "Growth Champion 📈"),
                SchoolLeaderboardEntry(4, "MasterMind_Rohan", "🧠", "+12%", "Growth", 8, "Steady Progress 📊")
            )
            LeaderboardCategory.STUDY_STREAK -> listOf(
                SchoolLeaderboardEntry(1, "Unstoppable_Ananya", "🔥", "42 Days", "Streak", 42, "Streak Legend 🔥"),
                SchoolLeaderboardEntry(2, "Consistent_Rahul", "🔥", "28 Days", "Streak", 28, "Streak Master 🔥"),
                SchoolLeaderboardEntry(3, "DailyGrinder_Sam", "🔥", "21 Days", "Streak", 21, "Dedicated ⭐"),
                SchoolLeaderboardEntry(4, "BoardScholar_99", "🎓", "7 Days", "Streak", 7, "Rising Streak 🔥", isCurrentUser = true)
            )
            LeaderboardCategory.QUIZ_CHAMPION -> listOf(
                SchoolLeaderboardEntry(1, "MCQ_King_Vikram", "👑", "1,840 XP", "Quiz XP", 16, "Quiz Master 🧠"),
                SchoolLeaderboardEntry(2, "BoardScholar_99", "🎓", "1,250 XP", "Quiz XP", 7, "Quiz Expert 💡", isCurrentUser = true),
                SchoolLeaderboardEntry(3, "SpeedySolver_Nisha", "⚡", "1,180 XP", "Quiz XP", 12, "Speed Demon ⚡")
            )
            LeaderboardCategory.MISSION_MASTER -> listOf(
                SchoolLeaderboardEntry(1, "MissionHero_Karan", "🎯", "34 Missions", "Completed", 34, "Mission Legend 🎯"),
                SchoolLeaderboardEntry(2, "BoardScholar_99", "🎓", "22 Missions", "Completed", 7, "Mission Achiever ⚡", isCurrentUser = true),
                SchoolLeaderboardEntry(3, "TaskCrusher_Isha", "✅", "19 Missions", "Completed", 19, "Consistent ✅")
            )
            LeaderboardCategory.CONSISTENCY -> listOf(
                SchoolLeaderboardEntry(1, "AlwaysPresent_Om", "⭐", "100%", "Attendance", 30, "Punctual Star ⭐"),
                SchoolLeaderboardEntry(2, "BoardScholar_99", "🎓", "96%", "Attendance", 7, "Highly Regular ⭐", isCurrentUser = true),
                SchoolLeaderboardEntry(3, "SteadyGamer_Tara", "🌟", "92%", "Attendance", 24, "Steady ⭐")
            )
        }
    }

    fun getGlobalAggregatedSchoolRanks(): List<GlobalAggregatedSchoolRank> {
        return listOf(
            GlobalAggregatedSchoolRank(1, "Delhi Public School (DPS)", "Kamrup Metropolitan", "Assam", 148500, 284),
            GlobalAggregatedSchoolRank(2, "National Public School (NPS)", "Bengaluru Urban", "Karnataka", 139200, 340),
            GlobalAggregatedSchoolRank(3, "Cotton Collegiate Government H.S. School", "Kamrup Metropolitan", "Assam", 132400, 310),
            GlobalAggregatedSchoolRank(4, "St. Xavier's High School", "Mumbai City", "Maharashtra", 128900, 412),
            GlobalAggregatedSchoolRank(5, "Springdales School", "New Delhi", "Delhi (NCR)", 119800, 320),
            GlobalAggregatedSchoolRank(6, "Bishop Cotton Boys' School", "Bengaluru Urban", "Karnataka", 112400, 290)
        )
    }

    // ==========================================
    // 7. ACADEMIC ACHIEVEMENTS & BADGES
    // ==========================================
    fun getAcademicAchievements(): List<AcademicAchievement> {
        return listOf(
            AcademicAchievement("ach_1", "First Mock Completed", "Complete your very first full-length board mock examination", "🏆", "Exam Milestones", true, "Unlocked on Aug 15"),
            AcademicAchievement("ach_2", "7-Day Study Streak", "Study consistently for 7 consecutive days without missing a mission", "🔥", "Consistency", true, "Unlocked Today!"),
            AcademicAchievement("ach_3", "100 Questions Completed", "Solve 100 objective and board-style questions across all subjects", "📚", "Practice Drills", true, "Unlocked on Aug 20"),
            AcademicAchievement("ach_4", "Target Progress (75%+)", "Reach an estimated overall practice score of 75% or higher", "🎯", "Mastery", true, "Unlocked on Aug 22"),
            AcademicAchievement("ach_5", "Concept Master", "Complete all fundamental concept lessons in Accountancy & Business Studies", "🧠", "Curriculum", false, "", 68),
            AcademicAchievement("ach_6", "Fast Learner", "Score 90%+ in a daily revision test in under 8 minutes", "⚡", "Speed & Accuracy", false, "", 80),
            AcademicAchievement("ach_7", "Most Improved", "Improve your weakest subject score by more than 15 percentage points", "📈", "Growth", true, "Unlocked on Aug 24"),
            AcademicAchievement("ach_8", "School Champion", "Rank in the Top 5 of your private verified school leaderboard", "🏅", "School Spirit", true, "Unlocked on Aug 26")
        )
    }

    // ==========================================
    // 8. BOARD MOCK EXAMS & DAILY TESTS
    // ==========================================
    fun getAvailableMockExams(board: String, subject: String): List<BoardMockExam> {
        return listOf(
            BoardMockExam(
                id = "mock_full_1",
                title = "$board Full Board Simulation 2026 (Model Paper 1)",
                board = board,
                gradeClass = "Class 12",
                subject = subject,
                timeLimitMinutes = 180,
                totalMarks = 80,
                instructions = listOf(
                    "This question paper contains 34 questions in 2 sections (Part A and Part B).",
                    "All questions are compulsory according to official board blueprint.",
                    "Questions 1 to 20 carry 1 mark each (MCQs).",
                    "Questions 21 to 26 carry 3 marks each.",
                    "Questions 27 to 29 carry 4 marks each.",
                    "Questions 30 to 34 carry 6 marks each.",
                    "There is no overall choice. Internal choices are provided in 7 questions."
                ),
                questionsCount = 34,
                difficultyRating = "Standard Board Blueprint 🎯"
            ),
            BoardMockExam(
                id = "mock_daily_10",
                title = "Daily Quick Revision Drill (10 Questions)",
                board = board,
                gradeClass = "Class 12",
                subject = subject,
                timeLimitMinutes = 15,
                totalMarks = 20,
                instructions = listOf(
                    "10 high-priority questions to test today's concepts.",
                    "Instant step-by-step solutions and XP rewards.",
                    "Optional test: skipping does not break study streaks."
                ),
                isDailyMock = true,
                questionsCount = 10,
                difficultyRating = "Quick & High-Yield ⚡"
            ),
            BoardMockExam(
                id = "mock_daily_20",
                title = "Daily Comprehensive Practice (20 Questions)",
                board = board,
                gradeClass = "Class 12",
                subject = subject,
                timeLimitMinutes = 30,
                totalMarks = 40,
                instructions = listOf(
                    "20 mixed-level questions covering key chapters.",
                    "Includes numerical and concept-based multiple choice questions."
                ),
                isDailyMock = true,
                questionsCount = 20,
                difficultyRating = "Standard Practice 📝"
            ),
            BoardMockExam(
                id = "mock_pyq_master",
                title = "Past 5-Year Board PYQ Challenge Paper",
                board = board,
                gradeClass = "Class 12",
                subject = subject,
                timeLimitMinutes = 120,
                totalMarks = 60,
                instructions = listOf(
                    "Curated from the most repeated board exam questions across 2020-2025.",
                    "Special marking notes and examiner grading criteria."
                ),
                questionsCount = 25,
                difficultyRating = "Past Board Repeat Master 🔥"
            )
        )
    }

    fun getGlobalSchoolRankings(): List<GlobalAggregatedSchoolRank> = getGlobalAggregatedSchoolRanks()
    fun getAchievementsList(): List<AcademicAchievement> = getAcademicAchievements()
    fun getPyqTrends(subject: String): List<PyqChapterTrend> = getPyqTrendsForSubject(subject)
    fun getSchoolLeaderboard(category: LeaderboardCategory, schoolName: String): List<SchoolLeaderboardEntry> = getSchoolLeaderboard(schoolName, category)
}

