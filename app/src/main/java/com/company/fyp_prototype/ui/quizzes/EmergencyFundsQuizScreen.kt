package com.company.fyp_prototype.ui.quizzes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun EmergencyFundsQuizScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizSaved by remember { mutableStateOf(false) }

    val coins by userViewModel.coins.collectAsState()
    val totalQuestions = emergencyFundQuestions.size
    val safeQuestionIndex = currentQuestionIndex.coerceIn(emergencyFundQuestions.indices)
    val isLastQuestion = safeQuestionIndex == 9

    if (isQuizFinished) {
        EmergencyQuizResultsState(
            score = score,
            totalQuestions = totalQuestions,
            onFinish = onDone
        )
    } else {
        val currentQuestion = emergencyFundQuestions[safeQuestionIndex]

        Scaffold(
            containerColor = BackgroundWhite,
            topBar = {
                EmergencyQuizTopBar(
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
                                    userViewModel.completeQuiz("emergency", updatedScore)
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
                // Coin Badge
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
fun EmergencyQuizTopBar(
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
        
        // Segmented Progress Bar
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
fun EmergencyQuizResultsState(
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
            Icons.Default.Shield,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = PrimaryGreen
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Safety Net Secured!",
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

@Composable
fun QuizGridOption(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCorrect: Boolean = false
) {
    Surface(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = if (isSelected) PrimaryGreen else Color.Transparent
        ),
        shadowElevation = 2.dp
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            if (isSelected && isCorrect) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                )
            }
            
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) PrimaryGreen else Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

data class EmergencyOption(val text: String, val icon: ImageVector)
data class EmergencyQuestion(
    val text: String,
    val options: List<EmergencyOption>,
    val correctOptionIndex: Int
)

private val emergencyFundQuestions = arrayOf(
    EmergencyQuestion(
        "You just received a $50 bonus. What is the smartest move for your emergency fund?",
        listOf(
            EmergencyOption("Spend it on dinner", Icons.Default.Restaurant),
            EmergencyOption("Deposit immediately", Icons.Default.Savings),
            EmergencyOption("Invest in crypto", Icons.Default.CurrencyBitcoin),
            EmergencyOption("Buy a gift", Icons.Default.CardGiftcard)
        ),
        1
    ),
    EmergencyQuestion(
        "How many months of expenses should a basic emergency fund cover?",
        listOf(
            EmergencyOption("1 Month", Icons.Default.Event),
            EmergencyOption("3-6 Months", Icons.Default.CalendarMonth),
            EmergencyOption("12 Months", Icons.Default.History),
            EmergencyOption("None", Icons.Default.Close)
        ),
        1
    ),
    EmergencyQuestion(
        "Which of these is a valid reason to use your emergency fund?",
        listOf(
            EmergencyOption("New TV sale", Icons.Default.Tv),
            EmergencyOption("Sudden car repair", Icons.Default.DirectionsCar),
            EmergencyOption("Weekend trip", Icons.Default.Flight),
            EmergencyOption("Fancy clothes", Icons.Default.Checkroom)
        ),
        1
    ),
    EmergencyQuestion(
        "Where is the best place to keep an emergency fund?",
        listOf(
            EmergencyOption("Under mattress", Icons.Default.Bed),
            EmergencyOption("High-yield savings", Icons.AutoMirrored.Filled.TrendingUp),
            EmergencyOption("Stock market", Icons.AutoMirrored.Filled.ShowChart),
            EmergencyOption("Checking account", Icons.Default.CreditCard)
        ),
        1
    ),
    EmergencyQuestion(
        "What is the first step in building an emergency fund?",
        listOf(
            EmergencyOption("Calculate expenses", Icons.Default.Calculate),
            EmergencyOption("Quit your job", Icons.Default.WorkOff),
            EmergencyOption("Buy a safe", Icons.Default.Lock),
            EmergencyOption("Take a loan", Icons.Default.AccountBalance)
        ),
        0
    ),
    EmergencyQuestion(
        "What should you do after using money from your emergency fund?",
        listOf(
            EmergencyOption("Ignore it", Icons.Default.VisibilityOff),
            EmergencyOption("Refill it ASAP", Icons.Default.AddBusiness),
            EmergencyOption("Close the account", Icons.Default.Cancel),
            EmergencyOption("Spend the rest", Icons.Default.ShoppingCart)
        ),
        1
    ),
    EmergencyQuestion(
        "An emergency fund is meant to provide...",
        listOf(
            EmergencyOption("Wealth", Icons.Default.Paid),
            EmergencyOption("Financial security", Icons.Default.VerifiedUser),
            EmergencyOption("Quick profit", Icons.Default.Speed),
            EmergencyOption("High risk", Icons.Default.Warning)
        ),
        1
    ),
    EmergencyQuestion(
        "Which expense is NOT usually part of an emergency fund calculation?",
        listOf(
            EmergencyOption("Rent", Icons.Default.Home),
            EmergencyOption("Groceries", Icons.Default.Restaurant),
            EmergencyOption("Movie tickets", Icons.Default.Movie),
            EmergencyOption("Utilities", Icons.Default.Lightbulb)
        ),
        2
    ),
    EmergencyQuestion(
        "If you have high-interest debt and no emergency fund, you should...",
        listOf(
            EmergencyOption("Ignore debt", Icons.Default.RemoveCircle),
            EmergencyOption("Build small fund first", Icons.Default.Construction),
            EmergencyOption("Invest in gold", Icons.Default.Toll),
            EmergencyOption("Take more debt", Icons.Default.AddCard)
        ),
        1
    ),
    EmergencyQuestion(
        "Liquidity in an emergency fund means...",
        listOf(
            EmergencyOption("It's made of cash", Icons.Default.Payments),
            EmergencyOption("Easy to access", Icons.Default.TouchApp),
            EmergencyOption("It's frozen", Icons.Default.AcUnit),
            EmergencyOption("It's a loan", Icons.Default.RequestQuote)
        ),
        1
    )
)

@Preview(showBackground = true)
@Composable
fun EmergencyFundsQuizScreenPreview() {
    FYP_PrototypeTheme {
        EmergencyQuizResultsState(score = 8, totalQuestions = 10, onFinish = {})
    }
}
