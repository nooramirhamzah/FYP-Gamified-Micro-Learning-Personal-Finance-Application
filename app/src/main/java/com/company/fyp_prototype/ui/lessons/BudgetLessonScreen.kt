package com.company.fyp_prototype.ui.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetLessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Lesson: The 50-30-20 Rule",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(8.dp)
                        .clip(CircleShape),
                    color = PrimaryGreen,
                    trackColor = Color(0xFFF0F0F0)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Got it!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        },
        containerColor = BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Donut Chart
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 40.dp.toPx()
                    // Background gray circle
                    drawArc(
                        color = Color(0xFF606D81),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    // Needs (50%) - Starts from top (270 degrees)
                    drawArc(
                        color = NeedsGray,
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    // Wants (30%)
                    drawArc(
                        color = WantsPurple,
                        startAngle = 180f,
                        sweepAngle = 108f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    // Savings (20%)
                    drawArc(
                        color = SavingsGreen,
                        startAngle = 288f,
                        sweepAngle = 72f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TOTAL", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("100%", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Income", color = Color.LightGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Budget Categories
            BudgetCategoryItem(
                title = "Needs",
                subtitle = "Rent, groceries, bills",
                percentage = "50%",
                icon = Icons.Default.Home,
                iconColor = NeedsGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            BudgetCategoryItem(
                title = "Wants",
                subtitle = "Dining, movies, hobbies",
                percentage = "30%",
                icon = Icons.Default.ShoppingBag,
                iconColor = WantsPurple
            )
            Spacer(modifier = Modifier.height(12.dp))
            BudgetCategoryItem(
                title = "Savings",
                subtitle = "Investing, emergency fund",
                percentage = "20%",
                icon = Icons.Default.Savings,
                iconColor = SavingsGreen,
                isGoal = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // FinBot Message
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE0B2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF795548))
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("FINBOT", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("This rule helps you budget! Try to save ")
                                withStyle(style = SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                                    append("20%")
                                }
                                append(" of your income first. Tap the green slice to see why!")
                            },
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetCategoryItem(
    title: String,
    subtitle: String,
    percentage: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    isGoal: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = if (isGoal) BorderStroke(2.dp, PrimaryGreen) else null,
        shadowElevation = if (isGoal) 4.dp else 0.dp
    ) {
        Box {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(subtitle, color = TextGray, fontSize = 12.sp)
                }
                Text(
                    percentage,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isGoal) iconColor else Color(0xFF606D81)
                )
            }
            if (isGoal) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    color = PrimaryGreen,
                    shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 22.dp)
                ) {
                    Text(
                        "YOUR GOAL",
                        color = DarkGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetLessonScreenPreview() {
    FYP_PrototypeTheme {
        BudgetLessonScreen()
    }
}
