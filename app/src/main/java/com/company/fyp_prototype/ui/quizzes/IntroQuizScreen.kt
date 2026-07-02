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
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizSaved by remember { mutableStateOf(false) }

    val coins by userViewModel.coins.collectAsState()
    val totalQuestions = introQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(introQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isQuizFinished) {
        IntroQuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            reward = userViewModel.calculateLessonReward(score, totalQuestions),
            onFinish = onFinish
        )
    } else {
        val currentQuestion = introQuestions[safeQuestionIndex]

        Scaffold(
            containerColor = BackgroundWhite,
            topBar = {
                IntroQuizTopBar(
                    onBack = onBack,
                    currentIndex = safeQuestionIndex + 1,
                    total = totalQuestions,
                    coins = coins
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
                                    userViewModel.completeQuiz("intro", updatedScore, totalQuestions)
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
                        color = TextDark,
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
                        onClick = { selectedOption = index }
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
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
                        .fillMaxWidth(currentIndex.toFloat() / total)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("5", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                color = Color.Gray,
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
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
            text = "Money Basics Complete!",
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You earned $reward coins!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = if (isSelected) PrimaryGreen else Color(0xFFEEEEEE)
        ),
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) PrimaryGreen else Color.Transparent)
                    .border(1.dp, if (isSelected) PrimaryGreen else Color(0xFFEEEEEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            RadioButton(
                selected = isSelected,
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
