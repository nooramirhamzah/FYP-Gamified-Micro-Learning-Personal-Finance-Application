package com.company.fyp_prototype.ui.lessons

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SmartToy
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
fun HighYieldSavingsLessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    val years = when {
        sliderPosition < 0.5f -> 1
        sliderPosition < 1.5f -> 5
        else -> 10
    }

    val traditionalInterest = when(years) {
        1 -> "$0.10"
        5 -> "$0.50"
        else -> "$1.00"
    }

    val highYieldInterest = when(years) {
        1 -> "$45.00"
        5 -> "$245.00"
        else -> "$553.00"
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
                    progress = { 0.8f },
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Why your money needs a better place to grow",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                ),
                lineHeight = 36.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "See how a small difference in rates makes a huge impact over time.",
                color = PrimaryGreen,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Interactive Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Traditional Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFE3F2FD))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(bottom = 8.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                // Mini tree visual
                                Box(modifier = Modifier.size(20.dp, 40.dp).background(Color(0xFF8D6E63)))
                                Box(modifier = Modifier.size(40.dp).offset(y = (-20).dp).clip(CircleShape).background(Color(0xFF81C784).copy(alpha = 0.6f)))
                            }
                            Text("Traditional", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("0.01% APY", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    traditionalInterest,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // High Yield Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(2.dp, PrimaryGreen, RoundedCornerShape(24.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(bottom = 8.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                // Large tree visual
                                Box(modifier = Modifier.size(24.dp, 60.dp).background(Color(0xFF5D4037)))
                                Box(modifier = Modifier.size(60.dp).offset(y = (-30).dp).clip(CircleShape).background(PrimaryGreen))
                            }
                            Text("High Yield", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("4.50% APY", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = PrimaryGreen,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "+$highYieldInterest",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1 Year", color = if(years == 1) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if(years == 1) FontWeight.Bold else FontWeight.Normal)
                        Text("5 Years", color = if(years == 5) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if(years == 5) FontWeight.Bold else FontWeight.Normal)
                        Text("10 Years", color = if(years == 10) PrimaryGreen else Color.Gray, fontSize = 12.sp, fontWeight = if(years == 10) FontWeight.Bold else FontWeight.Normal)
                    }

                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 0f..2f,
                        steps = 1,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = PrimaryGreen,
                            inactiveTrackColor = Color(0xFFF0F0F0)
                        )
                    )
                    
                    Text(
                        "Drag to see growth over time",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFE3F2FD).copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = LessonDarkBlue, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Compound Interest Magic", fontWeight = FontWeight.Bold, color = TextDark)
                        Text("With 4.5%, your interest earns interest!", fontSize = 13.sp, color = TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FinBot Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SecondaryGreen)
                        .border(2.dp, PrimaryGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 24.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Whoa! That's basically ")
                            withStyle(style = SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                                append("free money")
                            }
                            append(" just for switching accounts!")
                        },
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighYieldSavingsLessonScreenPreview() {
    FYP_PrototypeTheme {
        HighYieldSavingsLessonScreen()
    }
}
