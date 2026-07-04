package com.company.fyp_prototype.ui.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetLessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    val lessonProgress by remember {
        derivedStateOf {
            if (scrollState.maxValue > 0) {
                (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
                LinearProgressIndicator(
                    progress = { lessonProgress },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            BudgetIntroCard()

            Spacer(modifier = Modifier.height(20.dp))

            BudgetDonutChart()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetLearningGoalsCard()

            Spacer(modifier = Modifier.height(20.dp))

            BudgetSectionTitle(
                label = "THE FORMULA",
                title = "Split every ringgit into 3 jobs"
            )

            BudgetCategoryItem(
                title = "Needs",
                subtitle = "Rent, groceries, transport, bills and basic commitments",
                percentage = "50%",
                icon = Icons.Default.Home,
                iconColor = NeedsGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            BudgetCategoryItem(
                title = "Wants",
                subtitle = "Dining out, movies, games, hobbies and lifestyle spending",
                percentage = "30%",
                icon = Icons.Default.ShoppingBag,
                iconColor = WantsPurple
            )
            Spacer(modifier = Modifier.height(12.dp))
            BudgetCategoryItem(
                title = "Savings",
                subtitle = "Emergency fund, future goals, investing and debt repayment",
                percentage = "20%",
                icon = Icons.Default.Savings,
                iconColor = SavingsGreen,
                isGoal = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            BudgetCalculatorCard()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetExampleCard()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetAdjustmentCard()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetMistakesCard()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetActionPlanCard()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetFinBotMessage()

            Spacer(modifier = Modifier.height(24.dp))

            BudgetKeyTakeawayCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BudgetIntroCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PieChart,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Budgeting made simple",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "A quick rule for deciding where your income should go.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = buildAnnotatedString {
                    append("The 50-30-20 rule helps you avoid spending randomly. It divides your after-tax income into ")
                    withStyle(style = SpanStyle(color = NeedsGray, fontWeight = FontWeight.Bold)) { append("needs") }
                    append(", ")
                    withStyle(style = SpanStyle(color = WantsPurple, fontWeight = FontWeight.Bold)) { append("wants") }
                    append(" and ")
                    withStyle(style = SpanStyle(color = SavingsGreen, fontWeight = FontWeight.Bold)) { append("savings") }
                    append(" so you can enjoy today while still preparing for tomorrow.")
                },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun BudgetDonutChart() {
    Box(
        modifier = Modifier.size(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(190.dp)) {
            val strokeWidth = 42.dp.toPx()
            drawArc(
                color = Color(0xFFEAEFF5),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = NeedsGray,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = WantsPurple,
                startAngle = 90f,
                sweepAngle = 108f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = SavingsGreen,
                startAngle = 198f,
                sweepAngle = 72f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("MONTHLY", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("100%", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold))
            Text("Income", color = Color.LightGray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BudgetLearningGoalsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFECEFF3))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            BudgetSectionTitle(label = "LEARNING GOALS", title = "By the end, you should know:")
            Spacer(modifier = Modifier.height(4.dp))
            BudgetBulletPoint("How to separate spending into needs, wants and savings.")
            BudgetBulletPoint("How to calculate a simple monthly budget from your income.")
            BudgetBulletPoint("Why paying yourself first makes saving more consistent.")
        }
    }
}

@Composable
fun BudgetSectionTitle(label: String, title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            color = PrimaryGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 23.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun BudgetBulletPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(PrimaryGreen)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp, lineHeight = 20.sp)
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
        color = MaterialTheme.colorScheme.surface,
        border = if (isGoal) BorderStroke(2.dp, PrimaryGreen) else BorderStroke(1.dp, Color(0xFFECEFF3)),
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
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp, lineHeight = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
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
                        "PAY YOURSELF FIRST",
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

@Composable
fun BudgetCalculatorCard() {
    var incomeSlider by remember { mutableFloatStateOf(3000f) }
    val income = (incomeSlider / 100f).roundToInt() * 100
    val needs = (income * 0.50).roundToInt()
    val wants = (income * 0.30).roundToInt()
    val savings = (income * 0.20).roundToInt()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(SavingsGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = null, tint = SavingsGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Try it with your income", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                    Text("Move the slider and see the budget split.", fontSize = 12.sp, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("Monthly income", color = TextGray, fontSize = 13.sp)
                Text("RM ${income.formatBudgetMoney()}", color = TextDark, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            }

            Slider(
                value = incomeSlider,
                onValueChange = { incomeSlider = it },
                valueRange = 1500f..8000f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            BudgetCalculationRow("Needs", "50%", needs, NeedsGray)
            BudgetCalculationRow("Wants", "30%", wants, WantsPurple)
            BudgetCalculationRow("Savings", "20%", savings, SavingsGreen, highlight = true)
        }
    }
}

@Composable
fun BudgetCalculationRow(label: String, percent: String, amount: Int, color: Color, highlight: Boolean = false) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (highlight) color.copy(alpha = 0.12f) else Color(0xFFF8F9FB)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, modifier = Modifier.weight(1f), color = TextDark, fontWeight = FontWeight.Bold)
            Text(percent, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("RM ${amount.formatBudgetMoney()}", color = color, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun BudgetExampleCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFECEFF3))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            BudgetSectionTitle(label = "EXAMPLE", title = "How it works in real life")
            Text(
                "If a student earns RM2,500 per month from allowance, part-time work or internship pay:",
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            BudgetExampleLine("Needs", "RM1,250", "food, rent, transport, phone bill", NeedsGray)
            BudgetExampleLine("Wants", "RM750", "cafes, games, movies, shopping", WantsPurple)
            BudgetExampleLine("Savings", "RM500", "emergency fund and future goals", SavingsGreen)
        }
    }
}

@Composable
fun BudgetExampleLine(label: String, amount: String, description: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        BudgetPercentPill(label, color)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(amount, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            Text(description, color = TextGray, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
fun BudgetPercentPill(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun BudgetAdjustmentCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF6FFF8),
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            BudgetSectionTitle(label = "IMPORTANT", title = "The rule can be adjusted")
            Text(
                "The 50-30-20 rule is a guide, not a strict law. If your needs are high, start with a smaller saving target such as 5% or 10%, then slowly increase it when your income improves or expenses decrease.",
                color = TextDark,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "The main habit is consistency: save something every month before spending everything else.",
                color = PrimaryGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
fun BudgetMistakesCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFECEFF3))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            BudgetSectionTitle(label = "COMMON MISTAKES", title = "Watch out for these traps")
            BudgetWarningItem("Treating wants as needs, such as expensive subscriptions or frequent food delivery.")
            BudgetWarningItem("Saving only what is left at the end of the month instead of saving first.")
            BudgetWarningItem("Ignoring small daily purchases because each one feels harmless.")
        }
    }
}

@Composable
fun BudgetWarningItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF3E0)),
            contentAlignment = Alignment.Center
        ) {
            Text("!", color = Color(0xFFFF9800), fontWeight = FontWeight.ExtraBold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
fun BudgetActionPlanCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFECEFF3))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            BudgetSectionTitle(label = "ACTION PLAN", title = "Use the rule this week")
            BudgetActionStep("1", "Write down your monthly income after deductions.")
            BudgetActionStep("2", "List your fixed needs first, such as rent, food and transport.")
            BudgetActionStep("3", "Set aside savings immediately, even if the amount is small.")
            BudgetActionStep("4", "Use what remains for wants without guilt, because it is planned.")
        }
    }
}

