package com.company.fyp_prototype.ui.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighYieldSavingsLessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
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
                            "Lesson: High Yield Savings",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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
            Box(modifier = Modifier.padding(24.dp)) {
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
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            HighYieldHeroCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldLearningGoalsCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldDefinitionCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldInteractiveComparisonCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldTermsCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldSafetyChecklistCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldExampleCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldActionPlanCard()

            Spacer(modifier = Modifier.height(16.dp))

            HighYieldKeyTakeawayCard()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HighYieldHeroCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(SecondaryGreen)
                        .border(2.dp, PrimaryGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Savings, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(34.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Make idle money work harder",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        ),
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "A high-yield savings account pays more interest than a normal savings account, while still keeping money accessible.",
                        color = TextGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("The goal is not to get rich overnight. The goal is to stop your savings from sitting in a low-interest account when it could be earning more through ")
                        withStyle(SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                            append("compound interest")
                        }
                        append(".")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = TextDark
                )
            }
        }
    }
}

@Composable
private fun HighYieldLearningGoalsCard() {
    HighYieldSectionCard(
        title = "By the end of this lesson, you should know:",
        icon = Icons.Default.Lightbulb,
        iconTint = Color(0xFFFFB300)
    ) {
        HighYieldBullet("What high-yield savings means")
        HighYieldBullet("Why APY matters more than the account label")
        HighYieldBullet("How interest grows over time")
        HighYieldBullet("What to check before choosing an account")
    }
}

@Composable
private fun HighYieldDefinitionCard() {
    HighYieldSectionCard(
        title = "What is high-yield savings?",
        icon = Icons.Default.AccountBalance,
        iconTint = LessonDarkBlue
    ) {
        Text(
            text = "A high-yield savings account is a savings account that offers a higher annual return than a traditional savings account. It is useful for money you want to keep safe and accessible, such as an emergency fund or short-term goal savings.",
            color = TextGray,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HighYieldMiniDefinition(
                modifier = Modifier.weight(1f),
                label = "Traditional",
                value = "Low rate",
                description = "Often pays very little interest."
            )
            HighYieldMiniDefinition(
                modifier = Modifier.weight(1f),
                label = "High Yield",
                value = "Higher rate",
                description = "Pays more interest on the same balance."
            )
        }
    }
}

