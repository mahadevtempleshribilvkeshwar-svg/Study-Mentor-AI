package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolVerificationStatus
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ExamActiveScreen
import com.example.ui.screens.ExamReportScreen
import com.example.ui.screens.ExamsScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImprovementScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PyqScreen
import com.example.ui.screens.ResourcesScreen
import com.example.ui.screens.WorkspaceScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.CommerceViewModel

data class BottomNavItem(
    val destination: AppNavDestination,
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: CommerceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: CommerceViewModel) {
    val currentDest by viewModel.currentDestination.collectAsState()

    val bottomNavItems = listOf(
        BottomNavItem(AppNavDestination.HOME, "Home", Icons.Default.Home, "nav_home"),
        BottomNavItem(AppNavDestination.LEARN, "Learn", Icons.Default.School, "nav_learn"),
        BottomNavItem(AppNavDestination.PRACTICE, "Practice", Icons.Default.Create, "nav_practice"),
        BottomNavItem(AppNavDestination.TESTS, "Mocks", Icons.AutoMirrored.Filled.Assignment, "nav_tests"),
        BottomNavItem(AppNavDestination.AI_TUTOR, "AI Teacher", Icons.Default.AutoAwesome, "nav_ai_tutor"),
        BottomNavItem(AppNavDestination.LEADERBOARD, "School Hub", Icons.Default.EmojiEvents, "nav_leaderboard"),
        BottomNavItem(AppNavDestination.PROFILE, "Profile", Icons.Default.Person, "nav_profile")
    )

    val hideBottomBar = currentDest == AppNavDestination.ACTIVE_EXAM ||
            currentDest == AppNavDestination.ONBOARDING ||
            currentDest == AppNavDestination.AUTH

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!hideBottomBar) {
                androidx.compose.foundation.layout.Column {
                    androidx.compose.material3.HorizontalDivider(
                        thickness = 1.dp,
                        color = com.example.ui.theme.Slate200
                    )
                    NavigationBar(
                        containerColor = Color.White,
                        contentColor = com.example.ui.theme.TextPrimary,
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentDest == item.destination
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.destination) },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = com.example.ui.theme.IndigoPrimary,
                                    selectedTextColor = com.example.ui.theme.IndigoPrimary,
                                    indicatorColor = com.example.ui.theme.IndigoContainer,
                                    unselectedIconColor = com.example.ui.theme.Slate400,
                                    unselectedTextColor = com.example.ui.theme.Slate400
                                ),
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        val screenModifier = Modifier.padding(padding)

        when (currentDest) {
            AppNavDestination.ONBOARDING -> {
                OnboardingScreen(
                    onComplete = { country, state, district, area, boardCode, boardName, gradeId, gradeName, streamId, streamName, subjectIds, schId, schName, schArea, lang, targetPct ->
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
                            selectedSubjectIds = subjectIds,
                            schoolId = schId,
                            schoolName = schName,
                            schoolArea = schArea,
                            schoolVerificationStatus = SchoolVerificationStatus.UNVERIFIED,
                            preferredLanguage = lang,
                            targetPercentage = targetPct
                        )
                        viewModel.navigateTo(AppNavDestination.HOME)
                    },
                    modifier = screenModifier
                )
            }
            AppNavDestination.HOME -> HomeScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.LEARN -> LearnScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.PLAY -> GameScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.WORKSPACE -> WorkspaceScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.PRACTICE -> PracticeScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.PYQ -> PyqScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.TESTS -> ExamsScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.ACTIVE_EXAM -> ExamActiveScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.EXAM_REPORT -> ExamReportScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.AI_TUTOR -> AiTutorScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.RESOURCES -> ResourcesScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.IMPROVEMENT -> ImprovementScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.PROFILE -> ProfileScreen(viewModel = viewModel, modifier = screenModifier)
            AppNavDestination.AUTH -> AuthScreen(viewModel = viewModel, modifier = screenModifier)
        }
    }
}
