package com.example.ui.screens

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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.BoardMentorRepository
import com.example.ui.components.SchoolVerificationDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun LeaderboardScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.studentProfile.collectAsState()
    val selectedCategory by viewModel.selectedLeaderboardCategory.collectAsState()

    var showVerificationDialog by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("SCHOOL_HUB") } // SCHOOL_HUB, GLOBAL_SCHOOLS, BADGES

    val schoolLeaderboard = BoardMentorRepository.getSchoolLeaderboard(selectedCategory, profile.schoolName)
    val globalSchools = BoardMentorRepository.getGlobalSchoolRankings()
    val achievements = BoardMentorRepository.getAchievementsList()

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
                            Text("School Hub & Leaderboard", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Child-Safe & Privacy-First Academic Ranks", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    if (profile.schoolVerificationStatus != SchoolVerificationStatus.VERIFIED) {
                        Button(
                            onClick = { showVerificationDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verify Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Tabs: School Leaderboard | Inter-School Ranks | Badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LeaderboardTabItem(
                        title = "🏫 School Hub",
                        isSelected = activeTab == "SCHOOL_HUB",
                        onClick = { activeTab = "SCHOOL_HUB" },
                        modifier = Modifier.weight(1f)
                    )
                    LeaderboardTabItem(
                        title = "🌐 Inter-School",
                        isSelected = activeTab == "GLOBAL_SCHOOLS",
                        onClick = { activeTab = "GLOBAL_SCHOOLS" },
                        modifier = Modifier.weight(1f)
                    )
                    LeaderboardTabItem(
                        title = "🎖️ Badges",
                        isSelected = activeTab == "BADGES",
                        onClick = { activeTab = "BADGES" },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (activeTab) {
                "SCHOOL_HUB" -> {
                    item {
                        // School Information Card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Brush.linearGradient(listOf(NavyDark, IndigoDark)))
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = profile.schoolName,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${profile.district} • ${profile.boardDisplayName}",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "👥 142 Active Classmates • School Rank #4",
                                            color = GoldAccent,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (profile.schoolVerificationStatus == SchoolVerificationStatus.VERIFIED) EmeraldContainer else GoldContainer
                                    ) {
                                        Text(
                                            text = if (profile.schoolVerificationStatus == SchoolVerificationStatus.VERIFIED) "✓ Verified Hub" else "Pending Code",
                                            color = if (profile.schoolVerificationStatus == SchoolVerificationStatus.VERIFIED) EmeraldPrimary else GoldPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 6 Categories Filter
                    item {
                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LeaderboardCategory.entries.forEach { cat ->
                                val isSelected = cat == selectedCategory
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setLeaderboardCategory(cat) },
                                    label = { Text("${cat.emoji} ${cat.title}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    items(schoolLeaderboard) { entry ->
                        val isSelf = entry.isCurrentUser
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelf) IndigoContainer else Color.White
                            ),
                            border = if (isSelf) BorderStroke(1.dp, IndigoPrimary) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = when (entry.rank) {
                                            1 -> GoldContainer
                                            2 -> Slate200
                                            3 -> RoseContainer
                                            else -> Slate100
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = when (entry.rank) {
                                                    1 -> "🥇"
                                                    2 -> "🥈"
                                                    3 -> "🥉"
                                                    else -> "${entry.rank}"
                                                },
                                                fontSize = if (entry.rank <= 3) 14.sp else 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate800
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(entry.avatarEmoji, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = entry.anonymousHandle,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (isSelf) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("(You)", color = IndigoPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Text(
                                            text = "${entry.gradeClass} • ${entry.badgeTitle}",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Slate100
                                ) {
                                    Text(
                                        text = "${entry.scoreOrXp} ${selectedCategory.unit}",
                                        color = IndigoPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                "GLOBAL_SCHOOLS" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = IndigoContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("🌐 All-India Inter-School Academic Challenge", color = IndigoPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Aggregated school points only. Individual student profiles are kept strictly private.", color = Slate700, fontSize = 11.sp)
                            }
                        }
                    }

                    items(globalSchools) { sch ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = when (sch.rank) {
                                            1 -> GoldContainer
                                            2 -> Slate200
                                            3 -> RoseContainer
                                            else -> Slate100
                                        },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = when (sch.rank) {
                                                    1 -> "🥇"
                                                    2 -> "🥈"
                                                    3 -> "🥉"
                                                    else -> "#${sch.rank}"
                                                },
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(sch.schoolName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("${sch.district}, ${sch.state} • ${sch.activeStudentsCount} Students", color = TextSecondary, fontSize = 10.sp)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GoldContainer
                                ) {
                                    Text(
                                        text = "${sch.totalXpPoints} XP",
                                        color = GoldPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                "BADGES" -> {
                    items(achievements) { badge ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (badge.isUnlocked) Color.White else Slate100
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (badge.isUnlocked) GoldContainer else Slate200,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(badge.iconEmoji, fontSize = 20.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(badge.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(badge.description, color = TextSecondary, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(badge.category, color = IndigoPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (badge.isUnlocked) EmeraldContainer else Slate200
                                ) {
                                    Text(
                                        text = if (badge.isUnlocked) "✓ Unlocked" else "Locked 🔒",
                                        color = if (badge.isUnlocked) EmeraldPrimary else Slate600,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showVerificationDialog) {
        SchoolVerificationDialog(
            currentSchoolName = profile.schoolName,
            onVerifyCode = { code -> viewModel.verifySchoolWithCode(code) },
            onDismiss = { showVerificationDialog = false }
        )
    }
}

@Composable
private fun LeaderboardTabItem(
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
