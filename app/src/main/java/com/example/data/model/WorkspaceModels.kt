package com.example.data.model

import java.util.UUID

enum class AccountingWorkspaceType(val displayName: String, val description: String) {
    LEDGER("Ledger (T-Account)", "Individual accounts showing debit & credit balances with auto-balancing"),
    PARTNERS_CAPITAL("Partners' Capital Account", "Multi-column ledger for 2 or 3 partners (CBSE/ISC 6 & 8-mark pattern)"),
    REVALUATION("Revaluation Account", "Nominal account for asset & liability revaluation on Admission/Retirement"),
    REALISATION("Realisation Account", "Nominal account to close books & realise assets on Dissolution"),
    JOURNAL("Journal", "Record daily business transactions in chronological order (Dr/Cr)"),
    BALANCE_SHEET("Balance Sheet (Schedule III)", "Statement of financial position detailing Equities, Liabilities & Assets"),
    CASH_FLOW("Cash Flow Statement (AS-3)", "Classified operating, investing and financing cash flows")
}

data class JournalEntryRow(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val particulars: String = "",
    val lf: String = "",
    val debitAmount: String = "",
    val creditAmount: String = "",
    val narration: String = ""
)

data class LedgerEntryRow(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val particulars: String = "",
    val jf: String = "",
    val amount: String = "",
    // Partner multi-column amounts
    val partnerAAmount: String = "",
    val partnerBAmount: String = "",
    val partnerCAmount: String = ""
)

data class MultiPartnerLedgerAccount(
    val id: String = UUID.randomUUID().toString(),
    val accountName: String = "Partners' Capital A/c",
    val partnerNames: List<String> = listOf("Partner A", "Partner B"),
    val drRows: List<LedgerEntryRow> = listOf(
        LedgerEntryRow(date = "2025-03-31", particulars = "To Drawings A/c", partnerAAmount = "10000", partnerBAmount = "8000")
    ),
    val crRows: List<LedgerEntryRow> = listOf(
        LedgerEntryRow(date = "2024-04-01", particulars = "By Balance b/d", partnerAAmount = "150000", partnerBAmount = "100000"),
        LedgerEntryRow(date = "2025-03-31", particulars = "By General Reserve A/c", partnerAAmount = "18000", partnerBAmount = "12000"),
        LedgerEntryRow(date = "2025-03-31", particulars = "By Revaluation A/c (Profit)", partnerAAmount = "6000", partnerBAmount = "4000")
    )
)

data class SingleLedgerAccount(
    val id: String = UUID.randomUUID().toString(),
    val accountName: String = "Revaluation A/c",
    val accountType: String = "Nominal", // Real, Personal, Nominal, Asset, Liability, Capital
    val drRows: List<LedgerEntryRow> = listOf(
        LedgerEntryRow(date = "2025-04-01", particulars = "To Provision for Doubtful Debts A/c", jf = "1", amount = "4000"),
        LedgerEntryRow(date = "2025-04-01", particulars = "To Outstanding Expenses A/c", jf = "1", amount = "2500")
    ),
    val crRows: List<LedgerEntryRow> = listOf(
        LedgerEntryRow(date = "2025-04-01", particulars = "By Building A/c (Appreciation)", jf = "1", amount = "20000"),
        LedgerEntryRow(date = "2025-04-01", particulars = "By Accrued Income A/c", jf = "1", amount = "1500")
    )
)

data class LedgerBalancingResult(
    val totalDr: Double,
    val totalCr: Double,
    val balanceAmount: Double,
    val balanceType: String, // "Debit Balance", "Credit Balance", "Nil / Balanced"
    val balancingEntryParticulars: String, // "To Balance c/d" or "By Balance c/d"
    val broughtDownEntryParticulars: String, // "By Balance b/d" or "To Balance b/d"
    val ruleExplanation: String
)

data class PartnerBalancingResult(
    val partnerName: String,
    val totalDr: Double,
    val totalCr: Double,
    val balanceAmount: Double,
    val balanceType: String,
    val balancingEntry: String,
    val broughtDownEntry: String
)

data class BalanceSheetItem(
    val id: String = UUID.randomUUID().toString(),
    val particular: String = "",
    val noteNo: String = "",
    val amount: String = ""
)

data class CashFlowItem(
    val id: String = UUID.randomUUID().toString(),
    val activity: String = "Operating", // Operating, Investing, Financing
    val description: String = "",
    val amount: String = "",
    val isOutflow: Boolean = false
)

data class BoardLedgerScenario(
    val id: String,
    val title: String,
    val boardTag: String,
    val marks: String,
    val chapter: String,
    val description: String,
    val defaultAccountType: AccountingWorkspaceType,
    val suggestedAccountName: String,
    val sampleDrRows: List<LedgerEntryRow>,
    val sampleCrRows: List<LedgerEntryRow>,
    val partners: List<String> = emptyList(),
    val hint: String
)
