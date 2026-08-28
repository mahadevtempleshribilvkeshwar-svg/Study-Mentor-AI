package com.example.data.repository

import com.example.data.model.EducationBoard
import com.example.data.model.ExamCategory
import com.example.data.model.ExamQuestion
import com.example.data.model.JournalEntryRow
import com.example.data.model.QuestionType

object ExamRepository {

    val allPyqQuestions: List<ExamQuestion> = listOf(
        ExamQuestion(
            id = "pyq_cbse_2025_1",
            chapterId = "acc_ch1_fundamentals",
            chapterName = "Partnership Fundamentals",
            questionNumber = 1,
            marks = 1,
            type = QuestionType.MCQ,
            questionText = "In the absence of a partnership deed, what rate of interest is allowed on a loan advanced by a partner to the firm?",
            instructions = "Select the single correct option in accordance with the Indian Partnership Act, 1932.",
            mcqOptions = listOf(
                "A) 12% per annum",
                "B) 6% per annum",
                "C) No interest is allowed",
                "D) At current bank lending rate"
            ),
            correctMcqIndex = 1,
            modelAnswerExplanation = "As per Section 13(d) of Indian Partnership Act, 1932, in the absence of any agreement, a partner is entitled to interest @ 6% p.a. on loan/advance given to the firm.",
            markingRubricSteps = listOf("1 Mark: Correct identification of 6% p.a."),
            yearTag = "2025 Board Paper",
            boardTag = "CBSE"
        ),
        ExamQuestion(
            id = "pyq_cbse_2025_2",
            chapterId = "acc_ch6_shares",
            chapterName = "Accounting for Share Capital",
            questionNumber = 2,
            marks = 3,
            type = QuestionType.JOURNAL_WORKSPACE,
            questionText = "Sunlight Ltd. forfeited 300 equity shares of ₹10 each, issued at a premium of ₹2 per share, for non-payment of first call of ₹3 per share. The final call of ₹2 per share was not yet made. Pass the journal entry for forfeiture.",
            instructions = "Prepare the Journal entry in the blank workspace. Specify Account Debited, Account Credited, Called-up Face Value and Narration.",
            modelAnswerJournal = listOf(
                JournalEntryRow(
                    date = "2025-03-15",
                    particulars = "Equity Share Capital A/c (300 × ₹8 called-up)\n  To Share Forfeiture A/c (300 × ₹5 paid towards face value)\n  To Equity Share First Call A/c (or Calls in Arrears)",
                    lf = "12",
                    debitAmount = "2400",
                    creditAmount = "2400",
                    narration = "Being 300 shares forfeited for non-payment of first call"
                )
            ),
            modelAnswerExplanation = "Called-up face value = ₹10 - ₹2 (uncalled) = ₹8 per share. Total Share Capital Dr. = 300 × ₹8 = ₹2,400. Securities premium was already received on application/allotment, so Section 52 prohibits debiting it. Amount paid on application/allotment towards face value = ₹5 per share (credited to Forfeiture = ₹1,500). Unpaid first call = ₹3 per share = ₹900.",
            markingRubricSteps = listOf(
                "1 Mark: Debit Share Capital with called-up value (300 × ₹8 = ₹2,400)",
                "1 Mark: Credit Share Forfeiture with amount received towards face value (₹1,500)",
                "1 Mark: Credit Calls in Arrears / First Call (₹900) & Narration"
            ),
            yearTag = "2025 Board Paper",
            boardTag = "CBSE"
        ),
        ExamQuestion(
            id = "pyq_cbse_2024_1",
            chapterId = "acc_ch3_admission",
            chapterName = "Admission of a Partner",
            questionNumber = 3,
            marks = 4,
            type = QuestionType.JOURNAL_WORKSPACE,
            questionText = "X and Y are partners sharing profits in the ratio of 3:2. They admit Z for 1/4th share in profits. Z brings ₹2,00,000 as capital and ₹40,000 for his share of goodwill. Half of the goodwill is withdrawn by old partners. Pass journal entries.",
            instructions = "Enter both the receipt of goodwill premium and the withdrawal entries in the Journal table.",
            modelAnswerJournal = listOf(
                JournalEntryRow(
                    date = "2024-03-20",
                    particulars = "Bank A/c Dr.\n  To Z's Capital A/c\n  To Premium for Goodwill A/c",
                    lf = "4",
                    debitAmount = "240000",
                    creditAmount = "240000",
                    narration = "Being capital and premium for goodwill received"
                ),
                JournalEntryRow(
                    date = "2024-03-20",
                    particulars = "Premium for Goodwill A/c Dr.\n  To X's Capital A/c (3/5)\n  To Y's Capital A/c (2/5)",
                    lf = "5",
                    debitAmount = "40000",
                    creditAmount = "40000",
                    narration = "Being premium distributed in sacrificing ratio 3:2"
                ),
                JournalEntryRow(
                    date = "2024-03-20",
                    particulars = "X's Capital A/c Dr. (₹24,000 / 2)\nY's Capital A/c Dr. (₹16,000 / 2)\n  To Bank A/c",
                    lf = "6",
                    debitAmount = "20000",
                    creditAmount = "20000",
                    narration = "Being half of premium withdrawn by old partners"
                )
            ),
            modelAnswerExplanation = "Sacrificing ratio is 3:2. Goodwill credited to X = ₹24,000 and Y = ₹16,000. Half withdrawn = X Dr ₹12,000, Y Dr ₹8,000, Bank Cr ₹20,000.",
            markingRubricSteps = listOf(
                "1.5 Marks: Bank Dr to Z Capital & Premium for Goodwill",
                "1.5 Marks: Distribution of Premium in sacrificing ratio 3:2",
                "1 Mark: Half withdrawal entry (X Dr 12,000, Y Dr 8,000 To Bank 20,000)"
            ),
            yearTag = "2024 Board Paper",
            boardTag = "CBSE"
        ),
        ExamQuestion(
            id = "pyq_cbse_2023_1",
            chapterId = "acc_ch9_cashflow",
            chapterName = "Cash Flow Statement",
            questionNumber = 4,
            marks = 6,
            type = QuestionType.NUMERICAL_INPUT,
            questionText = "From the following details, calculate Net Cash Flow from Operating Activities:\n- Net Profit after Tax: ₹2,50,000\n- Provision for Tax made during year: ₹60,000\n- Depreciation on Plant: ₹35,000\n- Loss on sale of Machinery: ₹10,000\n- Increase in Trade Receivables: ₹25,000\n- Decrease in Inventories: ₹15,000\n- Increase in Trade Payables: ₹18,000\n- Income Tax actually paid: ₹50,000",
            instructions = "Calculate the final Net Cash Flow from Operating Activities in Rupees (₹) and enter the exact numerical value.",
            expectedNumericalAnswer = 313000.0,
            modelAnswerExplanation = "1. Net Profit before Tax = ₹2,50,000 + ₹60,000 = ₹3,10,000.\n2. Operating Profit before Working Capital Changes = ₹3,10,000 + Depreciation (₹35,000) + Loss on sale (₹10,000) = ₹3,55,000.\n3. Working Capital Changes: - Increase in Debtors (₹25,000) + Decrease in Stock (₹15,000) + Increase in Creditors (₹18,000) = +₹8,000.\n4. Cash Generated from Operations = ₹3,63,000.\n5. Less Income Tax Paid = ₹3,63,000 - ₹50,000 = ₹3,13,000.",
            markingRubricSteps = listOf(
                "1 Mark: Net profit before tax calculation (₹3,10,000)",
                "2 Marks: Non-cash adjustments (+35,000 dep, +10,000 loss)",
                "2 Marks: Working capital changes (-25,000 + 15,000 + 18,000)",
                "1 Mark: Deduction of actual tax paid (₹50,000) -> Final ₹3,13,000"
            ),
            yearTag = "2023 Board Paper",
            boardTag = "CBSE"
        ),
        ExamQuestion(
            id = "pyq_cbse_2022_1",
            chapterId = "acc_ch8_ratios",
            chapterName = "Accounting Ratios",
            questionNumber = 5,
            marks = 3,
            type = QuestionType.THEORY_EXPLANATION,
            questionText = "Current Ratio of a company is 2:1. State with reasons whether each of the following transactions will increase, decrease or not change the ratio:\n(i) Repayment of a current liability of ₹20,000.\n(ii) Purchase of goods on credit of ₹15,000.\n(iii) Sale of goods costing ₹10,000 for ₹12,000 for cash.",
            instructions = "Explain the step-by-step mathematical impact on numerator (Current Assets) and denominator (Current Liabilities).",
            modelAnswerExplanation = "(i) Repayment of Current Liability: INCREASES the ratio. Because both CA and CL reduce by the same amount. When numerator is greater than denominator (2:1), equal reduction increases the ratio (e.g. 200 - 20 / 100 - 20 = 180/80 = 2.25:1).\n(ii) Purchase of goods on credit: DECREASES the ratio. Both CA (Stock) and CL (Creditors) increase by ₹15,000, pulling the ratio closer to 1:1.\n(iii) Sale of goods costing ₹10,000 for ₹12,000 cash: INCREASES the ratio. CA net increase = +₹12,000 cash - ₹10,000 stock = +₹2,000, while CL remains unchanged.",
            markingRubricSteps = listOf(
                "1 Mark: (i) Increase with valid mathematical reason",
                "1 Mark: (ii) Decrease with valid reason",
                "1 Mark: (iii) Increase with explanation of net asset gain"
            ),
            yearTag = "2022 Board Paper",
            boardTag = "CBSE"
        ),
        ExamQuestion(
            id = "pyq_isc_2024_1",
            chapterId = "acc_ch5_dissolution",
            chapterName = "Dissolution of Partnership",
            questionNumber = 6,
            marks = 3,
            type = QuestionType.JOURNAL_WORKSPACE,
            questionText = "Pass journal entries on dissolution for the following:\n(a) Realisation expenses ₹5,000 paid by partner Rohan on behalf of the firm.\n(b) Creditors of ₹40,000 accepted machinery of ₹35,000 in full settlement.",
            instructions = "Record entries for both transactions. Note: If no entry is required, explicitly write 'No Entry Required' with reason.",
            modelAnswerJournal = listOf(
                JournalEntryRow(
                    date = "2024-04-10",
                    particulars = "Realisation A/c Dr.\n  To Rohan's Capital A/c",
                    lf = "2",
                    debitAmount = "5000",
                    creditAmount = "5000",
                    narration = "Being realisation expenses paid by partner on firm's behalf"
                )
            ),
            modelAnswerExplanation = "(a) Realisation A/c Dr. ₹5,000 To Rohan's Capital A/c ₹5,000 (Firm's expense paid by partner credit partner capital).\n(b) NO ENTRY IS REQUIRED because when an asset is transferred to a creditor in full/part settlement of their debt, both accounts are already in Realisation A/c.",
            markingRubricSteps = listOf(
                "1.5 Marks: Correct entry for (a) Realisation Dr to Rohan Capital",
                "1.5 Marks: Stating 'No Entry' for (b) with proper justification"
            ),
            yearTag = "2024 Board Paper",
            boardTag = "ISC"
        )
    )

    fun getQuestionsForExamCategory(category: ExamCategory, board: EducationBoard): List<ExamQuestion> {
        val baseList = allPyqQuestions
        return when (category) {
            ExamCategory.DAILY_REVISION -> baseList.take(4) // 4 high-yield targeted questions
            ExamCategory.UNIT_TEST -> baseList.filter { it.chapterId == "acc_ch1_fundamentals" || it.chapterId == "acc_ch3_admission" || it.chapterId == "acc_ch6_shares" }.take(3)
            ExamCategory.HALF_YEARLY -> baseList.take(5)
            ExamCategory.FULL_BOARD_MOCK -> baseList // full set with all question styles
        }
    }
}
