package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamCategory
import com.example.data.repository.BoardMentorRepository
import com.example.ui.components.AcademicHeaderCard
import com.example.ui.components.PhotoQuestionAnalysisDialog
import com.example.ui.components.TargetProgressCard
import com.example.ui.components.TodayMissionCard
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
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.SlateCanvas
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun HomeScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.studentProfile.collectAsState()
    val todayMission by viewModel.todayStudyMission.collectAsState()
    val photoResult by viewModel.photoAnalysisResult.collectAsState()
    val isAiLoading by viewModel.aiTutorLoading.collectAsState()
    val subjectTargets = viewModel.getSubjectTargets()

    var showPhotoDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateCanvas)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Academic Profile Header Card
        item {
            AcademicHeaderCard(
                profile = profile,
                onEditProfileClick = { viewModel.navigateTo(AppNavDestination.PROFILE) }
            )
        }

        // 2. Today's Study Mission Card (Checkable Tasks & XP)
        item {
            TodayMissionCard(
                mission = todayMission,
                onToggleTask = { taskId -> viewModel.toggleMissionTask(taskId) }
            )
        }

        // 3. Target Marks vs Practice Score & Subject Breakdown
        item {
            TargetProgressCard(
                targetPercentage = profile.targetPercentage,
                currentLevel = profile.currentPracticeLevel,
                subjectTargets = subjectTargets,
                onUpdateTargetClick = { viewModel.navigateTo(AppNavDestination.PROFILE) }
            )
        }

        // 4. Quick Action 2x2 Hub
        item {
            Text(
                text = "⚡ AI Learning Tools",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        emoji = "👨‍🏫",
                        title = "AI Teacher",
                        subtitle = "8 Pedagogical Modes",
                        containerColor = IndigoContainer,
                        accentColor = IndigoPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppNavDestination.AI_TUTOR) }
                    )
                    QuickActionCard(
                        emoji = "📊",
                        title = "PYQ Analysis",
                        subtitle = "5-10 Year Trends",
                        containerColor = GoldContainer,
                        accentColor = GoldPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppNavDestination.PYQ) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        emoji = "📸",
                        title = "Photo Solver",
                        subtitle = "Instant Concept Breakdown",
                        containerColor = EmeraldContainer,
                        accentColor = EmeraldPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { showPhotoDialog = true }
                    )
                    QuickActionCard(
                        emoji = "📝",
                        title = "Board Mock Exam",
                        subtitle = "Full 80M Blueprint",
                        containerColor = RoseContainer,
                        accentColor = RosePrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppNavDestination.TESTS) }
                    )
                }
            }
        }

        // 5. Weak Topics & Focus Alert
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🚨", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Weak Areas Needing Practice",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RoseContainer
                        ) {
                            Text(
                                text = "2 Topics",
                                color = RosePrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    WeakTopicRow(
                        topic = "Cash Flow from Operating Activities",
                        subject = "Accountancy",
                        mastery = 48,
                        onReviseClick = {
                            viewModel.sendTeacherChatMessage(
                                "Explain Cash Flow from Operating Activities adjustments in simple steps",
                                "Accountancy"
                            )
                            viewModel.navigateTo(AppNavDestination.AI_TUTOR)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WeakTopicRow(
                        topic = "Marketing Mix: Promotion vs Personal Selling",
                        subject = "Business Studies",
                        mastery = 62,
                        onReviseClick = {
                            viewModel.sendTeacherChatMessage(
                                "Explain the key differences between Advertising and Personal Selling",
                                "Business Studies"
                            )
                            viewModel.navigateTo(AppNavDestination.AI_TUTOR)
                        }
                    )
                }
            }
        }

        // 6. Curated Video Classes & Lectures
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎥 Curated Video Lectures",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.navigateTo(AppNavDestination.RESOURCES) }) {
                    Text("View All", fontSize = 12.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val videos = BoardMentorRepository.getCuratedVideos("Accountancy")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(videos) { video ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable { viewModel.navigateTo(AppNavDestination.RESOURCES) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = IndigoContainer
                                ) {
                                    Text(
                                        text = video.subject,
                                        color = IndigoPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(video.duration, color = Slate500, fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = video.topic,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "By ${video.teacherOrChannelName} • ${video.language}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
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
            onAnalyzeSample = { prompt -> viewModel.performPhotoQuestionAnalysis(prompt, "Accountancy") },
            onDismiss = { showPhotoDialog = false }
        )
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    title: String,
    subtitle: String,
    containerColor: Color,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = containerColor,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(emoji, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = TextSecondary, fontSize = 10.sp, maxLines = 1)
        }
    }
}

@Composable
private fun WeakTopicRow(
    topic: String,
    subject: String,
    mastery: Int,
    onReviseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Slate50)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(topic, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("$subject • Mastery: $mastery%", color = RosePrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
        OutlinedButton(
            onClick = onReviseClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            border = BorderStroke(1.dp, IndigoPrimary)
        ) {
            Text("Fix Doubt 🤖", fontSize = 11.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
        }
    }
}
