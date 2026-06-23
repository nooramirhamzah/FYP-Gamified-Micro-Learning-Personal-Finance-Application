package com.company.fyp_prototype.ui

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
    userViewModel: UserViewModel? = null,
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf<Int?>(1) }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            Row(
                modifier = Modifier
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
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(if (index < 4) PrimaryGreen else Color(0xFFE0E0E0))
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    "4/5",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = {
                        userViewModel?.addCoins(50)
                        userViewModel?.completeLesson("high_yield")
                        onFinish()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Finish", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
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
                            text = "750 coins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFFFA000)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Which of these accounts will grow your money the fastest over 10 years?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    textAlign = TextAlign.Start
                ),
                lineHeight = 32.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Grid of options
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuizGridOption(
                        text = "Traditional Savings",
                        icon = Icons.Default.AccountBalance,
                        isSelected = selectedOption == 0,
                        onClick = { selectedOption = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    QuizGridOption(
                        text = "High Yield Savings",
                        icon = Icons.Default.TrendingUp,
                        isSelected = selectedOption == 1,
                        isCorrect = true,
                        onClick = { selectedOption = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuizGridOption(
                        text = "Checking Account",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedOption == 2,
                        onClick = { selectedOption = 2 },
                        modifier = Modifier.weight(1f)
                    )
                    QuizGridOption(
                        text = "Physical Cash",
                        icon = Icons.Default.Payments,
                        isSelected = selectedOption == 3,
                        onClick = { selectedOption = 3 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
