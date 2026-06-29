package com.company.fyp_prototype.ui.quizzes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

@Composable
fun HighYieldQuizScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizSaved by remember { mutableStateOf(false) }

    val coins by userViewModel.coins.collectAsState()
    val totalQuestions = highYieldQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(highYieldQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isQuizFinished) {
        HighYieldQuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            onFinish = onFinish
        )
    } else {
        val currentQuestion = highYieldQuestions[safeQuestionIndex]

        Scaffold(
            containerColor = BackgroundWhite,
            topBar = {
                HighYieldQuizTopBar(
                    onBack = onBack,
                    currentIndex = safeQuestionIndex + 1,
                    total = totalQuestions
                )
            },
            bottomBar = {
                Box(modifier = Modifier.padding(24.dp)) {
                    Button(
                        onClick = {
                            val answeredCorrectly = selectedOption == currentQuestion.correctOptionIndex
                            val updatedScore = score + if (answeredCorrectly) 1 else 0
                            score = updatedScore

                            if (isLastQuestion) {
                                if (!isQuizSaved) {
                                    userViewModel.completeQuiz("high_yield", updatedScore)
                                    isQuizSaved = true
                                }
                                isQuizFinished = true
                            } else {
                                currentQuestionIndex = (safeQuestionIndex + 1).coerceAtMost(9)
                                selectedOption = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedOption != null) PrimaryGreen else Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        enabled = selectedOption != null
                    ) {
                        Text(
                            text = if (isLastQuestion) "Finish Quiz" else "Next Question",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedOption != null) Color.White else Color.Gray
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF9C4),
                        shadowElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("$", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$coins coins",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFFFA000)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark,
                        textAlign = TextAlign.Start
                    ),
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    currentQuestion.options.chunked(2).forEachIndexed { rowIndex, rowOptions ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            rowOptions.forEachIndexed { optionIndex, option ->
                                val absoluteIndex = rowIndex * 2 + optionIndex
                                QuizGridOption(
                                    text = option.text,
                                    icon = option.icon,
                                    isSelected = selectedOption == absoluteIndex,
                                    onClick = { selectedOption = absoluteIndex },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowOptions.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HighYieldQuizTopBar(
    onBack: () -> Unit,
    currentIndex: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0).copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(total) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(if (index < currentIndex) PrimaryGreen else Color(0xFFE0E0E0))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            "$currentIndex/$total",
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HighYieldQuizResultsState(
    score: Int,
    totalQuestions: Int,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Savings Growth Unlocked!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You scored $score/$totalQuestions!",
            fontSize = 18.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Return to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

data class HighYieldOption(val text: String, val icon: ImageVector)
data class HighYieldQuestion(
    val text: String,
    val options: List<HighYieldOption>,
    val correctOptionIndex: Int
)

private val highYieldQuestions = arrayOf(
    HighYieldQuestion(
        "What makes a high-yield savings account different from a traditional savings account?",
        listOf(
            HighYieldOption("Higher interest rate", Icons.AutoMirrored.Filled.TrendingUp),
            HighYieldOption("No account number", Icons.Default.AccountBalance),
            HighYieldOption("Guaranteed stocks", Icons.AutoMirrored.Filled.ShowChart),
            HighYieldOption("Cash only deposits", Icons.Default.Payments)
        ),
        0
    ),
    HighYieldQuestion(
        "Which metric is most useful for comparing savings account earnings?",
        listOf(
            HighYieldOption("APR only", Icons.Default.Percent),
            HighYieldOption("APY", Icons.Default.Savings),
            HighYieldOption("ATM color", Icons.Default.CreditCard),
            HighYieldOption("Branch size", Icons.Default.Store)
        ),
        1
    ),
    HighYieldQuestion(
        "If two accounts have the same fees, which one usually grows money faster?",
        listOf(
            HighYieldOption("Lower APY", Icons.Default.South),
            HighYieldOption("Higher APY", Icons.AutoMirrored.Filled.TrendingUp),
            HighYieldOption("More paper mail", Icons.Default.Mail),
            HighYieldOption("Longer password", Icons.Default.Lock)
        ),
        1
    ),
    HighYieldQuestion(
        "Why is compound interest powerful in a high-yield savings account?",
        listOf(
            HighYieldOption("Interest earns interest", Icons.Default.Savings),
            HighYieldOption("It removes taxes", Icons.Default.Receipt),
            HighYieldOption("It prevents spending", Icons.Default.Block),
            HighYieldOption("It guarantees investing gains", Icons.Default.Verified)
        ),
        0
    ),
    HighYieldQuestion(
        "Which account feature should you check before opening a high-yield savings account?",
        listOf(
            HighYieldOption("Monthly fees", Icons.Default.Receipt),
            HighYieldOption("Logo shape", Icons.Default.Category),
            HighYieldOption("Card color", Icons.Default.Palette),
            HighYieldOption("App icon only", Icons.Default.Apps)
        ),
        0
    ),
    HighYieldQuestion(
        "What is a good use for a high-yield savings account?",
        listOf(
            HighYieldOption("Emergency fund", Icons.Default.Shield),
            HighYieldOption("Day trading", Icons.AutoMirrored.Filled.ShowChart),
            HighYieldOption("Lottery money", Icons.Default.Casino),
            HighYieldOption("Long-term stock replacement", Icons.Default.EmojiEvents)
        ),
        0
    ),
    HighYieldQuestion(
        "What can happen to a high-yield savings account's interest rate over time?",
        listOf(
            HighYieldOption("It can change", Icons.Default.History),
            HighYieldOption("It is always fixed forever", Icons.Default.Lock),
            HighYieldOption("It only goes up", Icons.AutoMirrored.Filled.TrendingUp),
            HighYieldOption("It becomes a stock", Icons.AutoMirrored.Filled.ShowChart)
        ),
        0
    ),
    HighYieldQuestion(
        "Why might an online bank offer a higher APY than a traditional branch bank?",
        listOf(
            HighYieldOption("Lower overhead costs", Icons.Default.Savings),
            HighYieldOption("No security rules", Icons.Default.Lock),
            HighYieldOption("No customer accounts", Icons.Default.PersonOff),
            HighYieldOption("Only cash deposits", Icons.Default.Payments)
        ),
        0
    ),
    HighYieldQuestion(
        "Which protection should you look for when choosing a savings account in the U.S.?",
        listOf(
            HighYieldOption("FDIC or NCUA insurance", Icons.Default.VerifiedUser),
            HighYieldOption("Crypto backing", Icons.Default.CurrencyBitcoin),
            HighYieldOption("Celebrity sponsor", Icons.Default.Star),
            HighYieldOption("No statements", Icons.Default.VisibilityOff)
        ),
        0
    ),
    HighYieldQuestion(
        "If a high-yield savings account pays 4% APY, what does that generally mean?",
        listOf(
            HighYieldOption("Approximate annual growth rate", Icons.Default.Percent),
            HighYieldOption("Monthly fee amount", Icons.Default.Receipt),
            HighYieldOption("Guaranteed daily bonus", Icons.Default.CardGiftcard),
            HighYieldOption("Minimum credit score", Icons.Default.Star)
        ),
        0
    )
)

@Preview(showBackground = true)
@Composable
fun HighYieldQuizScreenPreview() {
    FYP_PrototypeTheme {
        HighYieldQuizResultsState(score = 8, totalQuestions = 10, onFinish = {})
    }
}
