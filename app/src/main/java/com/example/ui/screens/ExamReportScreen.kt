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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun ExamReportScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val report by viewModel.latestExamReport.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(AppNavDestination.HOME) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ACCOUNTANCY PERFORMANCE REPORT",
                        color = GoldAccent,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Rubric Score, AI Mistake Breakdown & Board Readiness",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    ) { padding ->
        if (report != null) {
            val rep = report!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Score Hero Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = rep.examTitle.uppercase(),
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${rep.scoredMarks.toInt()} / ${rep.totalMarks}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp
                            )
                            Text(
                                text = "Percentage: ${rep.percentage.toInt()}% • Accuracy: ${rep.accuracyPercentage}%",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(GoldAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = rep.mission100ReadinessDelta,
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Strengths & Weaknesses
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🟢 Strong Areas", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                rep.strongChapters.forEach { ch ->
                                    Text("• $ch", fontSize = 11.sp, color = Color.DarkGray)
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🔴 Weak Areas", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CrimsonRed)
                                Spacer(modifier = Modifier.height(4.dp))
                                rep.weakChapters.forEach { ch ->
                                    Text("• $ch", fontSize = 11.sp, color = Color.DarkGray)
                                }
                            }
                        }
                    }
                }

                // AI Mistake Breakdown & Step Rules
                item {
                    Text(
                        text = "Detailed Mistake & Rubric Diagnosis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyDark
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("What was wrong:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CrimsonRed)
                            rep.whatWasWrong.forEach { Text("• $it", fontSize = 11.sp, color = Color.DarkGray) }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Applicable Statutory Rules:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)
                            rep.rulesApplicable.forEach { Text("• $it", fontSize = 11.sp, color = Color.DarkGray) }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("How to fix for 100/100 Board Target:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldGreen)
                            rep.howToFix.forEach { Text("• $it", fontSize = 11.sp, color = Color.DarkGray) }
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.askAiTutor("Explain how to fix my weak areas: ${rep.weakChapters.joinToString(", ")}", "Accountancy Test Revision")
                                viewModel.navigateTo(AppNavDestination.AI_TUTOR)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ask AI Tutor", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppNavDestination.HOME) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Continue Prep", fontSize = 12.sp)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
