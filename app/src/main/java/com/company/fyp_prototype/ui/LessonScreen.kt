package com.company.fyp_prototype.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "LESSON: INTRO TO MONEY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
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
                        containerColor = Color.White
                    )
                )
                LinearProgressIndicator(
                    progress = { 0.25f },
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
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Robot Placeholder (Imagine a cute 3D robot here)
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Speech Bubble
            SpeechBubble(
                title = "Time is your best friend!",
                content = buildAnnotatedString {
                    append("Compound interest is like a ")
                    withStyle(style = SpanStyle(color = LessonDarkBlue, fontWeight = FontWeight.Bold)) {
                        append("snowball")
                    }
                    append(" rolling down a hill. It gets bigger the longer it rolls, even if you don't add more snow!")
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Interactive Demo Card
            InteractiveDemoCard()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tip Section
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
                    text = "Most of the growth happens at the end. That's why starting early is your superpower!",
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SpeechBubble(title: String, content: androidx.compose.ui.text.AnnotatedString) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Triangle for speech bubble
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
fun InteractiveDemoCard() {
    var sliderValue by remember { mutableFloatStateOf(10f) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                            color = Color.LightGray,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Watch your money grow",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                }
                
                Surface(
                    color = ChartLightGreen.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("+8%", color = ChartGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Interest", color = ChartGreen, fontSize = 10.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val bars = listOf(
                        0.15f to Color(0xFFF0F0F0),
                        0.25f to Color(0xFFE0E0E0),
                        0.40f to LessonBlue,
                        0.60f to ChartLightGreen,
                        0.85f to ChartGreen
                    )
                    
                    bars.forEachIndexed { index, (height, color) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .fillMaxHeight(height)
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                                .background(color)
                        )
                    }
                }
                
                // Tooltip/Label for active bar
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(bottom = 60.dp, end = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBackground)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("$12,450", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1 Year", color = Color.LightGray, fontSize = 12.sp)
                Text("10 Years", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("30 Years", color = Color.LightGray, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..30f,
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

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    FYP_PrototypeTheme {
        LessonScreen()
    }
}
