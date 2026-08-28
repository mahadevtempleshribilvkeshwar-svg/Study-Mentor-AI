package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountingWorkspaceType
import com.example.ui.components.CoreLedgerWorkspace
import com.example.ui.components.JournalWorkspaceTable
import com.example.ui.components.LedgerWorkspaceTable
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun WorkspaceScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val workspaceType by viewModel.workspaceType.collectAsState()
    val journalRows by viewModel.workspaceJournalRows.collectAsState()
    val ledgerDrRows by viewModel.workspaceLedgerRowsDr.collectAsState()
    val ledgerCrRows by viewModel.workspaceLedgerRowsCr.collectAsState()
    val singleLedgerAccount by viewModel.singleLedgerAccount.collectAsState()
    val multiPartnerLedgerAccount by viewModel.multiPartnerLedgerAccount.collectAsState()
    val balanceSheetItems by viewModel.workspaceBalanceSheetItems.collectAsState()
    val cashFlowItems by viewModel.workspaceCashFlowItems.collectAsState()
    val workspaceMessage by viewModel.workspaceMessage.collectAsState()

    var draftTitle by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = com.example.ui.theme.SlateCanvas,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.navigateTo(AppNavDestination.HOME) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.ui.theme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACCOUNTING WORKSPACE (CREATE ACCOUNT)",
                            color = com.example.ui.theme.IndigoPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Interactive Blank Journal, Ledger & Financial Statements",
                            color = com.example.ui.theme.Slate500,
                            fontSize = 10.sp
                        )
                    }
                }
                androidx.compose.material3.HorizontalDivider(
                    thickness = 1.dp,
                    color = com.example.ui.theme.Slate200
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Account / Statement Type Selector
            item {
                Text(
                    text = "Select Blank Account Format:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyDark
                )
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AccountingWorkspaceType.values()) { type ->
                        val isSelected = type == workspaceType
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.setWorkspaceType(type) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("select_workspace_${type.name}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.displayName,
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Overview banner for selected format
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = workspaceType.displayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NavyDark
                        )
                        Text(
                            text = workspaceType.description,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Render Appropriate Blank Table
            item {
                when (workspaceType) {
                    AccountingWorkspaceType.JOURNAL -> {
                        JournalWorkspaceTable(
                            rows = journalRows,
                            onRowUpdated = { index, row -> viewModel.updateJournalRow(index, row) },
                            onAddRow = { viewModel.addJournalRow() },
                            onRemoveRow = { index -> viewModel.removeJournalRow(index) },
                            onCheckBalance = { viewModel.checkWorkspaceBalance() }
                        )
                    }
                    AccountingWorkspaceType.LEDGER,
                    AccountingWorkspaceType.REVALUATION,
                    AccountingWorkspaceType.REALISATION,
                    AccountingWorkspaceType.PARTNERS_CAPITAL -> {
                        CoreLedgerWorkspace(
                            singleAccount = singleLedgerAccount,
                            onSingleAccountChange = { viewModel.updateSingleLedgerAccount(it) },
                            multiPartnerAccount = multiPartnerLedgerAccount,
                            onMultiPartnerAccountChange = { viewModel.updateMultiPartnerLedgerAccount(it) },
                            onAskAiAboutLedger = { query ->
                                viewModel.askAiTutor(query, "Class 12 Accountancy Ledger Balancing")
                                viewModel.navigateTo(AppNavDestination.AI_TUTOR)
                            }
                        )
                    }
                    AccountingWorkspaceType.BALANCE_SHEET -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "BALANCE SHEET (SCHEDULE III PART I)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                balanceSheetItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.particular,
                                            fontSize = 12.sp,
                                            fontWeight = if (item.particular.startsWith("I.") || item.particular.startsWith("II.")) FontWeight.Bold else FontWeight.Normal,
                                            color = if (item.particular.startsWith("I.") || item.particular.startsWith("II.")) NavyDark else Color.DarkGray
                                        )
                                        if (item.amount.isNotEmpty()) {
                                            Text(
                                                text = "₹${item.amount}",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                color = EmeraldGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    AccountingWorkspaceType.CASH_FLOW -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "CASH FLOW STATEMENT (AS-3 REVISED)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                cashFlowItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "[${item.activity}] ${item.description}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = if (item.isOutflow) "(₹${item.amount})" else "₹${item.amount}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = if (item.isOutflow) Color(0xFFDC2626) else EmeraldGreen
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Message Banner
            if (workspaceMessage != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyPrimary.copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyPrimary)
                    ) {
                        Text(
                            text = workspaceMessage ?: "",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                    }
                }
            }

            // Save Draft Feature
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = draftTitle,
                        onValueChange = { draftTitle = it },
                        placeholder = { Text("Draft Title (e.g. Q4 Admission Solution)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Button(
                        onClick = { viewModel.saveWorkspaceDraft(draftTitle) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save Draft", fontSize = 12.sp)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
