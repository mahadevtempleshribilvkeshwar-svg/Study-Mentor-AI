package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ImportantPoint
import com.example.data.model.PriorityLevel
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseText
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TextPrimary

@Composable
fun TopGamificationHeader(
    xp: Int,
    coins: Int,
    level: Int,
    streak: Int,
    boardName: String,
    userName: String = "Aryan Sharma",
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Avatar & Name/Board
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(IndigoPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (userName.take(1).ifEmpty { "A" }).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = userName,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = boardName.uppercase(),
                            color = Slate500,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Level Pill Badge & Gamification Stats
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Streak Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFFFF7ED))
                            .border(1.dp, Color(0xFFFFEDD5), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = Color(0xFFEA580C),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${streak}d",
                            color = Color(0xFFEA580C),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Level Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IndigoContainer)
                            .border(1.dp, IndigoBorder, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(IndigoPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LVL $level",
                            color = IndigoDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Slate200)
        }
    }
}

@Composable
fun Mission100Card(
    readinessPercentage: Int = 76,
    targetDate: String = "Mar 2025",
    daysLeft: Int = 42,
    onInspectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onInspectClick() }
            .testTag("mission_100_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header Row: Mission 100 + Big Readiness & Target Days
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "MISSION 100",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$readinessPercentage%",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ready",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Slate400,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "TARGET: $targetDate".uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate500,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$daysLeft Days Left",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rounded-full Progress Bar
            LinearProgressIndicator(
                progress = { readinessPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = IndigoPrimary,
                trackColor = Slate100,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle Guidance Message
            val annotatedFeedback = buildAnnotatedString {
                append("You're strong in ")
                withStyle(style = SpanStyle(color = IndigoDark, fontWeight = FontWeight.SemiBold)) {
                    append("Partnership")
                }
                append(", but need focus on ")
                withStyle(style = SpanStyle(color = Color(0xFFEF4444), fontWeight = FontWeight.SemiBold)) {
                    append("Cash Flow")
                }
                append(" for full mastery.")
            }

            Text(
                text = annotatedFeedback,
                fontSize = 11.sp,
                color = Slate500,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun BoardCountdownCard(
    daysLeft: Int = 42,
    boardName: String = "CBSE",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(IndigoContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⏳", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "$daysLeft DAYS TO $boardName BOARDS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = IndigoDark,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = if (daysLeft > 30) "Phase 1: Concept Mastery & Workspaces" else "Phase 2: PYQ Solving & Full Mocks",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(IndigoContainer)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Timeline Plan",
                    color = IndigoDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ImportantPointItemCard(
    point: ImportantPoint,
    modifier: Modifier = Modifier
) {
    val (badgeBg, badgeText, badgeIcon) = when (point.priority) {
        PriorityLevel.VERY_IMPORTANT -> Triple(Color(0xFFFFF1F2), Color(0xFFE11D48), "🔥 VERY IMPORTANT")
        PriorityLevel.IMPORTANT -> Triple(Color(0xFFFFFBEB), Color(0xFFD97706), "⭐ IMPORTANT")
        PriorityLevel.MUST_REVISE -> Triple(Color(0xFFEEF2FF), Color(0xFF4F46E5), "📌 MUST REVISE")
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = point.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeIcon,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = badgeText,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = point.description,
                fontSize = 12.sp,
                color = Slate600,
                lineHeight = 16.sp
            )

            if (point.ruleOrFormula.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "📐 Rule/Formula: ${point.ruleOrFormula}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IndigoDark
                    )
                }
            }

            if (point.boardExamTip.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 Board Tip: ${point.boardExamTip}",
                    fontSize = 11.sp,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PracticeQuestionCard(
    question: com.example.data.model.ExamQuestion,
    onAskAiHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showSolution = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(IndigoContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${question.yearTag} • ${question.marks} Marks",
                        color = IndigoDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = question.chapterName,
                    color = Slate500,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = question.questionText,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal
            )

            if (question.instructions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = question.instructions,
                    color = Slate500,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.OutlinedButton(
                    onClick = { showSolution.value = !showSolution.value },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (showSolution.value) "Hide Solution" else "View Solution Outline",
                        fontSize = 11.sp
                    )
                }

                androidx.compose.material3.Button(
                    onClick = onAskAiHelp,
                    shape = RoundedCornerShape(10.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = IndigoPrimary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("🤖 Ask AI Tutor", fontSize = 11.sp, color = Color.White)
                }
            }

            if (showSolution.value) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "Model Solution / Explanation:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = IndigoDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.modelAnswerExplanation.ifEmpty { "Detailed step-by-step working notes as per board syllabus." },
                            fontSize = 11.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )

                        if (question.markingRubricSteps.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Marking Scheme Steps:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = EmeraldGreen
                            )
                            question.markingRubricSteps.forEach { step ->
                                Text(
                                    text = "• $step",
                                    fontSize = 10.sp,
                                    color = Slate600
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

