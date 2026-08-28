package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AiAnswerEvaluationResult
import com.example.data.model.DailyStudyMission
import com.example.data.model.GeneratedImportantQuestion
import com.example.data.model.PhotoAnalysisResult
import com.example.data.model.PyqChapterTrend
import com.example.data.model.SchoolVerificationStatus
import com.example.data.model.StudentAcademicProfile
import com.example.data.model.SubjectTargetItem
import com.example.data.model.TeacherPedagogicalMode
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoseContainer
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

// =========================================================================
// 1. ACADEMIC HEADER CARD
// =========================================================================

@Composable
fun AcademicHeaderCard(
    profile: StudentAcademicProfile,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("academic_header_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(NavyDark, IndigoDark, IndigoPrimary)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(profile.avatarEmoji, fontSize = 22.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.anonymousHandle,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = GoldAccent.copy(alpha = 0.3f),
                                    border = BorderStroke(1.dp, GoldAccent)
                                ) {
                                    Text(
                                        text = "${profile.country} 🇮🇳",
                                        color = GoldAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${profile.gradeClassDisplayName} • ${profile.boardDisplayName}",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Edit Profile / Pill Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.clickable { onEditProfileClick() }
                    ) {
                        Text(
                            text = "Edit Profile ⚙️",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // School Info Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "School",
                                tint = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = profile.schoolName,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (profile.schoolVerificationStatus == SchoolVerificationStatus.VERIFIED) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldPrimary.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, EmeraldPrimary)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Verified",
                                        color = EmeraldPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GoldAccent.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Unverified",
                                    color = GoldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Bar (Streak, XP, Target)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPill(
                        icon = "🔥",
                        label = "Streak",
                        value = "${profile.streakDays} Days"
                    )
                    StatPill(
                        icon = "⚡",
                        label = "XP Gained",
                        value = "${profile.totalXp} XP"
                    )
                    StatPill(
                        icon = "🎯",
                        label = "Target",
                        value = "${profile.targetPercentage}%"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(icon: String, label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp)
                Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// =========================================================================
// 2. TODAY'S STUDY MISSION CARD
// =========================================================================

@Composable
fun TodayMissionCard(
    mission: DailyStudyMission,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = mission.tasks.count { it.isDone }
    val progress = if (mission.tasks.isNotEmpty()) completedCount.toFloat() / mission.tasks.size else 0f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("today_mission_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = IndigoContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("⚡", fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Today's Study Mission",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${mission.targetMinutes} Mins Target • +${mission.totalXpReward} XP Reward",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (mission.isFullyCompleted) EmeraldContainer else IndigoContainer
                ) {
                    Text(
                        text = "$completedCount/${mission.tasks.size} Done",
                        color = if (mission.isFullyCompleted) EmeraldPrimary else IndigoPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progress >= 1f) EmeraldPrimary else IndigoPrimary,
                trackColor = Slate200
            )
            Spacer(modifier = Modifier.height(14.dp))

            mission.tasks.forEach { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (task.isDone) Slate50 else Color.White)
                        .clickable { onToggleTask(task.id) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { onToggleTask(task.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldPrimary,
                            uncheckedColor = Slate400
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            color = if (task.isDone) Slate600 else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = if (task.isDone) MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            ) else MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${task.description} • ${task.durationMinutes}m",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldContainer
                    ) {
                        Text(
                            text = "+${task.xpReward} XP",
                            color = GoldPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. TARGET PROGRESS & GAP CARD
// =========================================================================

@Composable
fun TargetProgressCard(
    targetPercentage: Int,
    currentLevel: Int,
    subjectTargets: List<SubjectTargetItem>,
    onUpdateTargetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("target_progress_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎯", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Target Marks vs Practice Score",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Target: $targetPercentage% • Current Estimated: $currentLevel%",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                TextButton(onClick = onUpdateTargetClick) {
                    Text("Adjust ✏️", fontSize = 12.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val gap = targetPercentage - currentLevel
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (gap <= 10) EmeraldContainer else GoldContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (gap <= 10) "🚀" else "📈", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (gap <= 5) "Excellent! You are within 5% of your board exam target."
                        else "You need +$gap% to hit your dream target. Focus on your 2 weak chapters!",
                        color = if (gap <= 10) EmeraldPrimary else GoldPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Subject-wise Target Breakdown:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            subjectTargets.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.iconEmoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.subjectName,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${item.currentEstimatedMark}/${item.targetMark}M",
                            color = Slate800,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${item.accuracyPercentage}%)",
                            color = if (item.accuracyPercentage >= 85) EmeraldPrimary else if (item.accuracyPercentage >= 70) GoldPrimary else RosePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 4. AI TEACHER 8-MODE SELECTOR & CHAT COMPONENT
// =========================================================================

@Composable
fun TeacherModeSelectorRow(
    selectedMode: TeacherPedagogicalMode,
    onSelectMode: (TeacherPedagogicalMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TeacherPedagogicalMode.entries.forEach { mode ->
            val isSelected = mode == selectedMode
            FilterChip(
                selected = isSelected,
                onClick = { onSelectMode(mode) },
                label = {
                    Text(
                        text = "${mode.emoji} ${mode.title}",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IndigoPrimary,
                    selectedLabelColor = Color.White,
                    containerColor = Slate100,
                    labelColor = Slate700
                ),
                border = null,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

// =========================================================================
// 5. PHOTO QUESTION ANALYSIS DIALOG
// =========================================================================

@Composable
fun PhotoQuestionAnalysisDialog(
    result: PhotoAnalysisResult?,
    isLoading: Boolean,
    onAnalyzeSample: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customQuestionInput by remember { mutableStateOf("") }
    var selectedSample by remember { mutableStateOf("Admission of Partner numerical: Revaluation of plant (+₹20,000) & Bad debts provision (₹3,000)") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📸", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Photo & Problem Solver",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "AI Multimodal Concept Tutor",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = customQuestionInput,
                    onValueChange = { customQuestionInput = it },
                    placeholder = { Text("Paste textbook question or diagram description...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val q = customQuestionInput.ifBlank { selectedSample }
                            onAnalyzeSample(q)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            Text("Analyzing Question...", fontSize = 12.sp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyze Problem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // If Result is ready, display structured breakdown
                result?.let { res ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate50, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IndigoContainer
                        ) {
                            Text(
                                text = "${res.subject} • ${res.topic}",
                                color = IndigoPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💡 Underlying Concept:", color = Slate800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(res.conceptualExplanation, color = Slate700, fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("📝 Step-by-Step Resolution:", color = Slate800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        res.stepByStepSolution.forEach { step ->
                            Text("• $step", color = Slate700, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("⚠️ Common Examiner Trap:", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(res.commonMistakeWarning, color = Slate800, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 6. AI ANSWER EVALUATION DIALOG
// =========================================================================

@Composable
fun AnswerEvaluationDialog(
    evaluation: AiAnswerEvaluationResult?,
    isLoading: Boolean,
    onEvaluateClick: (String, Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var questionText by remember { mutableStateOf("Explain the accounting treatment of Goodwill when a new partner brings cash for goodwill premium. (4 Marks)") }
    var studentAnswer by remember { mutableStateOf("Cash A/c Dr. To Premium for Goodwill A/c. Then Premium for Goodwill is distributed to old partners in sacrificing ratio.") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📝", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("AI Answer Evaluation", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Formative Board-Style Assessment", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Question:", color = Slate700, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Text(questionText, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = studentAnswer,
                    onValueChange = { studentAnswer = it },
                    placeholder = { Text("Write or paste your answer...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onEvaluateClick(questionText, 4.0, studentAnswer) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        Text("Evaluating with AI...", fontSize = 12.sp)
                    } else {
                        Text("Evaluate Answer (4 Marks)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                evaluation?.let { eval ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate50, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Score: ${eval.estimatedScore} / ${eval.maxMarks} Marks",
                                color = IndigoPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldContainer
                            ) {
                                Text(
                                    text = "Relevance: ${eval.relevanceScore}/10",
                                    color = EmeraldPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("✅ Actionable Improvements:", color = Slate800, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        eval.actionableImprovements.forEach { imp ->
                            Text("• $imp", color = Slate700, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("🎯 Stronger Model Answer Outline:", color = Slate800, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(eval.strongerModelAnswer, color = Slate700, fontSize = 10.sp)

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = eval.aiDisclaimer,
                            color = Slate400,
                            fontSize = 9.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 7. SCHOOL VERIFICATION DIALOG
// =========================================================================

@Composable
fun SchoolVerificationDialog(
    currentSchoolName: String,
    onVerifyCode: (String) -> Boolean,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    var verificationResult by remember { mutableStateOf<Boolean?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = IndigoPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verify School Affiliation", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Unlock your school's private hub, official class challenges, and earn 100 XP!",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "School: $currentSchoolName",
                        color = Slate800,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = codeInput,
                    onValueChange = {
                        codeInput = it
                        verificationResult = null
                    },
                    label = { Text("6-Digit School Verification Code") },
                    placeholder = { Text("e.g. DPS2026 or BM8821") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                verificationResult?.let { isValid ->
                    if (isValid) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎉 School Verified Successfully! Welcome to your school's private learning hub.",
                                color = EmeraldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RoseContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ Invalid School Code. Try 'DPS2026' or 'BM8821' for test verification.",
                                color = RosePrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        val valid = onVerifyCode(codeInput)
                        verificationResult = valid
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Verify & Claim 100 XP", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
