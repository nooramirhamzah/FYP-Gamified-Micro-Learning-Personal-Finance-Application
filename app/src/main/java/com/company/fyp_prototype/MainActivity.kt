package com.company.fyp_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.company.fyp_prototype.ui.BudgetLessonScreen
import com.company.fyp_prototype.ui.EmergencyFundsLessonScreen
import com.company.fyp_prototype.ui.EmergencyFundsQuizScreen
import com.company.fyp_prototype.ui.HighYieldQuizScreen
import com.company.fyp_prototype.ui.HighYieldSavingsLessonScreen
import com.company.fyp_prototype.ui.HomeScreen
import com.company.fyp_prototype.ui.IntroQuizScreen
import com.company.fyp_prototype.ui.LessonScreen
import com.company.fyp_prototype.ui.PortfolioScreen
import com.company.fyp_prototype.ui.ProfileScreen
import com.company.fyp_prototype.ui.QuizScreen
import com.company.fyp_prototype.ui.theme.FYP_PrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf("home") }

            FYP_PrototypeTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        "home" -> HomeScreen(
                            onLessonSelect = { lessonId ->
                                currentScreen = when (lessonId) {
                                    "budget" -> "budget_lesson"
                                    "emergency" -> "emergency_lesson"
                                    "high_yield" -> "high_yield_lesson"
                                    else -> "lesson"
                                }
                            },
                            onNavigate = { route -> currentScreen = route }
                        )
                        "lesson" -> LessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "intro_quiz" }
                        )
                        "intro_quiz" -> IntroQuizScreen(
                            onBack = { currentScreen = "lesson" },
                            onFinish = { currentScreen = "home" }
                        )
                        "quiz" -> QuizScreen(
                            onBack = { currentScreen = "budget_lesson" },
                            onComplete = { currentScreen = "home" }
                        )
                        "budget_lesson" -> BudgetLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "quiz" }
                        )
                        "emergency_lesson" -> EmergencyFundsLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "emergency_quiz" }
                        )
                        "emergency_quiz" -> EmergencyFundsQuizScreen(
                            onBack = { currentScreen = "emergency_lesson" },
                            onDone = { currentScreen = "home" }
                        )
                        "high_yield_lesson" -> HighYieldSavingsLessonScreen(
                            onBack = { currentScreen = "home" },
                            onContinue = { currentScreen = "high_yield_quiz" }
                        )
                        "high_yield_quiz" -> HighYieldQuizScreen(
                            onBack = { currentScreen = "high_yield_lesson" },
                            onFinish = { currentScreen = "home" }
                        )
                        "portfolio" -> PortfolioScreen(
                            onBack = { currentScreen = "home" },
                            onNavigate = { route -> currentScreen = route }
                        )
                        "profile" -> ProfileScreen(
                            onBack = { currentScreen = "home" },
                            onNavigate = { route -> currentScreen = route }
                        )
                    }
                }
            }
        }
    }
}
