package com.company.fyp_prototype.ui.lessons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
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
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "LESSON: INTRO TO MONEY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 1.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
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
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBackground),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Continue Learning",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
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

            MoneyGuideImage()

            Spacer(modifier = Modifier.height(8.dp))

            SpeechBubble(
                title = "Time is your best friend!",
                content = buildAnnotatedString {
                    append("Money is not just something you spend. It is a tool you can use to protect yourself, reach goals, and build future choices. The earlier you understand how it grows, the more powerful your decisions become.")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LessonSectionTitle(
                title = "What you will learn",
                subtitle = "This quick lesson explains why saving early matters before you move into budgeting and emergency funds."
            )

            LearningObjectivesCard()

            Spacer(modifier = Modifier.height(16.dp))

            LessonContentCard(title = "Money has 3 main jobs") {
                ConceptRow(
                    number = "1",
                    title = "Medium of exchange",
                    description = "You use money to buy goods and services, such as food, transport, books, subscriptions, or phone bills."
                )
                ConceptRow(
                    number = "2",
                    title = "Store of value",
                    description = "Money lets you keep value for later. Saving today gives you options when an unexpected cost appears."
                )
                ConceptRow(
                    number = "3",
                    title = "Unit of account",
                    description = "Money helps you compare prices. For example, you can decide whether a RM12 drink is worth more than saving that RM12."
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LessonContentCard(title = "The hidden enemy: inflation") {
                HighlightText(
                    text = buildAnnotatedString {
                        append("Inflation means prices slowly increase over time. If your money stays idle, its ")
                        withStyle(style = SpanStyle(color = LessonDarkBlue, fontWeight = FontWeight.Bold)) {
                            append("buying power")
                        }
                        append(" can decrease.")
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                ExampleBox(
                    title = "Simple example",
                    description = "If a meal costs RM10 today and RM12 in the future, the same RM10 buys less than before. This is why saving is important, but learning how money can grow is even better."
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LessonContentCard(title = "Simple interest vs compound interest") {
                TwoColumnComparison(
                    leftTitle = "Simple interest",
                    leftText = "Interest is earned only on your original money.",
                    rightTitle = "Compound interest",
                    rightText = "Interest is earned on your original money plus earlier interest. This creates a snowball effect."
                )
                Spacer(modifier = Modifier.height(12.dp))
                HighlightText(
                    text = buildAnnotatedString {
                        append("That snowball effect is why ")
                        withStyle(style = SpanStyle(color = LessonDarkBlue, fontWeight = FontWeight.Bold)) {
                            append("starting early")
                        }
                        append(" can matter more than starting with a large amount.")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InteractiveDemoCard()

            Spacer(modifier = Modifier.height(16.dp))

            LessonContentCard(title = "Why starting early wins") {
                ExampleBox(
                    title = "Scenario A: Start early",
                    description = "A student saves a small amount consistently while in university. The money gets more years to compound."
                )
                Spacer(modifier = Modifier.height(10.dp))
                ExampleBox(
                    title = "Scenario B: Start later",
                    description = "Another student waits until after graduation. They may need to save more each month to catch up because their money has less time to grow."
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Lesson: time is a financial advantage. Even small early habits can become meaningful later.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormulaCard()

            Spacer(modifier = Modifier.height(16.dp))

            LessonContentCard(title = "Mini action plan") {
                ActionStep(
                    step = "1",
                    text = "Track one week of spending so you know where your money actually goes."
                )
                ActionStep(
                    step = "2",
                    text = "Choose one small saving target, such as RM5 to RM10 per week."
                )
                ActionStep(
                    step = "3",
                    text = "Keep the money separate from daily spending so it is easier not to touch."
                )
                ActionStep(
                    step = "4",
                    text = "Review your progress every week and increase the amount only when it feels realistic."
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            KeyTakeawayCard()

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Most of the growth happens near the end. That is why starting early is your superpower!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MoneyGuideImage() {
    Box(
        modifier = Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ChartLightGreen.copy(alpha = 0.32f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 18.dp, end = 18.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+%",
                color = ChartGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }

        Box(
            modifier = Modifier
                .size(width = 124.dp, height = 76.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "RM",
                color = PrimaryGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 34.sp
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 1) 34.dp else 28.dp)
                        .clip(CircleShape)
                        .background(if (index == 1) PrimaryGreen else ChartGreen.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RM",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (index == 1) 11.sp else 9.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SpeechBubble(title: String, content: androidx.compose.ui.text.AnnotatedString) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(20.dp, 10.dp)) {
            val path = Path().apply {
                moveTo(10.dp.toPx(), 0.dp.toPx())
                lineTo(0.dp.toPx(), 10.dp.toPx())
                lineTo(20.dp.toPx(), 10.dp.toPx())
                close()
            }
            drawPath(path, color = LessonBlue)
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = LessonBlue
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = LessonDarkBlue
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    color = LessonDarkBlue.copy(alpha = 0.8f),
                    lineHeight = 22.sp,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun LessonSectionTitle(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun LearningObjectivesCard() {
    LessonContentCard(title = "Learning goals") {
        ActionStep(step = "✓", text = "Explain the basic purpose of money.")
        ActionStep(step = "✓", text = "Understand why inflation reduces buying power.")
        ActionStep(step = "✓", text = "Compare simple interest and compound interest.")
        ActionStep(step = "✓", text = "Recognize why saving early creates more financial flexibility.")
    }
}

@Composable
private fun LessonContentCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun ConceptRow(number: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(PrimaryGreen.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = PrimaryGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ActionStep(step: String, text: String) {
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
                .background(PrimaryGreen.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightText(text: androidx.compose.ui.text.AnnotatedString) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LessonBlue.copy(alpha = 0.75f)
    ) {
        Text(
            text = text,
            color = LessonDarkBlue.copy(alpha = 0.85f),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ExampleBox(title: String, description: String) {
    val exampleBackground = if (MaterialTheme.colorScheme.background == DarkBackground) {
        Color(0xFFEAF0F7)
    } else {
        Color(0xFFF8F8F8)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = exampleBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                color = TextGray,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun TwoColumnComparison(
    leftTitle: String,
    leftText: String,
    rightTitle: String,
    rightText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ComparisonBox(
            title = leftTitle,
            description = leftText,
            modifier = Modifier.weight(1f),
            backgroundColor = Color(0xFFF5F5F5),
            titleColor = TextDark,
            bodyColor = TextGray
        )
        ComparisonBox(
            title = rightTitle,
            description = rightText,
            modifier = Modifier.weight(1f),
            backgroundColor = ChartLightGreen.copy(alpha = 0.42f),
            titleColor = DarkGreen,
            bodyColor = TextDark
        )
    }
}

@Composable
private fun ComparisonBox(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    titleColor: Color,
    bodyColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                color = bodyColor,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun FormulaCard() {
    LessonContentCard(title = "Compound interest formula") {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = DarkBackground
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "A = P × (1 + r)ᵗ",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A is the final amount, P is your starting money, r is the yearly interest rate, and t is time in years.",
                    color = Color.White.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "You do not need to memorize the formula for the quiz. Focus on the idea: money can earn more money when it is given enough time.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun KeyTakeawayCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = ChartLightGreen.copy(alpha = 0.32f)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Key takeaway",
                color = ChartGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Good money habits are built from small, repeated decisions. Start small, stay consistent, and let time do part of the work.",
                color = TextDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InteractiveDemoCard() {
    var sliderValue by remember { mutableFloatStateOf(10f) }
    var initialAmountInput by remember { mutableStateOf("1000") }
    val selectedYear = sliderValue.toInt().coerceIn(1, 30)
    val startingAmount = initialAmountInput.toDoubleOrNull()?.coerceIn(0.0, 1_000_000.0) ?: 0.0
    val interestRate = 0.08
    val currentBalance = startingAmount * Math.pow(1 + interestRate, selectedYear.toDouble())
    val interestEarned = (currentBalance - startingAmount).coerceAtLeast(0.0)
    val finalBalance = startingAmount * Math.pow(1 + interestRate, 30.0)
    val progressFraction = if (finalBalance > 0.0) {
        (currentBalance / finalBalance).toFloat().coerceIn(0.05f, 1f)
    } else {
        0.05f
    }
    val milestoneYears = listOf(1, 5, 10, 20, 30)
    val bars = milestoneYears.map { year ->
        val balance = startingAmount * Math.pow(1 + interestRate, year.toDouble())
        val height = if (finalBalance > 0.0) {
            (balance / finalBalance).toFloat().coerceIn(0.08f, 1f)
        } else {
            0.08f
        }
        val color = when {
            year <= selectedYear -> ChartGreen
            year <= selectedYear + 5 -> ChartLightGreen
            else -> Color(0xFFE0E0E0)
        }
        Triple(year, height, color)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "INTERACTIVE DEMO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Watch your money grow",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Surface(
                    color = ChartLightGreen.copy(alpha = 0.28f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("+8%", color = ChartGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("yearly", color = ChartGreen, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = initialAmountInput,
                onValueChange = { value ->
                    initialAmountInput = value.filter { it.isDigit() }.take(7)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Initial amount") },
                prefix = { Text("RM") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = PrimaryGreen,
                    focusedLabelColor = ChartGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF7F8FA),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("After ${selectedYear.yearLabel()}", color = TextGray, fontSize = 12.sp)
                        Text(
                            text = "RM${currentBalance.toInt().formatMoney()}",
                            color = TextDark,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Interest earned", color = TextGray, fontSize = 12.sp)
                        Text(
                            text = "+RM${interestEarned.toInt().formatMoney()}",
                            color = ChartGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFAFAFA),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Growth preview", color = TextGray, fontSize = 12.sp)
                        Text(
                            text = "${(progressFraction * 100).toInt()}% of 30-year target",
                            color = ChartGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(132.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        bars.forEach { (year, height, color) ->
                            val isActive = year <= selectedYear
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(34.dp)
                                        .fillMaxHeight(height)
                                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 5.dp, bottomEnd = 5.dp))
                                        .background(color)
                                )
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Text(
//                                    text = "${year}y",
//                                    color = if (isActive) TextDark else Color(0xFFBDBDBD),
//                                    fontSize = 11.sp,
//                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
//                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("1 year", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                Surface(
                    color = PrimaryGreen.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = selectedYear.yearLabel(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = ChartGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("30 years", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it.coerceAtLeast(1f) },
                valueRange = 1f..30f,
                steps = 28,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun Int.formatMoney(): String = "%,d".format(this)

private fun Int.yearLabel(): String = if (this == 1) "1 year" else "$this years"

//@Preview(showBackground = true)
//@Composable
//fun LessonScreenPreview() {
//    FYP_PrototypeTheme {
//        LessonScreen()
//    }
//}

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    FYP_PrototypeTheme {
        LessonScreen()
    }
}

@Preview(
    name = "LessonScreen Full Content",
    showBackground = true,
    widthDp = 393,
    heightDp = 3000
)
@Composable
fun LessonScreenFullPreview() {
    FYP_PrototypeTheme {
        LessonScreen()
    }
}