package com.example.data.repository

import com.example.data.model.AiRevisionCheatSheet
import com.example.data.model.TeacherNotesResource
import com.example.data.model.TeacherVideoResource
import com.example.data.model.VideoLectureType

object ResourceRepository {

    val teacherVideos = listOf(
        TeacherVideoResource(
            id = "v1_one_shot_partnership",
            teacherName = "CA Parag Gupta / Commerce Wallah",
            title = "Partnership Fundamentals Full Chapter One Shot 2026",
            chapterName = "Partnership Fundamentals",
            topic = "Complete P&L Appropriation, Interest Calculations & Past Adjustments",
            lectureType = VideoLectureType.ONE_SHOT,
            durationText = "2h 45m",
            publicUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+partnership+fundamentals+one+shot"
        ),
        TeacherVideoResource(
            id = "v2_shares_forfeiture",
            teacherName = "Sunil Panda / Commerce Champions",
            title = "Share Capital Pro-rata Allotment & Forfeiture Mastery",
            chapterName = "Share Capital",
            topic = "Calls in Arrears, Forfeiture, Reissue & Capital Reserve",
            lectureType = VideoLectureType.NUMERICAL_PRACTICE,
            durationText = "1h 30m",
            publicUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+shares+forfeiture+pro+rata"
        ),
        TeacherVideoResource(
            id = "v3_cash_flow_full",
            teacherName = "Rajat Arora",
            title = "Cash Flow Statement (AS-3) Operating & Investing Complete",
            chapterName = "Cash Flow Statement",
            topic = "Step-by-Step Operating Activities & Asset Account Working Notes",
            lectureType = VideoLectureType.FULL_CHAPTER,
            durationText = "2h 10m",
            publicUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+cash+flow+statement+rajat+arora"
        ),
        TeacherVideoResource(
            id = "v4_pyq_marathon",
            teacherName = "Unacademy Commerce",
            title = "Top 50 PYQs Class 12 Accountancy Board Exam (2020-2025)",
            chapterName = "All Chapters",
            topic = "Complete 80-Mark Model Paper Solving & Presentation Tips",
            lectureType = VideoLectureType.PYQ_DISCUSSION,
            durationText = "3h 15m",
            publicUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+pyq+marathon"
        ),
        TeacherVideoResource(
            id = "v5_exam_strategy",
            teacherName = "Expert Commerce Faculty",
            title = "Mission 100: How to Score 100/100 in Class 12 Accountancy",
            chapterName = "Exam Strategy",
            topic = "Time Management: 15-min reading time, working notes presentation & avoiding calculation traps",
            lectureType = VideoLectureType.EXAM_STRATEGY,
            durationText = "45m",
            publicUrl = "https://www.youtube.com/results?search_query=class+12+accountancy+how+to+score+100+strategy"
        )
    )

    val teacherNotes = listOf(
        TeacherNotesResource(
            id = "tn_1_ncert_official",
            teacherOrSource = "NCERT Official Resource Portal",
            title = "NCERT Class 12 Accountancy Part 1 & Part 2 Official e-Textbooks",
            chapterName = "All Chapters",
            description = "Official prescribed textbook PDFs and chapter-wise illustrations from NCERT.",
            officialResourceUrl = "https://ncert.nic.in/textbook.php"
        ),
        TeacherNotesResource(
            id = "tn_2_cbse_curriculum",
            teacherOrSource = "CBSE Academic Portal",
            title = "Official CBSE Class 12 Accountancy Syllabus & Sample Question Papers",
            chapterName = "Curriculum & Marking Scheme",
            description = "Official blueprint, design of question paper, and step-marking guidelines.",
            officialResourceUrl = "https://cbseacademic.nic.in/sqp_classxii.html"
        ),
        TeacherNotesResource(
            id = "tn_3_free_notes",
            teacherOrSource = "Commerce Study Hub",
            title = "Summary Formula Booklet: Accounting Ratios & Cash Flow Formats",
            chapterName = "Financial Analysis",
            description = "Quick reference PDF tables for all 16 board exam ratio formulas.",
            officialResourceUrl = "https://www.google.com/search?q=class+12+accountancy+formula+sheet+pdf"
        )
    )

