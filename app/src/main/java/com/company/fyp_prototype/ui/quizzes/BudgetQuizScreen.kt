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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int
)

@Composable
fun BudgetQuizScreen(
    userViewModel: UserViewModel,
    lessonId: String = "budget",
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
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
    val totalQuestions = budgetQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(budgetQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isOutOfHearts) {
        OutOfHeartsState(onReturnToLesson = onBack)
    } else if (isQuizFinished) {
        QuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            reward = if (score >= 5) userViewModel.calculateActiveLessonReward(score, totalQuestions) else 0,
            onFinish = onComplete
        )
    } else {
        val currentQuestion = budgetQuestions[safeQuestionIndex]
        val progress = (safeQuestionIndex + 1).toFloat() / totalQuestions

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                QuizTopBar(
                    onBack = onBack,
                    progress = progress,
                    coins = coins,
                    currentIndex = safeQuestionIndex + 1,
                    total = totalQuestions,
                    heartsRemaining = heartsRemaining
                )
            },
            bottomBar = {
                QuizBottomBar(
                    isLastQuestion = isLastQuestion,
                    hasAnswered = hasAnswered,
                    isOutOfHearts = heartsRemaining == 0,
                    onNext = {
                        if (heartsRemaining == 0) {
                            isOutOfHearts = true
                        } else if (isLastQuestion) {
                            if (score >= 5 && !isQuizSaved) {
                                userViewModel.completeQuiz(lessonId, score, totalQuestions)
                                isQuizSaved = true
                            }
                            isQuizFinished = true
                        } else {
                            currentQuestionIndex = (safeQuestionIndex + 1).coerceAtMost(9)
                            selectedOption = null
                            hasAnswered = false
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF80CBC4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF0F0F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = currentQuestion.text,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Select the correct answer:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    QuizOptionCard(
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
fun QuizTopBar(
    onBack: () -> Unit,
    progress: Float,
    coins: Int,
    currentIndex: Int,
    total: Int,
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
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
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
                text = "QUESTION $currentIndex OF $total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
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
fun QuizBottomBar(
    isLastQuestion: Boolean,
    hasAnswered: Boolean,
    isOutOfHearts: Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onNext,
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
                when {
                    isOutOfHearts -> "REVIEW LESSON"
                    isLastQuestion -> "FINISH QUIZ"
                    else -> "NEXT"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (hasAnswered) Color.White else Color.Gray
            )
        }
    }
}

@Composable
fun OutOfHeartsState(
    onReturnToLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Out of Hearts",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Read the lesson again, then come back and try the quiz once more.",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onReturnToLesson,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Return to Lesson", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun QuizResultsState(
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
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(SecondaryGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (passed) "Quiz Completed!" else "Try Again",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You scored $score/$totalQuestions!",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (passed) "You earned $reward coins!" else "Score at least 5/10 to unlock the next lesson.",
            fontSize = 24.sp,
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
            Text(
                "RETURN TO HOME",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuizOptionCard(
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
        else -> Color(0xFFF0F0F0)
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
        shadowElevation = if (isSelected || (isRevealed && isCorrect)) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, borderColor, CircleShape)
                    .background(circleColor, CircleShape),
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
                    unselectedColor = Color(0xFFF0F0F0)
                )
            )
        }
    }
}

private val budgetQuestions = arrayOf(
    Question(1, "What is the goal of the 50/30/20 rule?", listOf("Split income into needs, wants, and savings", "Spend half on shopping", "Avoid all entertainment", "Track only cash spending"), 0),
    Question(2, "In the 50/30/20 rule, what does the 50% category usually cover?", listOf("Needs", "Wants", "Investing only", "Holiday gifts"), 0),
    Question(3, "Which expense is usually a need?", listOf("Streaming upgrade", "Rent", "Concert tickets", "Designer bag"), 1),
    Question(4, "Which expense is usually a want?", listOf("Groceries", "Electric bill", "Movie night", "Minimum debt payment"), 2),
    Question(5, "What does the 20% category encourage you to prioritize?", listOf("Savings and debt repayment", "Restaurants", "New gadgets", "Extra subscriptions"), 0),
    Question(6, "If your monthly income is $2,000, about how much is 20%?", listOf("$100", "$200", "$400", "$1,000"), 2),
    Question(7, "Why should you review your budget regularly?", listOf("Expenses and goals can change", "Budgets expire daily", "It removes bills", "It guarantees a raise"), 0),
    Question(8, "What should you do if needs are more than 50% of income?", listOf("Ignore the budget", "Look for cost cuts or income changes", "Stop saving forever", "Move wants into needs"), 1),
    Question(9, "Which tool helps you understand where your money goes?", listOf("Spending tracker", "Random guessing", "Deleting receipts", "Only checking once a year"), 0),
    Question(10, "What makes a budget realistic?", listOf("It matches your actual income and expenses", "It assumes no emergencies", "It bans all wants", "It never changes"), 0)
)

@Preview(showBackground = true)
@Composable
fun BudgetQuizScreenPreview() {
    FYP_PrototypeTheme {
        QuizResultsState(score = 8, totalQuestions = 10, reward = 85, onFinish = {})
    }
}
