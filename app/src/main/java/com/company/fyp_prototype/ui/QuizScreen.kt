package com.company.fyp_prototype.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

val mockQuestions = listOf(
    Question(1, "What is the 50/30/20 rule?", listOf("Needs, Wants, Savings", "Rent, Food, Fun", "Work, Sleep, Play"), 0),
    Question(2, "What does 'APY' stand for?", listOf("Annual Percentage Yield", "Actual Price Yearly", "Asset Profit Yield"), 0),
    Question(3, "Which is a liability?", listOf("Real Estate", "Car Loan", "Stock Portfolio"), 1),
    Question(4, "What is an Emergency Fund?", listOf("Money for vacations", "Savings for unexpected costs", "A retirement account"), 1),
    Question(5, "What is inflation?", listOf("Decrease in prices", "Increase in buying power", "Decrease in buying power"), 2),
    Question(6, "Which interest is better for savers?", listOf("Compound Interest", "Simple Interest", "Variable Interest"), 0),
    Question(7, "What is a 'FICO' score used for?", listOf("Job applications", "Creditworthiness", "Insurance premiums"), 1),
    Question(8, "What is diversification?", listOf("Investing in one stock", "Spreading investments", "Saving in a jar"), 1),
    Question(9, "What is a bull market?", listOf("Prices are falling", "Prices are rising", "Market is closed"), 1),
    Question(10, "What is a bond?", listOf("Ownership in a company", "A loan to an entity", "A type of insurance"), 1)
)

@Composable
fun QuizScreen(
    userViewModel: UserViewModel? = null,
    lessonId: String = "budget",
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }

    val coins by (userViewModel?.coins ?: remember { kotlinx.coroutines.flow.MutableStateFlow(150) }).collectAsState()

    if (isQuizFinished) {
        QuizResultsState(
            score = score,
            totalQuestions = mockQuestions.size,
            onFinish = {
                userViewModel?.addCoins(score * 10) // Award coins based on score
                userViewModel?.completeLesson(lessonId)
                onComplete()
            }
        )
    } else {
        val currentQuestion = mockQuestions[currentQuestionIndex]
        val progress = (currentQuestionIndex + 1).toFloat() / mockQuestions.size

        Scaffold(
            containerColor = Color.White,
            topBar = {
                QuizTopBar(
                    onBack = onBack,
                    progress = progress,
                    coins = coins,
                    currentIndex = currentQuestionIndex + 1,
                    total = mockQuestions.size
                )
            },
            bottomBar = {
                QuizBottomBar(
                    selectedOption = selectedOption,
                    isLastQuestion = currentQuestionIndex == mockQuestions.size - 1,
                    onNext = {
                        if (selectedOption == currentQuestion.correctOptionIndex) {
                            score++
                        }
                        
                        if (currentQuestionIndex < mockQuestions.size - 1) {
                            currentQuestionIndex++
                            selectedOption = null
                        } else {
                            isQuizFinished = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // Character and Question
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
                
                // Quiz Options
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
    total: Int
) {
    Row(
        modifier = Modifier
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
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
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
                if (isLastQuestion) "FINISH" else "NEXT",
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
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
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
            text = "You scored $score out of $totalQuestions",
            fontSize = 20.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "You earned ${score * 10} coins!",
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
                "CONTINUE",
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
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
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

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    FYP_PrototypeTheme {
        QuizScreen()
    }
}