    val aiCheatSheets = listOf(
        AiRevisionCheatSheet(
            id = "cs_1_deed",
            chapterName = "Partnership Fundamentals",
            title = "Partnership Deed Silence Rules (Section 13)",
            keyFormulas = listOf(
                "Profit Sharing: 1:1 (Equal)",
                "Interest on Capital: NIL",
                "Interest on Drawings: NIL",
                "Salary/Commission: NIL",
                "Partner's Loan Interest: 6% p.a. (Charge to P&L)"
            ),
            accountingRules = listOf(
                "P&L Appropriation Account is a NOMINAL account.",
                "Drawings date omitted -> 6 months average period.",
                "Capitals Fixed -> All adjustments in Partners' CURRENT Accounts."
            ),
            examMistakeAlerts = listOf(
                "Never write Interest on Loan in P&L Appropriation A/c (It is a charge against profit, put in P&L A/c).",
                "If % p.a. is NOT given in Interest on Drawings, ignore time and apply flat rate."
            ),
            stepByStepFormat = "P&L Appropriation A/c:\nDr Side: To IOC, To Salary, To Commission, To General Reserve, To Divisible Profit.\nCr Side: By Net Profit b/d, By Interest on Drawings."
        ),
        AiRevisionCheatSheet(
            id = "cs_2_shares",
            chapterName = "Share Capital",
            title = "Pro-rata & Forfeiture Super Cheat Sheet",
            keyFormulas = listOf(
                "Category Ratio = Shares Applied : Shares Allotted",
                "Surplus App Money = (Shares Applied - Shares Allotted) × App Money per share",
                "Net Due on Allotment = Allotment Due - Surplus App Money",
                "Capital Reserve = (Forfeited amount on reissued shares) - (Discount on Reissue)"
            ),
            accountingRules = listOf(
                "Debit Share Capital with CALLED-UP Face Value only.",
                "Credit Share Forfeiture with amount actually received towards face value.",
                "Securities Premium received -> CANNOT be debited (Sec 52).",
                "Securities Premium NOT received -> MUST be debited."
            ),
            examMistakeAlerts = listOf(
                "Don't transfer total Share Forfeiture balance to Capital Reserve if only a PART of shares were reissued! Transfer only the proportion for reissued shares."
            ),
            stepByStepFormat = "Forfeiture Journal:\nShare Capital A/c Dr. (Shares × Called up)\n[Securities Premium Dr. (if unpaid)]\n  To Share Forfeiture A/c (amount paid towards face value)\n  To Calls in Arrears A/c (unpaid money)"
        ),
        AiRevisionCheatSheet(
            id = "cs_3_cashflow",
            chapterName = "Cash Flow Statement",
            title = "Operating Activities 5-Step Rapid Framework",
            keyFormulas = listOf(
                "Net Profit before Tax = Closing P&L - Opening P&L + Proposed Dividend (Prev Year) + Interim Div + Tax Provision + Transfer to Reserve",
                "Operating Profit = NP before Tax + Non-Cash Expenses - Non-Operating Incomes",
                "Working Capital = Operating Profit + Decrease in CA + Increase in CL - Increase in CA - Decrease in CL",
                "Cash Flow from Operations = Working Capital - Income Tax Paid"
            ),
            accountingRules = listOf(
                "Bank Overdraft is Financing, not Cash & Cash Equivalent.",
                "Proposed Dividend: Add previous year in Operating, deduct in Financing.",
                "Provision for Tax: Add current year provision in Operating, deduct actual tax paid in Operating."
            ),
            examMistakeAlerts = listOf(
                "Loose Tools and Stores & Spares are NOT included in Current Assets for Cash Flow working capital changes (they are non-operating inventory items)."
            ),
            stepByStepFormat = "AS-3 Statement:\nA. Cash Flows from Operating Activities\nB. Cash Flows from Investing Activities\nC. Cash Flows from Financing Activities\nNet Increase/Decrease in Cash (A+B+C) + Opening Cash & Cash Eq. = Closing Cash & Cash Eq."
        )
    )
}
