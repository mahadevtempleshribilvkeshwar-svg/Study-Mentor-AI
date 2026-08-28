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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.VirtualBusiness
import com.example.data.repository.GameRepository
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun GameScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val selectedBusiness by viewModel.selectedBusiness.collectAsState()
    val currentScenarioIndex by viewModel.currentScenarioIndex.collectAsState()
    val gameFeedback by viewModel.gameFeedback.collectAsState()
    val gameAnswerCorrect by viewModel.gameAnswerCorrect.collectAsState()

    val scenarios = GameRepository.getScenariosForBusiness(selectedBusiness.id)
    val currentScenario = scenarios.getOrNull(currentScenarioIndex) ?: scenarios.first()

    var debitInput by remember(currentScenarioIndex) { mutableStateOf("") }
    var creditInput by remember(currentScenarioIndex) { mutableStateOf("") }
    var amountInput by remember(currentScenarioIndex) { mutableStateOf("") }
    var narrationInput by remember(currentScenarioIndex) { mutableStateOf("") }

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
                            text = "ACCOUNTING BUSINESS SIMULATOR",
                            color = com.example.ui.theme.IndigoPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Solve Real-World Transactions • Earn XP & Coins",
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

            // 1. Business Selection Strip
            item {
                Text(
                    text = "Select Virtual Enterprise:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyDark
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GameRepository.availableBusinesses) { biz ->
                        val isSelected = biz.id == selectedBusiness.id
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { viewModel.selectBusiness(biz) }
                                .testTag("select_biz_${biz.id}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "${biz.iconEmoji} ${biz.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else Color.Black,
                                    maxLines = 1
                                )
                                Text(
                                    text = biz.typeName,
                                    fontSize = 10.sp,
                                    color = if (isSelected) GoldAccent else Color.Gray,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // 2. Active Enterprise Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${selectedBusiness.iconEmoji} ${selectedBusiness.name}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NavyDark
                            )
                            Text(
                                text = selectedBusiness.description,
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // 3. Current Transaction Scenario
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SCENARIO ${currentScenarioIndex + 1} OF ${scenarios.size}",
                                color = GoldAccent,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "+${currentScenario.xpReward} XP",
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentScenario.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentScenario.transactionText,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "💡 Context: ${currentScenario.contextInfo}",
                            color = GoldAccent.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // 4. Interactive Accounting Decision Form
            item {
                Text(
                    text = "Make Accounting Decision (Double Entry):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyDark
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = debitInput,
                        onValueChange = { debitInput = it },
                        label = { Text("Account to be DEBITED (Dr.)") },
                        placeholder = { Text("e.g. Purchases A/c, Cash A/c, Bank A/c") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("game_debit_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = creditInput,
                        onValueChange = { creditInput = it },
                        label = { Text("Account to be CREDITED (Cr.)") },
                        placeholder = { Text("e.g. Creditor A/c, Capital A/c, Sales A/c") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("game_credit_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Transaction Amount (₹)") },
                        placeholder = { Text("e.g. 180000") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("game_amount_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = narrationInput,
                        onValueChange = { narrationInput = it },
                        label = { Text("Narration (Being...)") },
                        placeholder = { Text("Being goods purchased on credit...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // 5. Submit Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.submitGameAnswer(
                                currentScenario,
                                debitInput,
                                creditInput,
                                amountInput,
                                narrationInput
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("submit_game_answer_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Verify Accounting Entry", fontWeight = FontWeight.Bold)
                    }

                    if (gameAnswerCorrect != null) {
                        Button(
                            onClick = { viewModel.nextGameScenario() },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            modifier = Modifier.height(48.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Next ➡️", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 6. Feedback Banner
            if (gameFeedback != null) {
                item {
                    val isSuccess = gameAnswerCorrect == true
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccess) EmeraldGreen.copy(alpha = 0.15f) else CrimsonRed.copy(alpha = 0.15f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSuccess) EmeraldGreen else CrimsonRed
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = gameFeedback ?: "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSuccess) EmeraldGreen else CrimsonRed
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