@Composable
private fun HighYieldInteractiveComparisonCard() {
    var yearsSlider by remember { mutableFloatStateOf(1f) }
    var principalSlider by remember { mutableFloatStateOf(3000f) }

    val years = when {
        yearsSlider < 0.5f -> 1
        yearsSlider < 1.5f -> 5
        else -> 10
    }
    val principal = (principalSlider / 500f).roundToInt() * 500
    val traditionalRate = 0.0001 // 0.01% APY
    val highYieldRate = 0.045 // 4.50% APY

    val traditionalBalance = principal * (1 + traditionalRate).pow(years)
    val highYieldBalance = principal * (1 + highYieldRate).pow(years)
    val traditionalInterest = traditionalBalance - principal
    val highYieldInterest = highYieldBalance - principal
    val extraEarned = highYieldInterest - traditionalInterest

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = PrimaryGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Interactive comparison", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                    Text("Rates shown are educational examples.", color = TextGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Starting savings amount", color = TextGray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF5F7FA),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = principal.formatRM(),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )
            }
            Slider(
                value = principalSlider,
                onValueChange = { principalSlider = it },
                valueRange = 1000f..10000f,
                steps = 17,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1 Year", color = if (years == 1) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if (years == 1) FontWeight.Bold else FontWeight.Normal)
                Text("5 Years", color = if (years == 5) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if (years == 5) FontWeight.Bold else FontWeight.Normal)
                Text("10 Years", color = if (years == 10) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if (years == 10) FontWeight.Bold else FontWeight.Normal)
            }

            Slider(
                value = yearsSlider,
                onValueChange = { yearsSlider = it },
                valueRange = 0f..2f,
                steps = 1,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HighYieldAccountComparisonBox(
                    modifier = Modifier.weight(1f),
                    title = "Traditional",
                    rate = "0.01% APY",
                    interest = traditionalInterest,
                    isHighlighted = false
                )
                HighYieldAccountComparisonBox(
                    modifier = Modifier.weight(1f),
                    title = "High Yield",
                    rate = "4.50% APY",
                    interest = highYieldInterest,
                    isHighlighted = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SecondaryGreen,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Extra earned with high-yield savings", color = DarkGreen, fontSize = 13.sp)
                    Text(
                        text = extraEarned.formatRM(),
                        color = DarkGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text("after $years year${if (years > 1) "s" else ""}", color = DarkGreen, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun HighYieldAccountComparisonBox(
    modifier: Modifier,
    title: String,
    rate: String,
    interest: Double,
    isHighlighted: Boolean
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .then(
                if (isHighlighted) Modifier.border(2.dp, PrimaryGreen, RoundedCornerShape(24.dp))
                else Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            )
            .background(if (isHighlighted) Color(0xFFE8F5E9) else Color(0xFFE3F2FD))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (isHighlighted) 72.dp else 60.dp)
                .clip(CircleShape)
                .background(if (isHighlighted) PrimaryGreen.copy(alpha = 0.15f) else LessonDarkBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isHighlighted) Icons.Default.Savings else Icons.Default.AccountBalance,
                contentDescription = null,
                tint = if (isHighlighted) PrimaryGreen else LessonDarkBlue,
                modifier = Modifier.size(if (isHighlighted) 38.dp else 32.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark, textAlign = TextAlign.Center)
        Text(rate, color = if (isHighlighted) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = if (isHighlighted) PrimaryGreen else Color.White,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "+${interest.formatRM()}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                color = if (isHighlighted) Color.White else TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HighYieldTermsCard() {
    HighYieldSectionCard(
        title = "Important terms",
        icon = Icons.Default.Info,
        iconTint = LessonDarkBlue
    ) {
        HighYieldTermRow(
            term = "APY",
            meaning = "Annual Percentage Yield. It estimates how much you earn in one year after compounding."
        )
        HighYieldTermRow(
            term = "Interest",
            meaning = "Money the bank pays you for keeping your savings with them."
        )
        HighYieldTermRow(
            term = "Compound interest",
            meaning = "Interest earned on both your original money and your previous interest."
        )
        HighYieldTermRow(
            term = "Liquidity",
            meaning = "How easily you can access your money when you need it."
        )
    }
}

@Composable
private fun HighYieldSafetyChecklistCard() {
    HighYieldSectionCard(
        title = "Before choosing an account, check:",
        icon = Icons.Default.CheckCircle,
        iconTint = PrimaryGreen
    ) {
        HighYieldBullet("Is the rate promotional or long-term?")
        HighYieldBullet("Are there monthly fees that reduce the benefit?")
        HighYieldBullet("Is there a minimum balance requirement?")
        HighYieldBullet("Can you withdraw the money when needed?")
        HighYieldBullet("Is the provider regulated and trustworthy?")

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFFF8E1),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "A higher rate is good only if the account is still safe, accessible, and low-cost.",
                    color = TextDark,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun HighYieldExampleCard() {
    HighYieldSectionCard(
        title = "Where does this fit in your finances?",
        icon = Icons.Default.Map,
        iconTint = WantsPurple
    ) {
        HighYieldFitRow("Emergency fund", "Good fit", PrimaryGreen)
        HighYieldFitRow("Short-term goal, such as laptop or travel", "Good fit", PrimaryGreen)
        HighYieldFitRow("Money needed for bills this week", "Maybe too much effort", Color(0xFFFFA000))
        HighYieldFitRow("Long-term wealth building", "Consider investing too", WantsPurple)
    }
}

@Composable
private fun HighYieldActionPlanCard() {
    HighYieldSectionCard(
        title = "Mini action plan",
        icon = Icons.Default.TaskAlt,
        iconTint = PrimaryGreen
    ) {
        HighYieldNumberedStep(1, "Check the interest rate on your current savings account.")
        HighYieldNumberedStep(2, "Compare it with a higher-yield option.")
        HighYieldNumberedStep(3, "Move only savings that should stay safe and accessible.")
        HighYieldNumberedStep(4, "Review the account every few months because rates can change.")
    }
}

@Composable
private fun HighYieldKeyTakeawayCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = DarkBackground
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text("Key takeaway", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        append("High-yield savings helps your safe money grow faster. Use it for money you want to keep ")
                        withStyle(SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                            append("safe, separate, and accessible")
                        }
                        append(" — not for risky speculation.")
                    },
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
private fun HighYieldSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    title,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun HighYieldMiniDefinition(
    modifier: Modifier,
    label: String,
    value: String,
    description: String
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF5F7FA),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(value, color = TextDark, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = TextGray, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun HighYieldBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(PrimaryGreen)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun HighYieldTermRow(term: String, meaning: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = SecondaryGreen,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                term,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(meaning, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun HighYieldFitRow(label: String, status: String, statusColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        color = Color(0xFFF5F7FA),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = TextDark, fontSize = 13.sp, modifier = Modifier.weight(1f), lineHeight = 18.sp)
            Surface(
                color = statusColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HighYieldNumberedStep(number: Int, text: String) {
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
            Text(number.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
    }
}

private fun Int.formatRM(): String = "RM%,d".format(this)
private fun Double.formatRM(): String = "RM%,.2f".format(this)

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun HighYieldSavingsLessonScreenPreview() {
    FYP_PrototypeTheme {
        HighYieldSavingsLessonScreen()
    }
}

@Preview(
    name = "High Yield Lesson Full Content",
    showBackground = true,
    widthDp = 393,
    heightDp = 2200
)
@Composable
fun HighYieldSavingsLessonScreenFullPreview() {
    FYP_PrototypeTheme {
        HighYieldSavingsLessonScreen()
    }
}
