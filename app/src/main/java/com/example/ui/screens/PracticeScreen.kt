package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.*
import com.example.data.repository.BoardMentorRepository
import com.example.data.repository.ExamRepository
import com.example.ui.components.PhotoQuestionAnalysisDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun PracticeScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.studentProfile.collectAsState()
    val importantQuestions by viewModel.generatedImportantQuestions.collectAsState()
    val isAiLoading by viewModel.aiTutorLoading.collectAsState()
    val photoResult by viewModel.photoAnalysisResult.collectAsState()

    var practiceTab by remember { mutableStateOf("MCQ_BANK") } // MCQ_BANK, AI_IMPORTANT, PYQ_TRENDS
    var selectedSubject by remember { mutableStateOf("Accountancy") }
    var showPhotoDialog by remember { mutableStateOf(false) }

    // User selected MCQ answers mapping: questionId -> selectedOption
    var selectedAnswers by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var showExplanations by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SlateCanvas,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.navigateTo(AppNavDestination.HOME) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text("Practice & Question Bank", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("${profile.boardDisplayName} • ${profile.gradeClassDisplayName}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { showPhotoDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        border = BorderStroke(1.dp, IndigoPrimary)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Photo Solver", fontSize = 11.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                // Practice Sub-Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PracticeSubTab(
                        title = "✍️ MCQ Bank",
                        isSelected = practiceTab == "MCQ_BANK",
                        onClick = { practiceTab = "MCQ_BANK" },
                        modifier = Modifier.weight(1f)
                    )
                    PracticeSubTab(
                        title = "🎯 AI Important Qs",
                        isSelected = practiceTab == "AI_IMPORTANT",
                        onClick = { practiceTab = "AI_IMPORTANT" },
                        modifier = Modifier.weight(1f)
                    )
                    PracticeSubTab(
                        title = "📊 PYQ Trends",
                        isSelected = practiceTab == "PYQ_TRENDS",
                        onClick = { practiceTab = "PYQ_TRENDS" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (practiceTab) {
                "MCQ_BANK" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = IndigoContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("🎯 Instant MCQ Practice & Mastery", color = IndigoPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Select your option to see instant verification and 7-step rationale.", color = Slate700, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    val questions = ExamRepository.allPyqQuestions.filter { it.type == QuestionType.MCQ_1M }
                    items(questions) { q ->
                        val selectedOpt = selectedAnswers[q.id]
                        val showExp = showExplanations[q.id] ?: false

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Slate100
                                    ) {
                                        Text(
                                            text = "${q.chapter} • ${q.marks.toInt()} Mark",
                                            color = Slate700,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(q.difficulty.name, color = Slate400, fontSize = 10.sp)
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(q.questionText, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                                Spacer(modifier = Modifier.height(12.dp))

                                q.options.forEach { opt ->
                                    val isChosen = selectedOpt == opt
                                    val isCorrect = opt == q.correctAnswer
                                    val optionBg = if (selectedOpt != null) {
                                        if (isCorrect) EmeraldContainer else if (isChosen) RoseContainer else Slate50
                                    } else Slate50

                                    val optionBorder = if (selectedOpt != null) {
                                        if (isCorrect) EmeraldPrimary else if (isChosen) RosePrimary else Slate200
                                    } else Slate200

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(optionBg)
                                            .clickable(enabled = selectedOpt == null) {
                                                selectedAnswers = selectedAnswers + (q.id to opt)
                                                showExplanations = showExplanations + (q.id to true)
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = opt,
                                            color = if (selectedOpt != null && isCorrect) EmeraldPrimary else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isChosen || (selectedOpt != null && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (selectedOpt != null) {
                                            if (isCorrect) {
                                                Text("✓ Correct", color = EmeraldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            } else if (isChosen) {
                                                Text("✗ Incorrect", color = RosePrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                if (showExp) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Slate100,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text("💡 Explanation:", color = Slate800, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text(q.explanation, color = Slate700, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "AI_IMPORTANT" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("🤖", fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text("AI Important Question Generator", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Text("Generates novel, high-probability board questions", color = TextSecondary, fontSize = 11.sp)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        viewModel.generateAiImportantQuestion(
                                            subject = selectedSubject,
                                            chapter = "Accounting for Share Capital",
                                            difficulty = "Board Exam Level"
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (isAiLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Generating with AI...", fontSize = 12.sp)
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Generate Fresh Important Question", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    items(importantQuestions) { item ->
                        var showSolution by remember { mutableStateOf(false) }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GoldContainer
                                    ) {
                                        Text(
                                            text = "🎯 ${item.probabilityScore}% Board Probability",
                                            color = GoldPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = IndigoContainer
                                    ) {
                                        Text(
                                            text = "${item.marks} Marks • ${item.chapter}",
                                            color = IndigoPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(item.questionText, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                                Spacer(modifier = Modifier.height(10.dp))
                                TextButton(
                                    onClick = { showSolution = !showSolution }
                                ) {
                                    Text(
                                        text = if (showSolution) "Hide Solution & Marking Scheme ▲" else "View Detailed Solution & Step Marking ▼",
                                        fontSize = 11.sp,
                                        color = IndigoPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                AnimatedVisibility(visible = showSolution) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Slate50, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text("📝 Step-by-Step Marking Breakdown:", color = Slate800, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        item.markingBreakdown.forEach { step ->
                                            Text("• $step", color = Slate700, fontSize = 10.sp)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("✅ Ideal Answer / Working Notes:", color = Slate800, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(item.idealAnswerOutline, color = Slate700, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                "PYQ_TRENDS" -> {
                    val trends = BoardMentorRepository.getPyqTrends("Accountancy")
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("📊 5-10 Year Board Exam PYQ Analysis", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Data-driven chapter weightage and recurring board question types.", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    items(trends) { trend ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(trend.chapterName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (trend.averageMarksWeightage >= 10) RoseContainer else GoldContainer
                                    ) {
                                        Text(
                                            text = "Avg ${trend.averageMarksWeightage} Marks",
                                            color = if (trend.averageMarksWeightage >= 10) RosePrimary else GoldPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Frequently Tested (${trend.frequentlyAskedYears}):",
                                    color = Slate700,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                trend.recurringSubtopics.forEach { sub ->
                                    Text("• $sub", color = Slate600, fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Typical Blueprint Pattern:", color = Slate500, fontSize = 10.sp)
                                Text(trend.questionTypeDistribution, color = IndigoPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPhotoDialog) {
        PhotoQuestionAnalysisDialog(
            result = photoResult,
            isLoading = isAiLoading,
            onAnalyzeSample = { prompt -> viewModel.performPhotoQuestionAnalysis(prompt, selectedSubject) },
            onDismiss = { showPhotoDialog = false }
        )
    }
}

@Composable
private fun PracticeSubTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) IndigoPrimary else Slate100,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else Slate700,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
