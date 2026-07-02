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
fun QuizScreen(
    userViewModel: UserViewModel,
    lessonId: String = "budget",
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizSaved by remember { mutableStateOf(false) }

    val coins by userViewModel.coins.collectAsState()
    val totalQuestions = budgetQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(budgetQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isQuizFinished) {
        QuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            reward = userViewModel.calculateLessonReward(score, totalQuestions),
            onFinish = onComplete
        )
    } else {
        val currentQuestion = budgetQuestions[safeQuestionIndex]
        val progress = (safeQuestionIndex + 1).toFloat() / totalQuestions

        Scaffold(
            containerColor = Color.White,
            topBar = {
                QuizTopBar(
                    onBack = onBack,
                    progress = progress,
                    coins = coins,
                    currentIndex = safeQuestionIndex + 1,
                    total = totalQuestions
                )
            },
            bottomBar = {
                QuizBottomBar(
                    selectedOption = selectedOption,
                    isLastQuestion = isLastQuestion,
                    onNext = {
                        val answeredCorrectly = selectedOption == currentQuestion.correctOptionIndex
                        val updatedScore = score + if (answeredCorrectly) 1 else 0
                        score = updatedScore

                        if (isLastQuestion) {
                            if (!isQuizSaved) {
                                userViewModel.completeQuiz(lessonId, updatedScore, totalQuestions)
                                isQuizSaved = true
                            }
                            isQuizFinished = true
                        } else {
                            currentQuestionIndex = (safeQuestionIndex + 1).coerceAtMost(9)
                            selectedOption = null
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
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
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF0F0F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = currentQuestion.text,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Select the correct answer:",
                    color = TextGray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    QuizOptionCard(
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
fun QuizTopBar(
    onBack: () -> Unit,
    progress: Float,
    coins: Int,
    currentIndex: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
        }

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "QUESTION $currentIndex OF $total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F0F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
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
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = coins.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PrimaryGreen
                )
            }
        }
    }
}

@Composable
fun QuizBottomBar(
    selectedOption: Int?,
    isLastQuestion: Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EAF6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .weight(1f)
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedOption != null) PrimaryGreen else Color(0xFFE0E0E0)
            ),
            shape = RoundedCornerShape(24.dp),
            enabled = selectedOption != null
        ) {
            Text(
                if (isLastQuestion) "FINISH QUIZ" else "NEXT",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (selectedOption != null) Color.White else Color.Gray
            )
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
            text = "Quiz Completed!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You scored $score/$totalQuestions!",
            fontSize = 20.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You earned $reward coins!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
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
            color = if (isSelected) PrimaryGreen else Color(0xFFF0F0F0)
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, if (isSelected) PrimaryGreen else Color(0xFFF0F0F0), CircleShape)
                    .background(if (isSelected) PrimaryGreen else Color.Transparent, CircleShape),
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
fun QuizScreenPreview() {
    FYP_PrototypeTheme {
        QuizResultsState(score = 8, totalQuestions = 10, reward = 85, onFinish = {})
    }
}
