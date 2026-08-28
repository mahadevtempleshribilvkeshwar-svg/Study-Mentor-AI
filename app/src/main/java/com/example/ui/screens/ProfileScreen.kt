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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.DefaultBoardsCatalog
import com.example.data.model.DefaultCountriesCatalog
import com.example.data.model.DefaultGradesCatalog
import com.example.data.model.DefaultSchoolsCatalog
import com.example.data.model.DefaultStreamsCatalog
import com.example.data.model.SchoolVerificationStatus
import com.example.ui.components.SchoolVerificationDialog
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
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
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
fun ProfileScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.studentProfile.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var showVerificationDialog by remember { mutableStateOf(false) }

    var country by remember(profile) { mutableStateOf(profile.country) }
    var state by remember(profile) { mutableStateOf(profile.state) }
    var district by remember(profile) { mutableStateOf(profile.district) }
    var area by remember(profile) { mutableStateOf(profile.areaLocality) }
    var boardCode by remember(profile) { mutableStateOf(profile.boardCode) }
    var boardName by remember(profile) { mutableStateOf(profile.boardDisplayName) }
    var gradeId by remember(profile) { mutableStateOf(profile.gradeClassId) }
    var gradeName by remember(profile) { mutableStateOf(profile.gradeClassDisplayName) }
    var streamId by remember(profile) { mutableStateOf(profile.streamId) }
    var streamName by remember(profile) { mutableStateOf(profile.streamDisplayName) }
    var schoolName by remember(profile) { mutableStateOf(profile.schoolName) }
    var schoolArea by remember(profile) { mutableStateOf(profile.schoolArea) }
    var targetPercentage by remember(profile) { mutableFloatStateOf(profile.targetPercentage.toFloat()) }
    var preferredLanguage by remember(profile) { mutableStateOf(profile.preferredLanguage) }

    var hideFromLeaderboard by remember { mutableStateOf(false) }
    var strictPrivacyMode by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SlateCanvas,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo(AppNavDestination.HOME) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text("Academic Profile & Settings", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Personalized Board Exam Configuration", color = TextSecondary, fontSize = 11.sp)
                    }
                }

                Button(
                    onClick = {
                        if (isEditing) {
                            viewModel.updateStudentAcademicProfile(
                                country = country,
                                state = state,
                                district = district,
                                area = area,
                                boardCode = boardCode,
                                boardName = boardName,
                                gradeClassId = gradeId,
                                gradeClassName = gradeName,
                                streamId = streamId,
                                streamName = streamName,
                                selectedSubjectIds = profile.selectedSubjectIds,
                                schoolId = profile.schoolId,
                                schoolName = schoolName,
                                schoolArea = schoolArea,
                                schoolVerificationStatus = profile.schoolVerificationStatus,
                                preferredLanguage = preferredLanguage,
                                targetPercentage = targetPercentage.toInt()
                            )
                            isEditing = false
                        } else {
                            isEditing = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) EmeraldPrimary else IndigoPrimary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isEditing) "Save Profile" else "Edit Setup", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
            // Identity Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(Brush.linearGradient(listOf(NavyDark, IndigoDark, IndigoPrimary)))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(profile.avatarEmoji, fontSize = 28.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(profile.anonymousHandle, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
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
                                Text("${profile.gradeClassDisplayName} • ${profile.boardDisplayName}", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                                Text("School: ${profile.schoolName}", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // School & Affiliation Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.School, contentDescription = null, tint = IndigoPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("School & Verification", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }

                            if (profile.schoolVerificationStatus == SchoolVerificationStatus.VERIFIED) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldContainer
                                ) {
                                    Text("✓ Verified Member", color = EmeraldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { showVerificationDialog = true },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Enter Code", fontSize = 11.sp, color = IndigoPrimary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        if (isEditing) {
                            OutlinedTextField(
                                value = schoolName,
                                onValueChange = { schoolName = it },
                                label = { Text("School Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = schoolArea,
                                onValueChange = { schoolArea = it },
                                label = { Text("School Area / Branch") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(profile.schoolName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("${profile.schoolArea}, ${profile.district}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Target Percentage Slider
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Target Board Exam Percentage", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("${targetPercentage.toInt()}%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = IndigoPrimary)
                        }

                        Slider(
                            value = targetPercentage,
                            onValueChange = { targetPercentage = it },
                            valueRange = 60f..99f,
                            steps = 38,
                            enabled = isEditing,
                            colors = SliderDefaults.colors(thumbColor = IndigoPrimary, activeTrackColor = IndigoPrimary)
                        )
                    }
                }
            }

            // Preferred Language
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("AI Tutor Language", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        val langs = listOf("English", "Hindi", "Hinglish", "Bengali", "Tamil", "Marathi")
                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            langs.forEach { lang ->
                                val isSel = lang == preferredLanguage
                                FilterChip(
                                    selected = isSel,
                                    onClick = { if (isEditing) preferredLanguage = lang },
                                    label = { Text(lang) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Child Safety & Privacy Controls
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Child Safety & Privacy Controls", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Strict Student Privacy Protection", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text("No GPS tracking, no public contact sharing, and minimal data collection.", color = TextSecondary, fontSize = 10.sp)
                            }
                            Switch(
                                checked = strictPrivacyMode,
                                onCheckedChange = { strictPrivacyMode = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Hide Handle from Public Leaderboards", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text("Display as 'Anonymous Scholar' in school and global rankings.", color = TextSecondary, fontSize = 10.sp)
                            }
                            Switch(
                                checked = hideFromLeaderboard,
                                onCheckedChange = { hideFromLeaderboard = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = IndigoPrimary)
                            )
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
