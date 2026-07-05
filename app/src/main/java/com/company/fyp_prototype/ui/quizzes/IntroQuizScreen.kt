package com.company.fyp_prototype.ui.quizzes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
fun IntroQuizScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var heartsRemaining by remember { mutableIntStateOf(5) }
    var hasAnswered by remember { mutableStateOf(false) }
    var isOutOfHearts by remember { mutableStateOf(false) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizSaved by remember { mutableStateOf(false) }

    val coins by userViewModel.coins.collectAsState()
    val totalQuestions = introQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(introQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isOutOfHearts) {
        OutOfHeartsState(onReturnToLesson = onBack)
    } else if (isQuizFinished) {
        IntroQuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            reward = if (score >= 5) userViewModel.calculateActiveLessonReward(score, totalQuestions) else 0,
            onFinish = onFinish
        )
    } else {
        val currentQuestion = introQuestions[safeQuestionIndex]

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                IntroQuizTopBar(
                    onBack = onBack,
                    currentIndex = safeQuestionIndex + 1,
                    total = totalQuestions,
                    coins = coins,
                    heartsRemaining = heartsRemaining
                )
            },
            bottomBar = {
                Box(modifier = Modifier.padding(24.dp)) {
                    Button(
                        onClick = {
                            if (heartsRemaining == 0) {
                                isOutOfHearts = true
                            } else if (isLastQuestion) {
                                if (score >= 5 && !isQuizSaved) {
                                    userViewModel.completeQuiz("intro", score, totalQuestions)
                                    isQuizSaved = true
                                }
                                isQuizFinished = true
                            } else {
                                currentQuestionIndex = (safeQuestionIndex + 1).coerceAtMost(9)
                                selectedOption = null
                                hasAnswered = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasAnswered) PrimaryGreen else Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        enabled = hasAnswered
                    ) {
                        Text(
                            text = when {
                                heartsRemaining == 0 -> "Review Lesson"
                                isLastQuestion -> "Finish Quiz"
                                else -> "Next Question"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasAnswered) Color.White else Color.Gray
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0B2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        currentQuestion.icon,
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    QuizOptionItem(
                        label = ('A' + index).toString(),
                        text = option,
                        isSelected = selectedOption == index,
                        isCorrect = index == currentQuestion.correctOptionIndex,
                        isRevealed = hasAnswered,
                        onClick = {
                            if (!hasAnswered) {
                                val answeredCorrectly = index == currentQuestion.correctOptionIndex
                                selectedOption = index
                                score += if (answeredCorrectly) 1 else 0
                                heartsRemaining = if (answeredCorrectly) heartsRemaining else (heartsRemaining - 1).coerceAtLeast(0)
                                hasAnswered = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun IntroQuizTopBar(
    onBack: () -> Unit,
    currentIndex: Int,
    total: Int,
    coins: Int,
    heartsRemaining: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((currentIndex.toFloat() / total.toFloat()).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(heartsRemaining.toString(), color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "QUESTION $currentIndex OF $total",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF9C4),
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = coins.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFFFFA000)
                    )
                }
            }
        }
    }
}

@Composable
fun IntroQuizResultsState(
    score: Int,
    totalQuestions: Int,
    reward: Int,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val passed = score >= 5

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.EmojiEvents,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (passed) "Money Basics Complete!" else "Try Again",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You scored $score/$totalQuestions!",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (passed) "You earned $reward coins!" else "Score at least 5/10 to unlock the next lesson.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (passed) PrimaryGreen else Color.Gray,
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

@Composable
fun QuizOptionItem(
    label: String,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isRevealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWrongSelection = isRevealed && isSelected && !isCorrect
    val borderColor = when {
        isRevealed && isCorrect -> PrimaryGreen
        isWrongSelection -> Color.Red
        isSelected -> PrimaryGreen
        else -> Color(0xFFEEEEEE)
    }
    val circleColor = when {
        isRevealed && isCorrect -> PrimaryGreen
        isWrongSelection -> Color.Red
        isSelected -> PrimaryGreen
        else -> Color.Transparent
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isRevealed, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        shadowElevation = if (isSelected || (isRevealed && isCorrect)) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(circleColor)
                    .border(1.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    color = if (circleColor == Color.Transparent) Color.Gray else Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            RadioButton(
                selected = isSelected || (isRevealed && isCorrect),
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryGreen,
                    unselectedColor = Color(0xFFEEEEEE)
                )
            )
        }
    }
}

data class IntroQuestion(
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val icon: ImageVector
)

private val introQuestions = arrayOf(
    IntroQuestion(
        "What is the main purpose of money in everyday life?",
        listOf("A medium of exchange", "A way to make friends", "A type of exercise", "A decoration"),
        0,
        Icons.Default.Payments
    ),
    IntroQuestion(
        "Which example is income?",
        listOf("Buying lunch", "Monthly salary", "Paying rent", "A phone bill"),
        1,
        Icons.Default.Work
    ),
    IntroQuestion(
        "Which choice is a need rather than a want?",
        listOf("Movie tickets", "Rent payment", "Designer shoes", "Game upgrade"),
        1,
        Icons.Default.Home
    ),
    IntroQuestion(
        "What does saving money mean?",
        listOf("Spending it immediately", "Setting it aside for later", "Losing track of it", "Only using coins"),
        1,
        Icons.Default.Savings
    ),
    IntroQuestion(
        "Why is tracking spending useful?",
        listOf("It shows where money goes", "It removes all bills", "It creates free money", "It replaces saving"),
        0,
        Icons.Default.Receipt
    ),
    IntroQuestion(
        "What is a budget?",
        listOf("A plan for money", "A bank password", "A shopping reward", "A type of loan"),
        0,
        Icons.Default.PieChart
    ),
    IntroQuestion(
        "Which habit helps build financial confidence?",
        listOf("Ignoring balances", "Checking money regularly", "Spending every bonus", "Borrowing for wants"),
        1,
        Icons.Default.CheckCircle
    ),
    IntroQuestion(
        "What is an expense?",
        listOf("Money you earn", "Money you spend", "Money interest pays you", "Money a bank stores"),
        1,
        Icons.Default.ShoppingCart
    ),
    IntroQuestion(
        "Why should financial goals be specific?",
        listOf("They are easier to measure", "They become free", "They never change", "They skip budgeting"),
        0,
        Icons.Default.Star
    ),
    IntroQuestion(
        "Which is a smart first step after receiving money?",
        listOf("Spend all of it", "Plan how to use it", "Hide every receipt", "Ignore your balance"),
        1,
        Icons.Default.Lightbulb
    )
)

@Preview(showBackground = true)
@Composable
fun IntroQuizScreenPreview() {
    FYP_PrototypeTheme {
        IntroQuizResultsState(score = 8, totalQuestions = 10, reward = 85, onFinish = {})
    }
}
