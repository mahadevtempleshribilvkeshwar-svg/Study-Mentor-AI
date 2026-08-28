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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
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
import com.example.data.model.QuestionType
import com.example.ui.components.JournalWorkspaceTable
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun ExamActiveScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val activeExamCategory by viewModel.activeExamCategory.collectAsState()
    val activeExamQuestions by viewModel.activeExamQuestions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val studentResponses by viewModel.studentResponses.collectAsState()
    val timeRemaining by viewModel.activeExamTimeRemaining.collectAsState()

    var showSubmitDialog by remember { mutableStateOf(false) }

    val currentQuestion = activeExamQuestions.getOrNull(currentQuestionIndex)
    val currentResponse = currentQuestion?.let { studentResponses[it.id] }

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activeExamCategory.title.uppercase(),
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Question ${currentQuestionIndex + 1} of ${activeExamQuestions.size}",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }

                    // Timer Chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { showSubmitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Question Palette Strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(activeExamQuestions) { idx, q ->
                        val resp = studentResponses[q.id]
                        val isCurrent = idx == currentQuestionIndex
                        val isAnswered = resp?.isAnswered == true
                        val isMarked = resp?.isMarkedForReview == true

                        val (bgColor, textColor) = when {
                            isCurrent -> Pair(GoldAccent, NavyDark)
                            isMarked -> Pair(Color(0xFF7C3AED), Color.White)
                            isAnswered -> Pair(EmeraldGreen, Color.White)
                            else -> Pair(Color.White.copy(alpha = 0.2f), Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(bgColor)
                                .clickable { viewModel.selectExamQuestionIndex(idx) }
                                .testTag("palette_q_${idx + 1}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (currentQuestion != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Question Header Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyPrimary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Section: ${currentQuestion.chapterName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "[ ${currentQuestion.marks} Marks ]",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = GoldAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Q${currentQuestion.questionNumber}. ${currentQuestion.questionText}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = NavyDark
                            )

                            if (currentQuestion.instructions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Instructions: ${currentQuestion.instructions}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Input Response Space according to Question Type
                item {
                    when (currentQuestion.type) {
                        QuestionType.MCQ -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                currentQuestion.mcqOptions.forEachIndexed { optIndex, optionText ->
                                    val isSelected = currentResponse?.selectedMcqOption == optIndex
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                            .clickable { viewModel.updateStudentMcqChoice(currentQuestion.id, optIndex) }
                                            .padding(12.dp)
                                            .testTag("exam_mcq_option_$optIndex")
                                    ) {
                                        Text(
                                            text = optionText,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }

                        QuestionType.NUMERICAL_INPUT -> {
                            OutlinedTextField(
                                value = currentResponse?.numericalInput ?: "",
                                onValueChange = { viewModel.updateStudentNumericalInput(currentQuestion.id, it) },
                                label = { Text("Enter Final Calculated Amount (₹)") },
                                placeholder = { Text("e.g. 313000") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("exam_numerical_input"),
                                singleLine = true
                            )
                        }

                        else -> {
                            // Journal / Blank Workspace Input
                            OutlinedTextField(
                                value = currentResponse?.writtenAnswerText ?: "",
                                onValueChange = { viewModel.updateStudentWrittenAnswer(currentQuestion.id, it) },
                                label = { Text("Write Accounting Entries / Steps / Narration") },
                                placeholder = { Text("Debit ... A/c (₹...)\n  To ... A/c (₹...)\n(Being ...)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .testTag("exam_written_answer_input"),
                                maxLines = 10
                            )
                        }
                    }
                }

                // Navigation Controls (Prev, Next, Mark for Review)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (currentQuestionIndex > 0) {
                                    viewModel.selectExamQuestionIndex(currentQuestionIndex - 1)
                                }
                            },
                            enabled = currentQuestionIndex > 0
                        ) {
                            Text("⬅️ Previous")
                        }

                        OutlinedButton(
                            onClick = { viewModel.toggleMarkForReview(currentQuestion.id) }
                        ) {
                            Icon(Icons.Default.Flag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (currentResponse?.isMarkedForReview == true) "Unmark" else "Mark for Review", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                if (currentQuestionIndex < activeExamQuestions.size - 1) {
                                    viewModel.selectExamQuestionIndex(currentQuestionIndex + 1)
                                } else {
                                    showSubmitDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                        ) {
                            Text(if (currentQuestionIndex == activeExamQuestions.size - 1) "Review & Submit" else "Next ➡️")
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Submit Exam?") },
            text = {
                Text("Are you ready to submit your paper? You will receive a detailed Accountancy Performance Report with rubric evaluation and AI mistake breakdown.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        viewModel.submitActiveExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text("Yes, Submit Paper")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSubmitDialog = false }) {
                    Text("Continue Solving")
                }
            }
        )
    }
}
