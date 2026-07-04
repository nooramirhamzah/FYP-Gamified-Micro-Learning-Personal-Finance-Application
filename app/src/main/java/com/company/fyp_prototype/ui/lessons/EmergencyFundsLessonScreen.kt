package com.company.fyp_prototype.ui.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Face
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyFundsLessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
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
                            "Lesson: Emergency Funds",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            EmergencyHeroCard()

            Spacer(modifier = Modifier.height(20.dp))

            FinancialGuideBubble()

            Spacer(modifier = Modifier.height(20.dp))

            EmergencyLessonCard(
                eyebrow = "LEARNING GOALS",
                title = "By the end of this lesson, you should know:",
                emoji = "🎯"
            ) {
                LearningGoalRow("What an emergency fund is and why it protects your savings")
                LearningGoalRow("How to separate essential expenses from normal spending")
                LearningGoalRow("How to estimate a 3 to 6 month safety net")
                LearningGoalRow("When to use the fund and when not to touch it")
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "CORE IDEA",
                title = "What is an emergency fund?",
                emoji = "🦺"
            ) {
                Text(
                    text = "An emergency fund is money kept aside for unexpected but necessary situations. It stops one problem, such as a broken laptop or sudden medical bill, from becoming a bigger financial crisis.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "It is not investment money, shopping money, or vacation money. It is your financial backup plan.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyCalculatorCard()

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "ESSENTIAL EXPENSES",
                title = "What should you include?",
                emoji = "📋"
            ) {
                Text(
                    text = "Your target should be based on essential monthly expenses, not your full lifestyle spending. Focus on the expenses you must pay even during a difficult month.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        ExpenseChip("Rent / hostel", true)
                        ExpenseChip("Food", true)
                        ExpenseChip("Transport", true)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        ExpenseChip("Subscriptions", false)
                        ExpenseChip("Gaming skins", false)
                        ExpenseChip("Cafe treats", false)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Green items are usually essential. Grey items are usually optional and should be reduced first when budgeting.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "HOW MUCH IS ENOUGH?",
                title = "Beginner target: 3 stages",
                emoji = "📈"
            ) {
                EmergencyStageRow(
                    stage = "Stage 1",
                    title = "Starter Buffer",
                    detail = "Save RM500 to RM1,000 first. This helps cover small emergencies without borrowing."
                )
                EmergencyStageRow(
                    stage = "Stage 2",
                    title = "1 Month Safety Net",
                    detail = "Build enough to cover one month of essential expenses."
                )
                EmergencyStageRow(
                    stage = "Stage 3",
                    title = "3–6 Month Fund",
                    detail = "Aim for 3 months if your income is stable, or 6 months if your income is irregular."
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "WHERE TO KEEP IT",
                title = "Use the 3 rules: safe, liquid, separate",
                emoji = "🏦"
            ) {
                RuleRow("Safe", "Do not put your emergency fund in risky investments where the value can drop suddenly.")
                RuleRow("Liquid", "Keep it somewhere you can access quickly, such as a savings account or e-wallet cash balance.")
                RuleRow("Separate", "Keep it away from your daily spending account so you are less tempted to use it.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "WHEN TO USE IT",
                title = "Emergency or not emergency?",
                emoji = "✅"
            ) {
                UseCaseRow("Use it", "Medical bill, urgent travel, broken phone needed for study/work, job loss")
                Spacer(modifier = Modifier.height(8.dp))
                UseCaseRow("Do not use it", "Concert tickets, fashion sale, game top-up, vacation upgrade")
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Quick test: Is it unexpected, necessary, and urgent? If yes, it may be a real emergency.",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EmergencyLessonCard(
                eyebrow = "ACTION PLAN",
                title = "Build your fund step by step",
                emoji = "🛠️"
            ) {
                ActionStep(1, "Pick a starter target", "Start with RM500 or one week of essential expenses.")
                ActionStep(2, "Automate a small amount", "Move RM5, RM10, or RM20 every week before spending.")
                ActionStep(3, "Use extra cash wisely", "Put part of allowance, freelance pay, or refunds into the fund.")
                ActionStep(4, "Refill after using it", "If you spend the fund, rebuild it before increasing wants spending.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            KeyTakeawayCard()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EmergencyHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🦺", fontSize = 58.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your Financial Life Vest",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LessonDarkBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "A backup fund keeps small problems from sinking your budget.",
                fontSize = 13.sp,
                color = LessonDarkBlue.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 28.dp),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FinancialGuideBubble() {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE0B2)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Face, contentDescription = null, tint = Color(0xFF795548))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("FINANCIAL GUIDE", color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Surface(
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 24.dp),
                color = Color.White,
                tonalElevation = 2.dp
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Think of this as your financial life vest! Experts commonly recommend saving ")
                        withStyle(style = SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                            append("3 to 6 months")
                        }
                        append(" of essential expenses, but beginners can start with a smaller first target.")
                    },
                    modifier = Modifier.padding(16.dp),
                    color = TextDark,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun EmergencyCalculatorCard() {
    var monthlySpend by remember { mutableFloatStateOf(2000f) }
    var monthsOfCoverage by remember { mutableFloatStateOf(6f) }

    val roundedSpend = ((monthlySpend / 100).toInt() * 100).coerceAtLeast(500)
    val coverageMonths = monthsOfCoverage.toInt().coerceIn(1, 12)
    val goalAmount = roundedSpend * coverageMonths
    val progressMessage = when {
        coverageMonths < 3 -> "Good starter buffer. Keep building toward 3 months."
        coverageMonths <= 6 -> "Strong safety net for most students and young workers."
        else -> "Extra conservative fund for irregular income or higher uncertainty."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Estimate Your Safety Net", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Adjust the sliders to estimate how much emergency cash you may need.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Essential Monthly Spend", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                Text("RM ${roundedSpend.formatAmount()}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = monthlySpend,
                onValueChange = { monthlySpend = it },
                valueRange = 500f..6000f,
                steps = 54,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RM500", color = Color.LightGray, fontSize = 11.sp)
                Text("RM6,000", color = Color.LightGray, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Coverage Duration", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                Surface(
                    color = SecondaryGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "$coverageMonths Months",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Slider(
                value = monthsOfCoverage,
                onValueChange = { monthsOfCoverage = it },
                valueRange = 1f..12f,
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = Color(0xFFF0F0F0)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Your Goal Amount", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
                Text(
                    text = "RM ${goalAmount.formatAmount()}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SecondaryGreen.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = progressMessage,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = DarkGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EmergencyLessonCard(
    eyebrow: String,
    title: String,
    emoji: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SecondaryGreen.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = eyebrow,
                        color = PrimaryGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 21.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun LearningGoalRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("✓", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun ExpenseChip(label: String, isEssential: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        color = if (isEssential) SecondaryGreen.copy(alpha = 0.7f) else Color(0xFFF2F4F7),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isEssential) PrimaryGreen.copy(alpha = 0.25f) else Color(0xFFE1E5EA))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (isEssential) "✅" else "➖", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, color = if (isEssential) DarkGreen else TextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmergencyStageRow(stage: String, title: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = PrimaryGreen,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                stage,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                color = DarkGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(detail, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun RuleRow(rule: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SecondaryGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(rule.first().toString(), color = PrimaryGreen, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(rule, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(description, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun UseCaseRow(label: String, examples: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (label == "Use it") SecondaryGreen.copy(alpha = 0.7f) else Color(0xFFFFF3E0),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(if (label == "Use it") "✅" else "🚫", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = TextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(examples, color = TextGray, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun ActionStep(number: Int, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(PrimaryGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = DarkGreen, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(description, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun KeyTakeawayCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkBackground,
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("KEY TAKEAWAY", color = PrimaryGreen, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your emergency fund is not about becoming rich. It is about staying stable when life surprises you.",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Start small, save consistently, and refill it after every real emergency.",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
        }
    }
}

private fun Int.formatAmount(): String = "%,d".format(this)

@Preview(showBackground = true)
@Composable
fun EmergencyFundsLessonScreenPreview() {
    FYP_PrototypeTheme {
        EmergencyFundsLessonScreen()
    }
}

@Preview(
    name = "Emergency Funds Full Content",
    showBackground = true,
    widthDp = 393,
    heightDp = 2200
)
@Composable
fun EmergencyFundsLessonScreenFullPreview() {
    FYP_PrototypeTheme {
        EmergencyFundsLessonScreen()
    }
}
