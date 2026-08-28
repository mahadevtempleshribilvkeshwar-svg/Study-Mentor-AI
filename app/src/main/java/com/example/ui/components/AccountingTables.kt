package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BalanceSheetItem
import com.example.data.model.CashFlowItem
import com.example.data.model.JournalEntryRow
import com.example.data.model.LedgerEntryRow
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LedgerGridLine
import com.example.ui.theme.LedgerHeaderBg
import com.example.ui.theme.LedgerPaper
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun JournalWorkspaceTable(
    rows: List<JournalEntryRow>,
    onRowUpdated: (Int, JournalEntryRow) -> Unit,
    onAddRow: () -> Unit,
    onRemoveRow: (Int) -> Unit,
    onCheckBalance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LedgerPaper),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, LedgerGridLine)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📖 JOURNAL OF THE FIRM",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Dr. (₹) / Cr. (₹)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = GoldAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Journal Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LedgerHeaderBg, RoundedCornerShape(6.dp))
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date",
                    modifier = Modifier.width(68.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NavyDark
                )
                Text(
                    text = "Particulars & Narration",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NavyDark
                )
                Text(
                    text = "L.F.",
                    modifier = Modifier.width(36.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NavyDark
                )
                Text(
                    text = "Debit (₹)",
                    modifier = Modifier.width(72.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NavyDark
                )
                Text(
                    text = "Credit (₹)",
                    modifier = Modifier.width(72.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NavyDark
                )
                Spacer(modifier = Modifier.width(28.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Editable Rows
            rows.forEachIndexed { index, row ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(0.5.dp, LedgerGridLine, RoundedCornerShape(4.dp))
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = row.date,
                            onValueChange = { onRowUpdated(index, row.copy(date = it)) },
                            placeholder = { Text("01/04", fontSize = 10.sp) },
                            modifier = Modifier
                                .width(68.dp)
                                .testTag("journal_date_$index"),
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = row.particulars,
                            onValueChange = { onRowUpdated(index, row.copy(particulars = it)) },
                            placeholder = { Text("e.g. Bank A/c Dr.", fontSize = 10.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("journal_particulars_$index"),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            ),
                            singleLine = false,
                            maxLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = row.lf,
                            onValueChange = { onRowUpdated(index, row.copy(lf = it)) },
                            placeholder = { Text("LF", fontSize = 10.sp) },
                            modifier = Modifier.width(36.dp),
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = row.debitAmount,
                            onValueChange = { onRowUpdated(index, row.copy(debitAmount = it)) },
                            placeholder = { Text("₹ Dr", fontSize = 10.sp) },
                            modifier = Modifier
                                .width(72.dp)
                                .testTag("journal_debit_$index"),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = row.creditAmount,
                            onValueChange = { onRowUpdated(index, row.copy(creditAmount = it)) },
                            placeholder = { Text("₹ Cr", fontSize = 10.sp) },
                            modifier = Modifier
                                .width(72.dp)
                                .testTag("journal_credit_$index"),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent)
                        )

                        if (rows.size > 1) {
                            IconButton(
                                onClick = { onRemoveRow(index) },
                                modifier = Modifier.width(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Row",
                                    tint = Color.Gray,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(28.dp))
                        }
                    }

                    // Narration input
                    OutlinedTextField(
                        value = row.narration,
                        onValueChange = { onRowUpdated(index, row.copy(narration = it)) },
                        placeholder = { Text("(Being... narration required for board exams)", fontSize = 10.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            fontFamily = FontFamily.SansSerif
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons: Add Row & Check Balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onAddRow,
                    modifier = Modifier.testTag("add_journal_row_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Entry Row", fontSize = 12.sp)
                }

                Button(
                    onClick = onCheckBalance,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    modifier = Modifier.testTag("check_balance_button")
                ) {
                    Text("Verify Dr = Cr", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun LedgerWorkspaceTable(
    accountTitle: String,
    drRows: List<LedgerEntryRow>,
    crRows: List<LedgerEntryRow>,
    onDrRowUpdated: (Int, LedgerEntryRow) -> Unit,
    onCrRowUpdated: (Int, LedgerEntryRow) -> Unit,
    onAddDrRow: () -> Unit,
    onAddCrRow: () -> Unit,
    onVerifyBalance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LedgerPaper),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, LedgerGridLine)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // T-Account Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dr.", fontWeight = FontWeight.Bold, color = EmeraldGreen, fontSize = 14.sp)
                Text(
                    text = accountTitle.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                )
                Text("Cr.", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Debit Side Column
            Text(
                text = "Debit Side (To ...)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NavyDark
            )
            drRows.forEachIndexed { i, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = row.particulars,
                        onValueChange = { onDrRowUpdated(i, row.copy(particulars = it)) },
                        placeholder = { Text("To Particulars", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = row.amount,
                        onValueChange = { onDrRowUpdated(i, row.copy(amount = it)) },
                        placeholder = { Text("Amount (₹)", fontSize = 10.sp) },
                        modifier = Modifier.width(90.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
            OutlinedButton(onClick = onAddDrRow, modifier = Modifier.padding(vertical = 4.dp)) {
                Text("+ Add Dr Row", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Credit Side Column
            Text(
                text = "Credit Side (By ...)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NavyDark
            )
            crRows.forEachIndexed { i, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = row.particulars,
                        onValueChange = { onCrRowUpdated(i, row.copy(particulars = it)) },
                        placeholder = { Text("By Particulars", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = row.amount,
                        onValueChange = { onCrRowUpdated(i, row.copy(amount = it)) },
                        placeholder = { Text("Amount (₹)", fontSize = 10.sp) },
                        modifier = Modifier.width(90.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
            OutlinedButton(onClick = onAddCrRow, modifier = Modifier.padding(vertical = 4.dp)) {
                Text("+ Add Cr Row", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onVerifyBalance,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compute Balancing Figure (c/d)")
            }
        }
    }
}
