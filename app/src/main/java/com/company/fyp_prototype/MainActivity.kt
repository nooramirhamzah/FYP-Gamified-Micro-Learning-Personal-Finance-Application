package com.company.fyp_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.company.fyp_prototype.data.local.AppDatabase
import com.company.fyp_prototype.ui.home.HomeScreen
import com.company.fyp_prototype.ui.lessons.BudgetLessonScreen
import com.company.fyp_prototype.ui.lessons.EmergencyFundsLessonScreen
import com.company.fyp_prototype.ui.lessons.HighYieldSavingsLessonScreen
import com.company.fyp_prototype.ui.lessons.LessonScreen
import com.company.fyp_prototype.ui.onboarding.OnboardingScreen
import com.company.fyp_prototype.ui.profile.PortfolioScreen
import com.company.fyp_prototype.ui.profile.ProfileScreen
import com.company.fyp_prototype.ui.quizzes.EmergencyFundsQuizScreen
import com.company.fyp_prototype.ui.quizzes.HighYieldQuizScreen
import com.company.fyp_prototype.ui.quizzes.IntroQuizScreen
import com.company.fyp_prototype.ui.quizzes.BudgetQuizScreen
import com.company.fyp_prototype.ui.theme.FYP_PrototypeTheme
import com.company.fyp_prototype.ui.viewmodel.UserViewModel
import com.company.fyp_prototype.ui.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(database.userDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val hasCompletedOnboarding by userViewModel.hasCompletedOnboarding.collectAsState()
            val activeRewards by userViewModel.activeRewards.collectAsState()
            val isDarkModeActive = "dark_mode_theme" in activeRewards
            var currentScreen by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(hasCompletedOnboarding) {
                if (
                    currentScreen == null ||
                    (!hasCompletedOnboarding && currentScreen == "home") ||
                    (hasCompletedOnboarding && currentScreen == "onboarding")
                ) {
                    currentScreen = if (hasCompletedOnboarding) "home" else "onboarding"
                }
            }

            FYP_PrototypeTheme(
                darkTheme = isDarkModeActive,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen ?: "onboarding") {
                        "onboarding" -> OnboardingScreen(
                            userViewModel = userViewModel,
                            onComplete = { currentScreen = "home" }
                        )
                        "home" -> HomeScreen(
                            userViewModel = userViewModel,
                            onLessonSelect = { lessonId ->
                                currentScreen = when (lessonId) {
                                    "intro" -> "intro_lesson"
                                    "budget" -> "budget_lesson"
                                    "emergency" -> "emergency_lesson"
                                    "high_yield" -> "high_yield_lesson"
                                    else -> "home"
                                }
                            },
                            onNavigate = { route -> currentScreen = route }
                        )
                        "intro_lesson", "lesson" -> LessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "intro_quiz" }
                        )
                        "intro_quiz" -> IntroQuizScreen(
                            userViewModel = userViewModel,
                            onBack = { currentScreen = "intro_lesson" },
                            onFinish = { currentScreen = "home" }
                        )
                        "budget_quiz", "quiz" -> BudgetQuizScreen(
                            userViewModel = userViewModel,
                            lessonId = "budget",
                            onBack = { currentScreen = "budget_lesson" },
                            onComplete = { currentScreen = "home" }
                        )
                        "budget_lesson" -> BudgetLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "budget_quiz" }
                        )
                        "emergency_lesson" -> EmergencyFundsLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "emergency_quiz" }
                        )
                        "emergency_quiz" -> EmergencyFundsQuizScreen(
                            userViewModel = userViewModel,
                            onBack = { currentScreen = "emergency_lesson" },
                            onDone = { currentScreen = "home" }
                        )
                        "high_yield_lesson" -> HighYieldSavingsLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "high_yield_quiz" }
                        )
                        "high_yield_quiz" -> HighYieldQuizScreen(
                            userViewModel = userViewModel,
                            onBack = { currentScreen = "high_yield_lesson" },
                            onFinish = { currentScreen = "home" }
                        )
                        "portfolio" -> PortfolioScreen(
                            userViewModel = userViewModel,
                            onBack = { currentScreen = "home" },
                            onNavigate = { route -> currentScreen = route }
                        )
                        "profile" -> ProfileScreen(
                            userViewModel = userViewModel,
                            onBack = { currentScreen = "home" },
                            onNavigate = { route -> currentScreen = route }
                        )
                    }
                }
            }
        }
    }
}
