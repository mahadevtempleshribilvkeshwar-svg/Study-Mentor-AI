package com.example.data.repository

import com.example.data.model.AccountingConcept
import com.example.data.model.ChapterModule
import com.example.data.model.CommerceSubject
import com.example.data.model.EducationBoard
import com.example.data.model.ImportantPoint
import com.example.data.model.PriorityLevel

object CurriculumRepository {

    fun getCurriculumForBoard(board: EducationBoard, subject: CommerceSubject): List<ChapterModule> {
        if (subject != CommerceSubject.ACCOUNTANCY) {
            return getOtherSubjectModules(subject)
        }

        return listOf(
            ChapterModule(
                id = "acc_ch1_fundamentals",
                chapterNumber = 1,
                title = "Accounting for Partnership: Fundamentals",
                unitName = "Part A: Accounting for Partnership Firms",
                weightageMarks = "10 Marks",
                summary = "Partnership deed, Profit & Loss Appropriation A/c, Interest on Capital & Drawings, Past Adjustments, Guarantee of Profit.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c1_deed_rules",
                        title = "Rules Applicable in the Absence of Partnership Deed",
                        overview = "When there is no partnership deed or it is silent, specific provisions of the Indian Partnership Act, 1932 apply.",
                        keyRules = listOf(
                            "Profit Sharing: Equally among all partners irrespective of capital.",
                            "Interest on Capital: NOT allowed.",
                            "Interest on Drawings: NOT charged.",
                            "Salary / Remuneration / Commission: NOT allowed.",
                            "Interest on Partner's Loan: 6% per annum (Charge against profit)."
                        ),
                        practicalExample = "A and B contribute ₹5,00,000 and ₹1,00,000. In absence of deed, profit of ₹60,000 is shared ₹30,000 each.",
                        commonPitfall = "Do NOT provide 6% interest on Capital. 6% p.a. applies ONLY to Partner's Loan!",
                        goldenRuleExplanation = "Interest on Loan is debited to P&L A/c (Charge), while Interest on Capital is debited to P&L Appropriation A/c (Appropriation)."
                    ),
                    AccountingConcept(
                        id = "c1_pl_appropriation",
                        title = "Profit & Loss Appropriation Account",
                        overview = "A special nominal account prepared after P&L Account to show distribution of Net Profit among partners.",
                        keyRules = listOf(
                            "Credit Side: Net Profit b/d from P&L A/c, Interest on Drawings.",
                            "Debit Side: Interest on Capital, Partner's Salary, Partner's Commission, Transfer to General Reserve.",
                            "Balancing Figure: Divisible Profit (or Divisible Loss) distributed in Profit Sharing Ratio."
                        ),
                        formulaOrFormat = "Interest on Drawings = Total Drawings × Rate/100 × Average Period/12",
                        practicalExample = "Monthly drawings on 1st day = Avg period 6.5 months; Middle day = 6 months; Last day = 5.5 months.",
                        commonPitfall = "Remember: If date of drawings is not mentioned, calculate interest for an average period of 6 months.",
                        goldenRuleExplanation = "Nominal Account: Debit all expenses/losses (Partner remunerations), Credit all incomes/gains (Interest on drawings)."
                    ),
                    AccountingConcept(
                        id = "c1_past_adjustments",
                        title = "Past Adjustments (Omission / Error in Deed)",
                        overview = "Correcting accounting errors (like omitted interest on capital or wrong profit distribution) by passing a Single Adjusting Journal Entry.",
                        keyRules = listOf(
                            "Prepare Adjustment Table showing: Amount already credited (wrong) vs Amount which should have been credited (correct).",
                            "Net Effect: Identify Partner whose Capital has excess credit (Debit them) and Partner whose Capital is short (Credit them).",
                            "Adjusting Entry: Sacrificing/Gaining Partner Capital/Current A/c Dr. to Gaining/Sacrificing Partner Capital/Current A/c."
                        ),
                        practicalExample = "Interest on Capital @ 10% omitted. Calculate true I.O.C. and distribute total sum in profit sharing ratio to find Net Debit/Credit.",
                        commonPitfall = "If Capitals are FIXED, the adjusting entry must be passed through PARTNERS' CURRENT ACCOUNTS, not Capital accounts."
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip1_1",
                        title = "Provisions of Indian Partnership Act 1932 (No Deed)",
                        description = "Equal profits, No IOC, No IOD, No Salary, 6% p.a. on Loan.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Equal Ratio | 6% p.a. on Loan | Zero IOC/IOD",
                        boardExamTip = "Appears virtually every year in 1-mark MCQs and 3-mark case questions."
                    ),
                    ImportantPoint(
                        id = "ip1_2",
                        title = "Interest on Drawings Formulas & Average Periods",
                        description = "Beginning: 6.5m (monthly), 7.5m (quarterly). Middle: 6m. End: 5.5m (monthly), 4.5m (quarterly).",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Avg Period = (Time left after 1st drawing + Time left after last drawing) / 2",
                        commonMistakeTrap = "Don't forget '% p.a.' vs '% flat'. If p.a. is missing, time factor is ignored."
                    ),
                    ImportantPoint(
                        id = "ip1_3",
                        title = "Guarantee of Minimum Profit",
                        description = "Deficiency in guaranteed profit borne by guaranteeing partner(s) in specified ratio.",
                        priority = PriorityLevel.IMPORTANT,
                        ruleOrFormula = "Deficiency = Guaranteed Amount - Actual Share",
                        boardExamTip = "Deduct deficiency from guaranteeing partner's share in P&L Appropriation A/c."
                    ),
                    ImportantPoint(
                        id = "ip1_4",
                        title = "Manager's Commission vs Partner's Commission",
                        description = "Manager's commission is a charge (P&L A/c); Partner's commission is an appropriation (P&L Appropriation A/c).",
                        priority = PriorityLevel.MUST_REVISE,
                        ruleOrFormula = "Charge against profit vs Appropriation of profit"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch2_goodwill",
                chapterNumber = 2,
                title = "Goodwill: Nature and Valuation",
                unitName = "Part A: Accounting for Partnership Firms",
                weightageMarks = "4-6 Marks",
                summary = "Methods of Valuation: Average Profit, Super Profit, and Capitalisation Method with all adjustments.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c2_methods",
                        title = "Three Core Methods of Goodwill Valuation",
                        overview = "Goodwill represents the present value of anticipated future super-normal earnings.",
                        keyRules = listOf(
                            "1. Average Profit Method: Adjusted Normal Average Profit × Number of Years' Purchase.",
                            "2. Super Profit Method: Super Profit × Number of Years' Purchase (where Super Profit = Actual Normal Profit - Normal Profit).",
                            "3. Capitalisation Method: (a) Capitalisation of Average Profit = (Average Profit / NRR × 100) - Capital Employed. (b) Capitalisation of Super Profit = Super Profit / NRR × 100."
                        ),
                        formulaOrFormat = "Normal Profit = Capital Employed × (Normal Rate of Return / 100)",
                        practicalExample = "Average Profit = ₹80,000, Capital Employed = ₹5,00,000, NRR = 10%. Normal Profit = ₹50,000. Super Profit = ₹30,000. Goodwill @ 3 years' purchase = ₹90,000.",
                        commonPitfall = "Always deduct abnormal gains (e.g., gain on sale of machinery) and add back abnormal losses (e.g., loss by fire) to calculate Normal Operating Profit."
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip2_1",
                        title = "Adjustments in Past Profits for Goodwill",
                        description = "Deduct: Abnormal profits, Non-operating incomes, Overvaluation of closing stock. Add: Abnormal losses, Non-recurring expenses, Undervaluation of closing stock.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Normal Profit = Net Profit - Abnormal Gain + Abnormal Loss",
                        boardExamTip = "Watch out for closing stock adjustments: closing stock overvaluation reduces current year normal profit but increases next year normal profit!"
                    ),
                    ImportantPoint(
                        id = "ip2_2",
                        title = "Super Profit & Capitalisation Equivalence",
                        description = "Capitalisation of Super Profit gives the exact same result as Capitalisation of Average Profit.",
                        priority = PriorityLevel.IMPORTANT,
                        ruleOrFormula = "Goodwill = (Super Profit / NRR) × 100"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch3_admission",
                chapterNumber = 3,
                title = "Admission of a Partner",
                unitName = "Part A: Accounting for Partnership Firms",
                weightageMarks = "8-10 Marks",
                summary = "Sacrificing Ratio, Premium for Goodwill (AS-26), Revaluation Account, Accumulated Reserves, Capital Adjustment.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c3_revaluation",
                        title = "Revaluation Account (Profit and Loss Adjustment A/c)",
                        overview = "Nominal account prepared to record changes in asset values and liability amounts on reconstitution.",
                        keyRules = listOf(
                            "Debit Side: Decrease in value of Assets, Increase in value of Liabilities, Unrecorded Liabilities.",
                            "Credit Side: Increase in value of Assets, Decrease in value of Liabilities, Unrecorded Assets.",
                            "Profit or Loss on Revaluation: Distributed among OLD PARTNERS in OLD PROFIT SHARING RATIO only."
                        ),
                        practicalExample = "Plant appreciated by ₹20,000 -> Credit Revaluation A/c. Bad debts provision created @ 5% -> Debit Revaluation A/c.",
                        commonPitfall = "Never distribute Revaluation Profit to the Incoming New Partner! It belongs strictly to Old Partners in Old PSR."
                    ),
                    AccountingConcept(
                        id = "c3_goodwill_as26",
                        title = "Treatment of Goodwill as per AS-26",
                        overview = "Only purchased goodwill can be recorded in books. Internally generated goodwill is adjusted through Partner Capital accounts.",
                        keyRules = listOf(
                            "New Partner brings Premium in Cash: Bank A/c Dr. To Premium for Goodwill A/c.",
                            "Distribute Premium: Premium for Goodwill A/c Dr. To Sacrificing Partners' Capital A/cs (in Sacrificing Ratio).",
                            "If Premium NOT brought in cash: Incoming Partner Current A/c Dr. To Sacrificing Partners' Capital A/cs."
                        ),
                        formulaOrFormat = "Sacrificing Ratio = Old Share - New Share"
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip3_1",
                        title = "Workmen Compensation Reserve & Investment Fluctuation Reserve",
                        description = "Excess WCR over actual claim is distributed to Old Partners in Old Ratio. IFR covers fall in market value below book value.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Unclaimed WCR -> Credit Old Partners' Capital A/c (Old Ratio)",
                        boardExamTip = "Common board question: If claim is ₹30,000 and WCR is ₹50,000, remaining ₹20,000 is distributed among old partners."
                    ),
                    ImportantPoint(
                        id = "ip3_2",
                        title = "Adjustment of Capital based on New Partner's Capital",
                        description = "Total Firm Capital = New Partner Capital × Reciprocal of New Partner Share. Find new capital balances and adjust via Cash/Current A/c.",
                        priority = PriorityLevel.IMPORTANT,
                        ruleOrFormula = "Total Capital = New Partner Capital × (1 / Share)"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch4_retirement_death",
                chapterNumber = 4,
                title = "Retirement & Death of a Partner",
                unitName = "Part A: Accounting for Partnership Firms",
                weightageMarks = "6-8 Marks",
                summary = "Gaining Ratio, Share of Goodwill, Deceased Partner's Share of Profit up to Date of Death, Retiring Partner Loan A/c.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c4_profit_till_death",
                        title = "Calculation of Deceased Partner's Profit Share till Death",
                        overview = "Calculated either on Time Basis (Previous year's profit / Average profit) or on Turnover/Sales Basis.",
                        keyRules = listOf(
                            "Time Basis: Profit = Prev Profit × (Period in months/12) × Deceased Partner's Share.",
                            "Sales Basis: Profit = (Prev Profit / Prev Sales) × Sales till death × Deceased Partner's Share.",
                            "Journal Entry (if PSR of remaining partners remains same): Profit & Loss Suspense A/c Dr. To Deceased Partner's Capital A/c.",
                            "Journal Entry (if PSR of remaining partners changes): Gaining Partners' Capital A/cs Dr. To Deceased Partner's Capital A/c."
                        ),
                        commonPitfall = "Do not forget the fraction of the year (e.g. 3 months / 12 months) when using time basis!"
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip4_1",
                        title = "Gaining Ratio vs Sacrificing Ratio",
                        description = "Gaining Ratio = New Share - Old Share. Used to compensate retiring/deceased partner for their share of goodwill.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Gaining Ratio = New Ratio - Old Ratio"
                    ),
                    ImportantPoint(
                        id = "ip4_2",
                        title = "Settlement of Retiring Partner's Dues",
                        description = "Amount transferred to Retiring Partner's Loan A/c (bearing 6% interest unless agreed otherwise).",
                        priority = PriorityLevel.IMPORTANT,
                        ruleOrFormula = "Retiring Partner Capital A/c Dr. To Retiring Partner Loan A/c"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch5_dissolution",
                chapterNumber = 5,
                title = "Dissolution of a Partnership Firm",
                unitName = "Part A: Accounting for Partnership Firms",
                weightageMarks = "6-8 Marks",
                summary = "Realisation Account, Treatment of Partner Loan, Settlement of Accounts (Section 48), Realisation Expenses.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c5_realisation",
                        title = "Realisation Account Preparation & Rules",
                        overview = "Nominal account prepared upon dissolution to close all asset and liability accounts and determine gain/loss on liquidation.",
                        keyRules = listOf(
                            "Step 1: Transfer all Assets (except Cash/Bank, P&L Dr. balance) to Debit of Realisation A/c at Gross Book Value.",
                            "Step 2: Transfer all External Third-Party Liabilities & Provisions to Credit of Realisation A/c.",
                            "Step 3: Record Asset Realisations on Credit side (Bank A/c Dr. To Realisation A/c).",
                            "Step 4: Record Liability Payments on Debit side (Realisation A/c Dr. To Bank A/c).",
                            "Step 5: If a liability is paid off by taking an asset -> NO ENTRY IS PASSED in books!"
                        ),
                        practicalExample = "Creditors of ₹50,000 accepted stock of ₹45,000 in full settlement -> No journal entry required.",
                        commonPitfall = "Partner's Loan is an internal liability. It is NEVER transferred to Realisation A/c! Paid through separate Partner's Loan A/c."
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip5_1",
                        title = "Realisation Expenses - 4 Golden Cases",
                        description = "Case 1: Borne & paid by firm (Realisation Dr to Bank). Case 2: Borne by partner, paid by firm (Partner Cap Dr to Bank). Case 3: Borne & paid by partner (No Entry). Case 4: Remuneration given to partner (Realisation Dr to Partner Cap).",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Who bears the expense? Dr that entity. Who pays? Cr that entity.",
                        boardExamTip = "One of the most frequent 3-mark journal entry questions in Board examinations."
                    ),
                    ImportantPoint(
                        id = "ip5_2",
                        title = "Order of Settlement of Debts (Section 48)",
                        description = "1. First pay third-party debts -> 2. Partner's loans rateably -> 3. Partner's capital -> 4. Surplus divided in PSR.",
                        priority = PriorityLevel.MUST_REVISE,
                        ruleOrFormula = "Outside Debts -> Partner Loans -> Partner Capitals -> Residue"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch6_shares",
                chapterNumber = 6,
                title = "Accounting for Share Capital",
                unitName = "Part B: Company Accounts",
                weightageMarks = "12-16 Marks",
                summary = "Issue of Shares at Par/Premium, Pro-rata Allotment, Calls in Arrears/Advance, Forfeiture & Reissue, Capital Reserve.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c6_prorata",
                        title = "Pro-rata Allotment & Advance Calculation",
                        overview = "Proportionate allotment when shares are oversubscribed. Surplus application money adjusted towards allotment and calls.",
                        keyRules = listOf(
                            "Category table: Shares Applied, Shares Allotted, App Money Received, App Money Transferred to Share Capital, Surplus adjusted to Allotment, Refund.",
                            "To find shares allotted to a defaulter: Shares Allotted = (Total Allotted / Total Applied) × Shares Applied.",
                            "Net unpaid on allotment = Due on Allotment - Excess Application Money available."
                        ),
                        formulaOrFormat = "Capital Reserve = (Amount Forfeited on Reissued Shares) - (Discount allowed on Reissue)"
                    ),
                    AccountingConcept(
                        id = "c6_forfeiture",
                        title = "Forfeiture & Reissue of Shares",
                        overview = "Cancelling share membership for non-payment of calls and subsequent reissue.",
                        keyRules = listOf(
                            "Forfeiture Entry: Share Capital A/c Dr. (Called-up Face Value) [Securities Premium Dr. if NOT received] To Share Forfeiture A/c (Amount actually received towards face value) To Calls in Arrears A/c (Unpaid amount).",
                            "Reissue Entry: Bank A/c Dr. (Reissue price) [Share Forfeiture A/c Dr. for discount] To Share Capital A/c (Paid-up value).",
                            "Transfer to Capital Reserve: Share Forfeiture A/c Dr. To Capital Reserve A/c."
                        ),
                        practicalExample = "Forfeited 100 shares of ₹10 (₹8 called up) for non-payment of ₹3 first call. Share Capital Dr ₹800, Share Forfeiture Cr ₹500, Calls in Arrears Cr ₹300.",
                        commonPitfall = "Never debit Securities Premium if it has already been received before forfeiture (Sec. 52 restrictions)!"
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip6_1",
                        title = "Called-Up Value vs Paid-Up Value for Forfeiture",
                        description = "Always debit Share Capital with the CALLED-UP amount (face value called so far), NOT the total nominal value unless fully called.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Share Capital Dr. = Number of Forfeited Shares × Called-Up Face Value",
                        boardExamTip = "Always check whether the problem says '₹8 called up' or 'fully called up' before writing the debit line."
                    ),
                    ImportantPoint(
                        id = "ip6_2",
                        title = "Maximum Permissible Discount on Reissue",
                        description = "The discount allowed on reissue of forfeited shares cannot exceed the amount originally credited to Share Forfeiture on those specific shares.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Max Discount on Reissue ≤ Forfeited Amount per share"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch7_debentures",
                chapterNumber = 7,
                title = "Issue and Redemption of Debentures",
                unitName = "Part B: Company Accounts",
                weightageMarks = "6-8 Marks",
                summary = "Issue for cash at Par/Premium/Discount, Issue with terms of redemption, Loss on issue of debentures write-off.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c7_redemption_terms",
                        title = "6 Cases of Issue of Debentures from Redemption Standpoint",
                        overview = "When debentures are redeemable at a premium, the premium is a future liability and a loss at the time of issue.",
                        keyRules = listOf(
                            "Case 1: Issued at Par, Redeemable at Par.",
                            "Case 2: Issued at Discount, Redeemable at Par (Discount on Issue Dr).",
                            "Case 3: Issued at Premium, Redeemable at Par (Securities Premium Cr).",
                            "Case 4: Issued at Par, Redeemable at Premium (Loss on Issue Dr, Premium on Redemption Cr).",
                            "Case 5: Issued at Discount, Redeemable at Premium (Loss on Issue Dr [Discount + Prem], Premium on Redemption Cr).",
                            "Case 6: Issued at Premium, Redeemable at Premium (Securities Premium Cr, Loss on Issue Dr, Premium on Redemption Cr)."
                        )
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip7_1",
                        title = "Writing off Discount / Loss on Issue of Debentures",
                        description = "Must be written off in the year of issue itself: 1st from Securities Premium A/c, then Statement of Profit and Loss.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Securities Premium A/c Dr -> Statement of P&L Dr -> To Loss on Issue of Debentures A/c"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch8_ratios",
                chapterNumber = 8,
                title = "Accounting Ratios",
                unitName = "Part C: Financial Statement Analysis",
                weightageMarks = "8-10 Marks",
                summary = "Liquidity Ratios, Solvency Ratios, Activity/Turnover Ratios, Profitability Ratios.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c8_ratio_formulas",
                        title = "Core Accounting Ratio Formulas",
                        overview = "Mathematical metrics evaluating liquidity, solvency, operational efficiency, and profitability of a corporate entity.",
                        keyRules = listOf(
                            "Current Ratio = Current Assets / Current Liabilities (Ideal 2:1)",
                            "Quick / Acid Test Ratio = Quick Assets / Current Liabilities (Ideal 1:1) [Quick Assets = Current Assets - Inventory - Prepaid Expenses]",
                            "Debt to Equity Ratio = Long-term Debts / Shareholders' Funds (Ideal 2:1)",
                            "Inventory Turnover Ratio = Cost of Revenue from Operations (COGS) / Average Inventory (Expressed in Times)",
                            "Trade Receivables Turnover = Net Credit Revenue / Average Trade Receivables",
                            "Gross Profit Ratio = (Gross Profit / Net Revenue from Operations) × 100",
                            "Return on Investment (ROI) = (Net Profit before Interest and Tax / Capital Employed) × 100"
                        ),
                        commonPitfall = "Remember: Loose tools and stores & spares are excluded from Current Assets while calculating Current Ratio and Quick Ratio!"
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip8_1",
                        title = "Impact of Transactions on Ratios (Increase / Decrease / No Change)",
                        description = "For Current Ratio > 1 (e.g. 2:1), paying a current liability increases the ratio! For ratio < 1 (e.g. 0.8:1), paying a liability decreases the ratio.",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Analyze numerator & denominator changes mathematically",
                        boardExamTip = "Guaranteed 3-4 mark question in Board exam. Always test with real numbers (e.g. 200/100 -> paying 20 gives 180/80 = 2.25:1 -> Increases)."
                    ),
                    ImportantPoint(
                        id = "ip8_2",
                        title = "Capital Employed Computation (Both Approaches)",
                        description = "Liabilities Approach: Shareholders' Funds + Non-Current Liabilities. Assets Approach: Non-Current Assets + Working Capital (CA - CL).",
                        priority = PriorityLevel.IMPORTANT,
                        ruleOrFormula = "Capital Employed = Equity + Debt = Net Total Assets"
                    )
                )
            ),
            ChapterModule(
                id = "acc_ch9_cashflow",
                chapterNumber = 9,
                title = "Cash Flow Statement (AS-3 Revised)",
                unitName = "Part C: Financial Statement Analysis",
                weightageMarks = "8-10 Marks",
                summary = "Operating Activities (Indirect Method), Investing Activities, Financing Activities, Cash & Cash Equivalents.",
                concepts = listOf(
                    AccountingConcept(
                        id = "c9_operating",
                        title = "Cash Flow from Operating Activities (Indirect Method)",
                        overview = "Conversion of accrual net profit before tax into cash generated from core business operations.",
                        keyRules = listOf(
                            "Step 1: Net Profit before Tax and Extraordinary Items (Net Profit + Proposed Dividend + Interim Dividend + Transfer to Reserve + Provision for Tax - Tax Refund).",
                            "Step 2: Add Non-Cash / Non-Operating Expenses (Depreciation, Amortisation, Loss on sale of fixed assets, Interest on borrowings).",
                            "Step 3: Less Non-Operating Incomes (Gain on sale of assets, Dividend received, Interest received, Rental income).",
                            "Step 4: Working Capital Adjustments (+ Decrease in CA, + Increase in CL, - Increase in CA, - Decrease in CL).",
                            "Step 5: Less Income Tax Paid (Net of refund)."
                        ),
                        commonPitfall = "Provision for Tax: If given in adjustments, prepare Provision for Tax A/c. Debit side has 'Tax Paid' (deducted in operating), Credit side has 'Current year provision' (added to Net Profit)."
                    ),
                    AccountingConcept(
                        id = "c9_investing_financing",
                        title = "Investing & Financing Activities",
                        overview = "Investing: Fixed assets and investments. Financing: Equity, preference shares, debentures, loans, dividend paid.",
                        keyRules = listOf(
                            "Investing Inflows: Sale of Fixed Assets, Sale of Non-Current Investments, Dividend/Interest Received.",
                            "Investing Outflows: Purchase of Fixed Assets, Purchase of Investments.",
                            "Financing Inflows: Proceeds from issue of shares/debentures, Long-term loans raised.",
                            "Financing Outflows: Redemption of debentures/preference shares, Repayment of loans, Interest paid, Dividend paid (both interim & final)."
                        ),
                        practicalExample = "Dividend paid is ALWAYS a Financing Activity for all companies.",
                        commonPitfall = "Bank Overdraft and Cash Credit are part of FINANCING activities (short-term borrowings), NOT cash & cash equivalents!"
                    )
                ),
                importantPoints = listOf(
                    ImportantPoint(
                        id = "ip9_1",
                        title = "Fixed Asset & Accumulated Depreciation Account Preparation",
                        description = "When Accumulated Depreciation A/c is maintained, Asset A/c is at Cost. Balance asset a/c to find Purchase (Debit) or Sale (Credit).",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Cost - Acc Dep = Book Value vs Sale Price -> Profit/Loss",
                        boardExamTip = "Prepare working note T-accounts clearly. The Board allocates 2 marks specifically for working note accounts."
                    ),
                    ImportantPoint(
                        id = "ip9_2",
                        title = "Proposed Dividend Treatment as per AS-4",
                        description = "Proposed Dividend of PREVIOUS YEAR is added in Operating (NP before tax) and deducted in Financing. Current year proposed dividend is ignored (contingent liability).",
                        priority = PriorityLevel.VERY_IMPORTANT,
                        ruleOrFormula = "Previous Year Proposed Dividend: +Operating, -Financing",
                        boardExamTip = "Critical trap! Current year proposed dividend is never paid in the current year."
                    )
                )
            )
        )
    }

    private fun getOtherSubjectModules(subject: CommerceSubject): List<ChapterModule> {
        return when (subject) {
            CommerceSubject.BUSINESS_STUDIES -> listOf(
                ChapterModule(
                    id = "bst_ch1_principles",
                    chapterNumber = 1,
                    title = "Nature and Significance of Management",
                    unitName = "Principles & Functions of Management",
                    weightageMarks = "7 Marks",
                    summary = "Concept, Objectives, Importance, Management as Science/Art/Profession, Levels of Management, Coordination.",
                    concepts = emptyList(),
                    importantPoints = listOf(
                        ImportantPoint("bst_ip1", "Coordination: The Essence of Management", "Binds all other functions together.", PriorityLevel.VERY_IMPORTANT)
                    )
                ),
                ChapterModule(
                    id = "bst_ch2_financial_mgmt",
                    chapterNumber = 2,
                    title = "Financial Management & Capital Structure",
                    unitName = "Business Finance and Marketing",
                    weightageMarks = "12 Marks",
                    summary = "Investment, Financing and Dividend decisions. Factors affecting working capital and capital structure (EBIT-EPS analysis).",
                    concepts = emptyList(),
                    importantPoints = listOf(
                        ImportantPoint("bst_ip2", "Trading on Equity / Financial Leverage", "Using fixed debt to increase EPS for equity shareholders.", PriorityLevel.VERY_IMPORTANT)
                    )
                )
            )
            CommerceSubject.ECONOMICS -> listOf(
                ChapterModule(
                    id = "eco_ch1_national_income",
                    chapterNumber = 1,
                    title = "National Income and Related Aggregates",
                    unitName = "Macroeconomics",
                    weightageMarks = "10 Marks",
                    summary = "Circular flow of income, Value Added Method, Income Method, Expenditure Method, Real vs Nominal GDP.",
                    concepts = emptyList(),
                    importantPoints = listOf(
                        ImportantPoint("eco_ip1", "Precautions in Measuring National Income", "Exclude transfer payments, windfall gains, sale of second-hand goods.", PriorityLevel.VERY_IMPORTANT)
                    )
                ),
                ChapterModule(
                    id = "eco_ch2_money_banking",
                    chapterNumber = 2,
                    title = "Money and Banking & Central Bank Credit Control",
                    unitName = "Macroeconomics",
                    weightageMarks = "6 Marks",
                    summary = "Money supply (M1), Money Multiplier, Quantitative & Qualitative tools of RBI (Repo rate, CRR, SLR, Open Market Operations).",
                    concepts = emptyList(),
                    importantPoints = listOf(
                        ImportantPoint("eco_ip2", "Money Multiplier Formula", "Multiplier = 1 / LRR (Legal Reserve Ratio).", PriorityLevel.VERY_IMPORTANT)
                    )
                )
            )
            else -> emptyList()
        }
    }
}
