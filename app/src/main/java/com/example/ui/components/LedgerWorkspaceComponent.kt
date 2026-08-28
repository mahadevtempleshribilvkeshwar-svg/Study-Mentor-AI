package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoardLedgerScenario
import com.example.data.model.LedgerBalancingResult
import com.example.data.model.LedgerEntryRow
import com.example.data.model.MultiPartnerLedgerAccount
import com.example.data.model.PartnerBalancingResult
import com.example.data.model.SingleLedgerAccount
import com.example.ui.theme.AmberPastelBg
import com.example.ui.theme.AmberText
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldPastelBg
import com.example.ui.theme.EmeraldText
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.LedgerGridLine
import com.example.ui.theme.LedgerHeaderBg
import com.example.ui.theme.LedgerPaper
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RosePastelBg
import com.example.ui.theme.RoseText
import com.example.ui.theme.SkyPastelBg
import com.example.ui.theme.SkyText
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.SlateCanvas
import com.example.ui.theme.TextPrimary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CoreLedgerWorkspace(
    singleAccount: SingleLedgerAccount,
    onSingleAccountChange: (SingleLedgerAccount) -> Unit,
    multiPartnerAccount: MultiPartnerLedgerAccount,
    onMultiPartnerAccountChange: (MultiPartnerLedgerAccount) -> Unit,
    onAskAiAboutLedger: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMultiPartnerMode by remember { mutableStateOf(false) }
    var showCreateAccountDialog by remember { mutableStateOf(false) }
    var showJournalPostDialog by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }
    var showScenariosDialog by remember { mutableStateOf(false) }

    // Built-in presets
    val presetAccounts = remember {
        listOf(
            "Revaluation A/c" to false,
            "Partners' Capital A/c" to true,
            "Realisation A/c" to false,
            "Cash & Bank A/c" to false,
            "P&L Appropriation A/c" to false,
            "Share Capital A/c" to false,
            "Machinery A/c" to false
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Toolbar: Account Tabs, New Account, Mode Toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CORE LEDGER WORKSPACE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = IndigoPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Create, Post & Auto-Balance Class 12 T-Accounts",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }

                    // Mode switch badge: Single vs Multi-Partner (A, B, C)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Slate100)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (!isMultiPartnerMode) IndigoPrimary else Color.Transparent)
                                .clickable { isMultiPartnerMode = false }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("toggle_single_ledger"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Single A/c",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isMultiPartnerMode) Color.White else Slate600
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isMultiPartnerMode) IndigoPrimary else Color.Transparent)
                                .clickable { isMultiPartnerMode = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("toggle_partner_ledger"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Partners' Multi-Col",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMultiPartnerMode) Color.White else Slate600
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Preset Account Quick Selector Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetAccounts.forEach { (name, isPartner) ->
                        val isCurrentSelected = if (isPartner) {
                            isMultiPartnerMode && multiPartnerAccount.accountName == name
                        } else {
                            !isMultiPartnerMode && singleAccount.accountName == name
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isCurrentSelected) IndigoContainer else Slate100)
                                .border(
                                    1.dp,
                                    if (isCurrentSelected) IndigoBorder else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    if (isPartner) {
                                        isMultiPartnerMode = true
                                        onMultiPartnerAccountChange(
                                            multiPartnerAccount.copy(accountName = name)
                                        )
                                    } else {
                                        isMultiPartnerMode = false
                                        onSingleAccountChange(
                                            singleAccount.copy(accountName = name)
                                        )
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrentSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrentSelected) IndigoPrimary else Slate600
                            )
                        }
                    }

                    // + Create Custom Account button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(IndigoPrimary.copy(alpha = 0.08f))
                            .clickable { showCreateAccountDialog = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New A/c", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                        }
                    }
                }
            }
        }

        // Action Tools Row: Quick Post from Journal, Board Scenarios, Accounting Rules, Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showJournalPostDialog = true },
                modifier = Modifier.weight(1f).height(38.dp).testTag("btn_post_from_journal"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Icon(Icons.Default.PostAdd, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Post Journal", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = { showScenariosDialog = true },
                modifier = Modifier.weight(1f).height(38.dp).testTag("btn_board_scenarios"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = AmberText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CBSE Scenarios", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = { showRulesDialog = true },
                modifier = Modifier.weight(1f).height(38.dp).testTag("btn_accounting_rules"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = SkyText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Dr/Cr Rules", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }
        }

        // Active Ledger Content
        if (!isMultiPartnerMode) {
            SingleAccountLedgerView(
                account = singleAccount,
                onAccountChange = onSingleAccountChange,
                onAskAi = onAskAiAboutLedger
            )
        } else {
            MultiPartnerLedgerView(
                account = multiPartnerAccount,
                onAccountChange = onMultiPartnerAccountChange,
                onAskAi = onAskAiAboutLedger
            )
        }
    }

    // Dialog 1: Create New Custom Ledger Account
    if (showCreateAccountDialog) {
        var newAccName by remember { mutableStateOf("") }
        var selectedNature by remember { mutableStateOf("Personal (Asset/Liability)") }
        var partnerNamesCsv by remember { mutableStateOf("Partner A, Partner B") }
        var isPartnerAccount by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showCreateAccountDialog = false },
            title = {
                Text("Create New Ledger Account", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Specify account details for your Class 12 practice problem:", fontSize = 12.sp, color = Slate600)

                    OutlinedTextField(
                        value = newAccName,
                        onValueChange = { newAccName = it },
                        label = { Text("Account Name") },
                        placeholder = { Text("e.g. 10% Debentures A/c or X's Loan A/c") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Multi-Partner Column?", fontSize = 12.sp, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPartnerAccount) IndigoPrimary else Slate100)
                                .clickable { isPartnerAccount = !isPartnerAccount }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(if (isPartnerAccount) "YES" else "NO", color = if (isPartnerAccount) Color.White else Slate600, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isPartnerAccount) {
                        OutlinedTextField(
                            value = partnerNamesCsv,
                            onValueChange = { partnerNamesCsv = it },
                            label = { Text("Partner Names (comma separated)") },
                            placeholder = { Text("A, B, C") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAccName.isNotBlank()) {
                            if (isPartnerAccount) {
                                val pList = partnerNamesCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                onMultiPartnerAccountChange(
                                    MultiPartnerLedgerAccount(
                                        accountName = newAccName,
                                        partnerNames = pList.ifEmpty { listOf("Partner A", "Partner B") },
                                        drRows = listOf(LedgerEntryRow(particulars = "To Particulars")),
                                        crRows = listOf(LedgerEntryRow(particulars = "By Particulars"))
                                    )
                                )
                                isMultiPartnerMode = true
                            } else {
                                onSingleAccountChange(
                                    SingleLedgerAccount(
                                        accountName = newAccName,
                                        accountType = selectedNature,
                                        drRows = listOf(LedgerEntryRow(particulars = "To Particulars")),
                                        crRows = listOf(LedgerEntryRow(particulars = "By Particulars"))
                                    )
                                )
                                isMultiPartnerMode = false
                            }
                            showCreateAccountDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Create Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateAccountDialog = false }) {
                    Text("Cancel", color = Slate500)
                }
            }
        )
    }

    // Dialog 2: Post from Journal
    if (showJournalPostDialog) {
        var drAccount by remember { mutableStateOf("Cash A/c") }
        var crAccount by remember { mutableStateOf("Capital A/c") }
        var postAmount by remember { mutableStateOf("50000") }
        var postDate by remember { mutableStateOf("2025-04-01") }

        AlertDialog(
            onDismissRequest = { showJournalPostDialog = false },
            title = {
                Text("Post Journal Entry to Ledger", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Auto-generate posting into the active account following Class 12 double-entry conventions:", fontSize = 12.sp, color = Slate600)

                    OutlinedTextField(
                        value = postDate,
                        onValueChange = { postDate = it },
                        label = { Text("Date") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = drAccount,
                        onValueChange = { drAccount = it },
                        label = { Text("Debit Account (Dr.)") },
                        placeholder = { Text("e.g. Building A/c") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = crAccount,
                        onValueChange = { crAccount = it },
                        label = { Text("Credit Account (Cr.)") },
                        placeholder = { Text("e.g. Bank A/c") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = postAmount,
                        onValueChange = { postAmount = it },
                        label = { Text("Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postAmount.isNotBlank()) {
                            if (!isMultiPartnerMode) {
                                // If active account matches drAccount, add to debit side with "To crAccount"
                                val currentAcc = singleAccount.accountName.lowercase()
                                if (currentAcc.contains(drAccount.lowercase().replace("a/c", "").trim())) {
                                    val newDr = singleAccount.drRows + LedgerEntryRow(
                                        date = postDate,
                                        particulars = "To $crAccount",
                                        amount = postAmount
                                    )
                                    onSingleAccountChange(singleAccount.copy(drRows = newDr))
                                } else if (currentAcc.contains(crAccount.lowercase().replace("a/c", "").trim())) {
                                    val newCr = singleAccount.crRows + LedgerEntryRow(
                                        date = postDate,
                                        particulars = "By $drAccount",
                                        amount = postAmount
                                    )
                                    onSingleAccountChange(singleAccount.copy(crRows = newCr))
                                } else {
                                    // Default add as debit to current account
                                    val newDr = singleAccount.drRows + LedgerEntryRow(
                                        date = postDate,
                                        particulars = "To $crAccount",
                                        amount = postAmount
                                    )
                                    onSingleAccountChange(singleAccount.copy(drRows = newDr))
                                }
                            } else {
                                val newDr = multiPartnerAccount.drRows + LedgerEntryRow(
                                    date = postDate,
                                    particulars = "To $crAccount",
                                    partnerAAmount = postAmount
                                )
                                onMultiPartnerAccountChange(multiPartnerAccount.copy(drRows = newDr))
                            }
                            showJournalPostDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Post to Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJournalPostDialog = false }) {
                    Text("Cancel", color = Slate500)
                }
            }
        )
    }

    // Dialog 3: Accounting Rules Reference
    if (showRulesDialog) {
        AlertDialog(
            onDismissRequest = { showRulesDialog = false },
            title = {
                Text("Class 12 Debit & Credit Posting Rules", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("👑 Golden Rules of Accounting:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IndigoPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Real A/c (Assets): Debit what comes in, Credit what goes out", fontSize = 11.sp, color = Slate700)
                            Text("• Personal A/c (Debtors/Creditors/Capital): Debit the receiver, Credit the giver", fontSize = 11.sp, color = Slate700)
                            Text("• Nominal A/c (Expenses/Revaluation): Debit all expenses & losses, Credit all incomes & gains", fontSize = 11.sp, color = Slate700)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("📐 Modern Classification Rules:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IndigoPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Assets / Expenses: Increase = Debit (Dr.) | Decrease = Credit (Cr.)", fontSize = 11.sp, color = Slate700)
                            Text("• Liabilities / Capital / Revenue: Increase = Credit (Cr.) | Decrease = Debit (Dr.)", fontSize = 11.sp, color = Slate700)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = EmeraldPastelBg),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("🎯 Balancing Figure (c/d vs b/d) Logic:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldText)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• If Total Dr. > Total Cr. → Difference written as 'By Balance c/d' on Credit side. It is a DEBIT BALANCE carried down as 'To Balance b/d'.", fontSize = 11.sp, color = Slate700)
                            Text("• If Total Cr. > Total Dr. → Difference written as 'To Balance c/d' on Debit side. It is a CREDIT BALANCE carried down as 'By Balance b/d'.", fontSize = 11.sp, color = Slate700)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showRulesDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Got It")
                }
            }
        )
    }

    // Dialog 4: CBSE / ISC Board Scenarios
    if (showScenariosDialog) {
        val boardScenarios = remember {
            listOf(
                BoardLedgerScenario(
                    id = "cbse_reval_admission",
                    title = "Admission of Partner — Revaluation A/c",
                    boardTag = "CBSE 2024 (6 Marks)",
                    marks = "6 Marks",
                    chapter = "Admission of a Partner",
                    description = "Stock revalued down by ₹5,000; Building appreciated by ₹25,000; Provision for doubtful debts created at 5% on debtors (₹4,000); Outstanding legal charges ₹2,000.",
                    defaultAccountType = com.example.data.model.AccountingWorkspaceType.REVALUATION,
                    suggestedAccountName = "Revaluation A/c",
                    sampleDrRows = listOf(
                        LedgerEntryRow(date = "2024-04-01", particulars = "To Stock A/c (Decrease in value)", amount = "5000"),
                        LedgerEntryRow(date = "2024-04-01", particulars = "To Provision for Doubtful Debts A/c", amount = "4000"),
                        LedgerEntryRow(date = "2024-04-01", particulars = "To Outstanding Legal Charges A/c", amount = "2000")
                    ),
                    sampleCrRows = listOf(
                        LedgerEntryRow(date = "2024-04-01", particulars = "By Building A/c (Appreciation)", amount = "25000")
                    ),
                    hint = "Credit Total (₹25,000) > Debit Total (₹11,000) = Net Revaluation Profit of ₹14,000 transferred to old partners' capital accounts in old ratio."
                ),
                BoardLedgerScenario(
                    id = "cbse_partners_capital",
                    title = "Partners' Capital Account (3 Partners)",
                    boardTag = "CBSE 2023 (8 Marks)",
                    marks = "8 Marks",
                    chapter = "Partnership Accounts",
                    description = "Opening capital: A: ₹2,00,000, B: ₹1,50,000. New partner C brings ₹1,00,000 capital & ₹30,000 premium for goodwill shared equally. General Reserve ₹40,000 distributed.",
                    defaultAccountType = com.example.data.model.AccountingWorkspaceType.PARTNERS_CAPITAL,
                    suggestedAccountName = "Partners' Capital A/c",
                    partners = listOf("Partner A", "Partner B", "Partner C"),
                    sampleDrRows = listOf(
                        LedgerEntryRow(date = "2024-03-31", particulars = "To Drawings A/c", partnerAAmount = "15000", partnerBAmount = "10000", partnerCAmount = "0")
                    ),
                    sampleCrRows = listOf(
                        LedgerEntryRow(date = "2024-04-01", particulars = "By Balance b/d", partnerAAmount = "200000", partnerBAmount = "150000", partnerCAmount = "0"),
                        LedgerEntryRow(date = "2024-04-01", particulars = "By Bank A/c (Capital)", partnerAAmount = "0", partnerBAmount = "0", partnerCAmount = "100000"),
                        LedgerEntryRow(date = "2024-04-01", particulars = "By Premium for Goodwill A/c", partnerAAmount = "15000", partnerBAmount = "15000", partnerCAmount = "0"),
                        LedgerEntryRow(date = "2024-04-01", particulars = "By General Reserve A/c", partnerAAmount = "20000", partnerBAmount = "20000", partnerCAmount = "0")
                    ),
                    hint = "Compute closing capital c/d for each partner after accounting for drawings, premium and reserve additions."
                ),
                BoardLedgerScenario(
                    id = "cbse_realisation_dissolution",
                    title = "Realisation Account on Dissolution",
                    boardTag = "ISC / CBSE 2024 (6 Marks)",
                    marks = "6 Marks",
                    chapter = "Dissolution of Partnership Firm",
                    description = "Sundry Assets transferred: ₹1,80,000. Sundry Creditors transferred: ₹60,000. Assets realised ₹1,55,000. Creditors paid ₹58,000 in full settlement. Dissolution expenses ₹3,000.",
                    defaultAccountType = com.example.data.model.AccountingWorkspaceType.REALISATION,
                    suggestedAccountName = "Realisation A/c",
                    sampleDrRows = listOf(
                        LedgerEntryRow(date = "2024-03-31", particulars = "To Sundry Assets A/c (Book Value)", amount = "180000"),
                        LedgerEntryRow(date = "2024-03-31", particulars = "To Bank A/c (Creditors Paid)", amount = "58000"),
                        LedgerEntryRow(date = "2024-03-31", particulars = "To Bank A/c (Realisation Exp)", amount = "3000")
                    ),
                    sampleCrRows = listOf(
                        LedgerEntryRow(date = "2024-03-31", particulars = "By Sundry Creditors A/c (Book Value)", amount = "60000"),
                        LedgerEntryRow(date = "2024-03-31", particulars = "By Bank A/c (Assets Realised)", amount = "155000")
                    ),
                    hint = "Debit side (₹2,41,000) > Credit side (₹2,15,000) = Realisation Loss of ₹26,000 transferred to partners."
                )
            )
        }

        AlertDialog(
            onDismissRequest = { showScenariosDialog = false },
            title = {
                Text("Select Class 12 Board Exam Scenario", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    boardScenarios.forEach { scenario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (scenario.partners.isNotEmpty()) {
                                        onMultiPartnerAccountChange(
                                            MultiPartnerLedgerAccount(
                                                accountName = scenario.suggestedAccountName,
                                                partnerNames = scenario.partners,
                                                drRows = scenario.sampleDrRows,
                                                crRows = scenario.sampleCrRows
                                            )
                                        )
                                        isMultiPartnerMode = true
                                    } else {
                                        onSingleAccountChange(
                                            SingleLedgerAccount(
                                                accountName = scenario.suggestedAccountName,
                                                drRows = scenario.sampleDrRows,
                                                crRows = scenario.sampleCrRows
                                            )
                                        )
                                        isMultiPartnerMode = false
                                    }
                                    showScenariosDialog = false
                                },
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            border = BorderStroke(1.dp, Slate200),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(scenario.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IndigoPrimary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(AmberPastelBg)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(scenario.boardTag, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AmberText)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(scenario.description, fontSize = 11.sp, color = Slate600)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("💡 Hint: ${scenario.hint}", fontSize = 10.sp, color = EmeraldText, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showScenariosDialog = false }) {
                    Text("Close", color = Slate500)
                }
            }
        )
    }
}

// -------------------------------------------------------------
// 1. Single Account Ledger View (Standard T-Account)
// -------------------------------------------------------------
@Composable
fun SingleAccountLedgerView(
    account: SingleLedgerAccount,
    onAccountChange: (SingleLedgerAccount) -> Unit,
    onAskAi: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")) }

    // Live Balancing Computations
    val balancingResult by remember(account.drRows, account.crRows) {
        derivedStateOf {
            val totalDr = account.drRows.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            val totalCr = account.crRows.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            val diff = Math.abs(totalDr - totalCr)

            val (type, entry, bld, exp) = when {
                totalDr > totalCr -> {
                    listOf(
                        "Debit Balance",
                        "By Balance c/d (Balancing Figure)",
                        "To Balance b/d",
                        "Debit total exceeds Credit total. The balancing figure is written on the Credit side (By Balance c/d) and carried down as a Debit Balance (To Balance b/d) in the next accounting period."
                    )
                }
                totalCr > totalDr -> {
                    listOf(
                        "Credit Balance",
                        "To Balance c/d (Balancing Figure)",
                        "By Balance b/d",
                        "Credit total exceeds Debit total. The balancing figure is written on the Debit side (To Balance c/d) and carried down as a Credit Balance (By Balance b/d) in the next accounting period."
                    )
                }
                else -> {
                    listOf(
                        "Nil / Fully Balanced",
                        "Account Balanced (No c/d needed)",
                        "—",
                        "Debit total equals Credit total. The account is completely squared off without any closing balance."
                    )
                }
            }

            LedgerBalancingResult(
                totalDr = totalDr,
                totalCr = totalCr,
                balanceAmount = diff,
                balanceType = type,
                balancingEntryParticulars = entry,
                broughtDownEntryParticulars = bld,
                ruleExplanation = exp
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main T-Account Container
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LedgerPaper),
            border = BorderStroke(1.5.dp, LedgerGridLine)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // T-Account Top Heading
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dr.", fontWeight = FontWeight.Black, fontSize = 16.sp, color = EmeraldGreen)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = account.accountName.uppercase(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = NavyDark,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "[T-Shape General Ledger • Class 12 Format]",
                            fontSize = 9.sp,
                            color = Slate500
                        )
                    }
                    Text("Cr.", fontWeight = FontWeight.Black, fontSize = 16.sp, color = GoldDark)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Debit & Credit Dual Columns in T-Shape
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    // DEBIT SIDE (Left)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LedgerHeaderBg, RoundedCornerShape(6.dp))
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Date", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(46.dp))
                            Text("Particulars (To ...)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.weight(1f))
                            Text("J.F.", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(26.dp))
                            Text("Amount (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(62.dp), textAlign = TextAlign.End)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Dr Rows
                        account.drRows.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = row.date,
                                    onValueChange = {
                                        val newRows = account.drRows.toMutableList()
                                        newRows[index] = row.copy(date = it)
                                        onAccountChange(account.copy(drRows = newRows))
                                    },
                                    placeholder = { Text("01/04", fontSize = 9.sp) },
                                    modifier = Modifier.width(46.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.particulars,
                                    onValueChange = {
                                        val newRows = account.drRows.toMutableList()
                                        newRows[index] = row.copy(particulars = it)
                                        onAccountChange(account.copy(drRows = newRows))
                                    },
                                    placeholder = { Text("To Particulars", fontSize = 9.sp) },
                                    modifier = Modifier.weight(1f),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontFamily = FontFamily.Monospace),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.jf,
                                    onValueChange = {
                                        val newRows = account.drRows.toMutableList()
                                        newRows[index] = row.copy(jf = it)
                                        onAccountChange(account.copy(drRows = newRows))
                                    },
                                    placeholder = { Text("JF", fontSize = 8.sp) },
                                    modifier = Modifier.width(26.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.amount,
                                    onValueChange = {
                                        val newRows = account.drRows.toMutableList()
                                        newRows[index] = row.copy(amount = it)
                                        onAccountChange(account.copy(drRows = newRows))
                                    },
                                    placeholder = { Text("₹", fontSize = 9.sp) },
                                    modifier = Modifier.width(62.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                if (account.drRows.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val newRows = account.drRows.toMutableList()
                                            newRows.removeAt(index)
                                            onAccountChange(account.copy(drRows = newRows))
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Slate400, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        // Balancing Entry on Dr Side (if Cr > Dr)
                        if (balancingResult.totalCr > balancingResult.totalDr) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AmberPastelBg)
                                    .border(1.dp, AmberText.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("To Balance c/d", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberText)
                                    Text("₹${currencyFormat.format(balancingResult.balanceAmount)}", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AmberText)
                                }
                            }
                        }

                        TextButton(
                            onClick = {
                                onAccountChange(account.copy(drRows = account.drRows + LedgerEntryRow(particulars = "To ")))
                            },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Add Dr Row", fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Vertical Center Divider Line of T-Account
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.5.dp,
                        color = LedgerGridLine
                    )

                    // CREDIT SIDE (Right)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LedgerHeaderBg, RoundedCornerShape(6.dp))
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Date", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(46.dp))
                            Text("Particulars (By ...)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.weight(1f))
                            Text("J.F.", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(26.dp))
                            Text("Amount (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(62.dp), textAlign = TextAlign.End)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Cr Rows
                        account.crRows.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = row.date,
                                    onValueChange = {
                                        val newRows = account.crRows.toMutableList()
                                        newRows[index] = row.copy(date = it)
                                        onAccountChange(account.copy(crRows = newRows))
                                    },
                                    placeholder = { Text("01/04", fontSize = 9.sp) },
                                    modifier = Modifier.width(46.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.particulars,
                                    onValueChange = {
                                        val newRows = account.crRows.toMutableList()
                                        newRows[index] = row.copy(particulars = it)
                                        onAccountChange(account.copy(crRows = newRows))
                                    },
                                    placeholder = { Text("By Particulars", fontSize = 9.sp) },
                                    modifier = Modifier.weight(1f),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontFamily = FontFamily.Monospace),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.jf,
                                    onValueChange = {
                                        val newRows = account.crRows.toMutableList()
                                        newRows[index] = row.copy(jf = it)
                                        onAccountChange(account.copy(crRows = newRows))
                                    },
                                    placeholder = { Text("JF", fontSize = 8.sp) },
                                    modifier = Modifier.width(26.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                OutlinedTextField(
                                    value = row.amount,
                                    onValueChange = {
                                        val newRows = account.crRows.toMutableList()
                                        newRows[index] = row.copy(amount = it)
                                        onAccountChange(account.copy(crRows = newRows))
                                    },
                                    placeholder = { Text("₹", fontSize = 9.sp) },
                                    modifier = Modifier.width(62.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                if (account.crRows.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val newRows = account.crRows.toMutableList()
                                            newRows.removeAt(index)
                                            onAccountChange(account.copy(crRows = newRows))
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Slate400, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        // Balancing Entry on Cr Side (if Dr > Cr)
                        if (balancingResult.totalDr > balancingResult.totalCr) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AmberPastelBg)
                                    .border(1.dp, AmberText.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("By Balance c/d", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberText)
                                    Text("₹${currencyFormat.format(balancingResult.balanceAmount)}", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AmberText)
                                }
                            }
                        }

                        TextButton(
                            onClick = {
                                onAccountChange(account.copy(crRows = account.crRows + LedgerEntryRow(particulars = "By ")))
                            },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Add Cr Row", fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider(thickness = 1.5.dp, color = LedgerGridLine)

                // Totals Row with Accounting Double Underline
                val maxSideTotal = Math.max(balancingResult.totalDr, balancingResult.totalCr)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dr Total
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Debit Total: ₹${currencyFormat.format(maxSideTotal)}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NavyDark)
                        HorizontalDivider(thickness = 1.dp, color = NavyDark, modifier = Modifier.padding(top = 1.dp))
                        HorizontalDivider(thickness = 1.dp, color = NavyDark, modifier = Modifier.padding(top = 1.dp))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Cr Total
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Credit Total: ₹${currencyFormat.format(maxSideTotal)}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NavyDark, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                        HorizontalDivider(thickness = 1.dp, color = NavyDark, modifier = Modifier.padding(top = 1.dp))
                        HorizontalDivider(thickness = 1.dp, color = NavyDark, modifier = Modifier.padding(top = 1.dp))
                    }
                }
            }
        }

        // Live Balancing Summary Card & Explanation
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    balancingResult.totalDr > balancingResult.totalCr -> SkyPastelBg
                    balancingResult.totalCr > balancingResult.totalDr -> AmberPastelBg
                    else -> EmeraldPastelBg
                }
            ),
            border = BorderStroke(
                1.dp,
                when {
                    balancingResult.totalDr > balancingResult.totalCr -> SkyText.copy(alpha = 0.3f)
                    balancingResult.totalCr > balancingResult.totalDr -> AmberText.copy(alpha = 0.3f)
                    else -> EmeraldText.copy(alpha = 0.3f)
                }
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = when {
                                balancingResult.totalDr > balancingResult.totalCr -> SkyText
                                balancingResult.totalCr > balancingResult.totalDr -> AmberText
                                else -> EmeraldGreen
                            },
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BALANCING ANALYSIS: ${balancingResult.balanceType.uppercase()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "₹${currencyFormat.format(balancingResult.balanceAmount)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = balancingResult.ruleExplanation,
                    fontSize = 11.sp,
                    color = Slate700,
                    lineHeight = 16.sp
                )

                if (balancingResult.broughtDownEntryParticulars != "—") {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "➡️ Next Period Opening Entry: ${balancingResult.broughtDownEntryParticulars} ₹${currencyFormat.format(balancingResult.balanceAmount)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoDark
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // AI Tutor Quick Assistance
                Button(
                    onClick = {
                        onAskAi("Please analyze this Class 12 ${account.accountName} with Dr Total = ₹${balancingResult.totalDr}, Cr Total = ₹${balancingResult.totalCr}, and Balancing Figure = ₹${balancingResult.balanceAmount}. Check if any Class 12 board adjustments are missing.")
                    },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Analyze Account with AI Tutor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. Multi-Partner Ledger View (Partners' Capital A/c A, B, C)
// -------------------------------------------------------------
@Composable
fun MultiPartnerLedgerView(
    account: MultiPartnerLedgerAccount,
    onAccountChange: (MultiPartnerLedgerAccount) -> Unit,
    onAskAi: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")) }
    val partners = account.partnerNames

    // Balancing computations for each partner independently
    val partnerBalances by remember(account.drRows, account.crRows, partners) {
        derivedStateOf {
            partners.mapIndexed { idx, pName ->
                val drTotal = account.drRows.sumOf {
                    val amtStr = when (idx) {
                        0 -> it.partnerAAmount
                        1 -> it.partnerBAmount
                        else -> it.partnerCAmount
                    }
                    amtStr.toDoubleOrNull() ?: 0.0
                }
                val crTotal = account.crRows.sumOf {
                    val amtStr = when (idx) {
                        0 -> it.partnerAAmount
                        1 -> it.partnerBAmount
                        else -> it.partnerCAmount
                    }
                    amtStr.toDoubleOrNull() ?: 0.0
                }
                val diff = Math.abs(drTotal - crTotal)
                val type = if (crTotal >= drTotal) "Credit Balance (Capital Intact)" else "Debit Balance (Overdrawn)"
                val cEntry = if (crTotal >= drTotal) "To Balance c/d" else "By Balance c/d"
                val bEntry = if (crTotal >= drTotal) "By Balance b/d" else "To Balance b/d"

                PartnerBalancingResult(
                    partnerName = pName,
                    totalDr = drTotal,
                    totalCr = crTotal,
                    balanceAmount = diff,
                    balanceType = type,
                    balancingEntry = cEntry,
                    broughtDownEntry = bEntry
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Multi-Partner T-Account Card with Horizontal Scroll if 3 partners
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LedgerPaper),
            border = BorderStroke(1.5.dp, LedgerGridLine)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dr.", fontWeight = FontWeight.Black, fontSize = 16.sp, color = EmeraldGreen)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = account.accountName.uppercase(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = NavyDark
                        )
                        Text(
                            text = "Partners: ${partners.joinToString(", ")} • CBSE Multi-Column Layout",
                            fontSize = 9.sp,
                            color = Slate500
                        )
                    }
                    Text("Cr.", fontWeight = FontWeight.Black, fontSize = 16.sp, color = GoldDark)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable container for multi-partner table
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.widthIn(min = 680.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LedgerHeaderBg, RoundedCornerShape(6.dp))
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // DR Header
                            Text("Date", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(42.dp))
                            Text("Particulars (Dr)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(110.dp))
                            partners.forEach { p ->
                                Text("$p (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(64.dp), textAlign = TextAlign.End)
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            VerticalDivider(modifier = Modifier.height(18.dp), thickness = 1.dp, color = Slate400)
                            Spacer(modifier = Modifier.width(8.dp))

                            // CR Header
                            Text("Date", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(42.dp))
                            Text("Particulars (Cr)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(110.dp))
                            partners.forEach { p ->
                                Text("$p (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark, modifier = Modifier.width(64.dp), textAlign = TextAlign.End)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Dr Rows Section
                        Text("Debit Entries (Drawings, Loss, Interest on Drawings):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate600)
                        account.drRows.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = row.date,
                                    onValueChange = {
                                        val list = account.drRows.toMutableList()
                                        list[index] = row.copy(date = it)
                                        onAccountChange(account.copy(drRows = list))
                                    },
                                    placeholder = { Text("Date", fontSize = 9.sp) },
                                    modifier = Modifier.width(42.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedTextField(
                                    value = row.particulars,
                                    onValueChange = {
                                        val list = account.drRows.toMutableList()
                                        list[index] = row.copy(particulars = it)
                                        onAccountChange(account.copy(drRows = list))
                                    },
                                    placeholder = { Text("To Particulars", fontSize = 9.sp) },
                                    modifier = Modifier.width(110.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                // Partner A amount
                                OutlinedTextField(
                                    value = row.partnerAAmount,
                                    onValueChange = {
                                        val list = account.drRows.toMutableList()
                                        list[index] = row.copy(partnerAAmount = it)
                                        onAccountChange(account.copy(drRows = list))
                                    },
                                    placeholder = { Text("A ₹", fontSize = 9.sp) },
                                    modifier = Modifier.width(64.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                if (partners.size > 1) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    OutlinedTextField(
                                        value = row.partnerBAmount,
                                        onValueChange = {
                                            val list = account.drRows.toMutableList()
                                            list[index] = row.copy(partnerBAmount = it)
                                            onAccountChange(account.copy(drRows = list))
                                        },
                                        placeholder = { Text("B ₹", fontSize = 9.sp) },
                                        modifier = Modifier.width(64.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                if (partners.size > 2) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    OutlinedTextField(
                                        value = row.partnerCAmount,
                                        onValueChange = {
                                            val list = account.drRows.toMutableList()
                                            list[index] = row.copy(partnerCAmount = it)
                                            onAccountChange(account.copy(drRows = list))
                                        },
                                        placeholder = { Text("C ₹", fontSize = 9.sp) },
                                        modifier = Modifier.width(64.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                if (account.drRows.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val list = account.drRows.toMutableList()
                                            list.removeAt(index)
                                            onAccountChange(account.copy(drRows = list))
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = {
                                onAccountChange(account.copy(drRows = account.drRows + LedgerEntryRow(particulars = "To ")))
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Add Debit Entry Row", fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(thickness = 1.dp, color = Slate200)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Cr Rows Section
                        Text("Credit Entries (Opening Capital, Reserves, Profit, Goodwill):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate600)
                        account.crRows.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = row.date,
                                    onValueChange = {
                                        val list = account.crRows.toMutableList()
                                        list[index] = row.copy(date = it)
                                        onAccountChange(account.copy(crRows = list))
                                    },
                                    placeholder = { Text("Date", fontSize = 9.sp) },
                                    modifier = Modifier.width(42.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedTextField(
                                    value = row.particulars,
                                    onValueChange = {
                                        val list = account.crRows.toMutableList()
                                        list[index] = row.copy(particulars = it)
                                        onAccountChange(account.copy(crRows = list))
                                    },
                                    placeholder = { Text("By Particulars", fontSize = 9.sp) },
                                    modifier = Modifier.width(110.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                // Partner A amount
                                OutlinedTextField(
                                    value = row.partnerAAmount,
                                    onValueChange = {
                                        val list = account.crRows.toMutableList()
                                        list[index] = row.copy(partnerAAmount = it)
                                        onAccountChange(account.copy(crRows = list))
                                    },
                                    placeholder = { Text("A ₹", fontSize = 9.sp) },
                                    modifier = Modifier.width(64.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                if (partners.size > 1) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    OutlinedTextField(
                                        value = row.partnerBAmount,
                                        onValueChange = {
                                            val list = account.crRows.toMutableList()
                                            list[index] = row.copy(partnerBAmount = it)
                                            onAccountChange(account.copy(crRows = list))
                                        },
                                        placeholder = { Text("B ₹", fontSize = 9.sp) },
                                        modifier = Modifier.width(64.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                if (partners.size > 2) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    OutlinedTextField(
                                        value = row.partnerCAmount,
                                        onValueChange = {
                                            val list = account.crRows.toMutableList()
                                            list[index] = row.copy(partnerCAmount = it)
                                            onAccountChange(account.copy(crRows = list))
                                        },
                                        placeholder = { Text("C ₹", fontSize = 9.sp) },
                                        modifier = Modifier.width(64.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                if (account.crRows.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val list = account.crRows.toMutableList()
                                            list.removeAt(index)
                                            onAccountChange(account.copy(crRows = list))
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = {
                                onAccountChange(account.copy(crRows = account.crRows + LedgerEntryRow(particulars = "By ")))
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Add Credit Entry Row", fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Summary Card for All Partners
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "PARTNERS' CLOSING CAPITAL (BALANCE c/d) SUMMARY:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = IndigoPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                partnerBalances.forEach { pBal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pBal.partnerName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                            Text("Total Dr: ₹${currencyFormat.format(pBal.totalDr)} | Total Cr: ₹${currencyFormat.format(pBal.totalCr)}", fontSize = 10.sp, color = Slate500)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Closing Capital: ₹${currencyFormat.format(pBal.balanceAmount)}",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = EmeraldGreen
                            )
                            Text(pBal.balancingEntry, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = GoldDark)
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Slate100)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val summaryText = partnerBalances.joinToString(", ") { "${it.partnerName}: Closing Capital = ₹${it.balanceAmount}" }
                        onAskAi("Analyze Partners' Capital Account: $summaryText. Verify if goodwill premium and general reserve are distributed in sacrifice / old profit sharing ratio correctly.")
                    },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Verify Partners' Capital with AI Tutor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