@Composable
fun BudgetActionStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(PrimaryGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = DarkGreen, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
fun BudgetFinBotMessage() {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF795548)
                )
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
        Column(modifier = Modifier.weight(1f)) {
            Text("FINBOT", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Smart budgeting is not about saying no to everything. It is about giving every ringgit a purpose. Aim for ")
                        withStyle(style = SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                            append("20% savings")
                        }
                        append(" when possible, then adjust the rule based on your real situation.")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextDark
                )
            }
        }
    }
}

@Composable
fun BudgetKeyTakeawayCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = DarkBackground
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("KEY TAKEAWAY", color = PrimaryGreen, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Budgeting gives your money direction before it disappears.",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Use 50% for needs, 30% for wants and 20% for savings as a simple starting point. The more consistently you follow the habit, the easier future financial goals become.",
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

private fun Int.formatBudgetMoney(): String = "%,d".format(this)

@Preview(showBackground = true)
@Composable
fun BudgetLessonScreenPreview() {
    FYP_PrototypeTheme {
        BudgetLessonScreen()
    }
}

@Preview(
    name = "Budget Lesson Full Content",
    showBackground = true,
    widthDp = 393,
    heightDp = 1800
)
@Composable
fun BudgetLessonScreenFullPreview() {
    FYP_PrototypeTheme {
        BudgetLessonScreen()
    }
}
