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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountingConcept
import com.example.data.model.ChapterModule
import com.example.data.model.CommerceSubject
import com.example.data.model.EducationBoard
import com.example.data.model.PriorityLevel
import com.example.data.repository.CurriculumRepository
import com.example.ui.components.ImportantPointItemCard
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBlue
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

@Composable
fun LearnScreen(
    viewModel: CommerceViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedChapterId by viewModel.selectedChapterId.collectAsState()
    val highlightImportantOnly by viewModel.highlightImportantOnly.collectAsState()

    val board = userProfile?.board ?: EducationBoard.CBSE
    val subject = userProfile?.selectedSubject ?: CommerceSubject.ACCOUNTANCY
    val curriculum = CurriculumRepository.getCurriculumForBoard(board, subject)

    val activeChapter = curriculum.find { it.id == selectedChapterId } ?: curriculum.firstOrNull()

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
                            text = "LEARN MODE • ${subject.displayName}",
                            color = com.example.ui.theme.IndigoPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Concept → Example → Demo → Practice → Board Ready",
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

            // Chapter Selector Horizontal Strip
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(curriculum) { ch ->
                        val isSelected = ch.id == activeChapter?.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.selectChapter(ch.id) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("select_chapter_${ch.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Ch ${ch.chapterNumber}: ${ch.title.take(18)}...",
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            if (activeChapter != null) {
                // Chapter Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "CHAPTER ${activeChapter.chapterNumber}",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Weightage: ${activeChapter.weightageMarks}",
                                        color = GoldAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = activeChapter.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = activeChapter.summary,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Important Points Filter / Highlight Toggle
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Core Exam Points & Formulas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyDark
                        )

                        FilterChip(
                            selected = highlightImportantOnly,
                            onClick = { viewModel.toggleHighlightImportantOnly() },
                            label = { Text("🔥 VERY IMPORTANT ONLY", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFEE2E2),
                                selectedLabelColor = Color(0xFFDC2626)
                            )
                        )
                    }
                }

                // Important points list
                val filteredPoints = if (highlightImportantOnly) {
                    activeChapter.importantPoints.filter { it.priority == PriorityLevel.VERY_IMPORTANT }
                } else {
                    activeChapter.importantPoints
                }

                items(filteredPoints) { point ->
                    ImportantPointItemCard(point = point)
                }

                // Detailed Concepts Section
                item {
                    Text(
                        text = "Detailed Concepts & 7-Step Understanding",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyDark
                    )
                }

                items(activeChapter.concepts) { concept ->
                    ConceptDetailCard(
                        concept = concept,
                        onAskAiTutor = {
                            viewModel.askAiTutor(
                                "Explain ${concept.title} with 7-step understanding: Concept, Rule, Reason, Logic, Example, Similar example, and Practice question.",
                                activeChapter.title
                            )
                            viewModel.navigateTo(AppNavDestination.AI_TUTOR)
                        }
                    )
                }

                // Bottom Action: Go to Practice or Game
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppNavDestination.PLAY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("🎮 Play Simulation", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppNavDestination.WORKSPACE) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("✍️ Blank Workspace", fontSize = 12.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun ConceptDetailCard(
    concept: AccountingConcept,
    onAskAiTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = concept.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = concept.overview,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Key Rules Bullet points
            concept.keyRules.forEach { rule ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Bold, color = NavyPrimary)
                    Text(
                        text = rule,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }

            if (concept.formulaOrFormat.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "📐 Format/Formula:\n${concept.formulaOrFormat}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyPrimary
                    )
                }
            }

            if (concept.practicalExample.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 Practical Example: ${concept.practicalExample}",
                    fontSize = 11.sp,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            if (concept.commonPitfall.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠️ Common Pitfall: ${concept.commonPitfall}",
                    fontSize = 11.sp,
                    color = Color(0xFFB91C1C),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAskAiTutor,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI 7-Step Understanding Tutor",
                    color = NavyPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
