package com.company.fyp_prototype.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*

@Composable
fun HomeScreen(onLessonSelect: (String) -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    Scaffold(
        bottomBar = { BottomNavigationBar(onNavigate) },
        containerColor = BackgroundWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TopHeader() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { ProgressCard() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item {
                Text(
                    text = "Your Quest",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            
            // Learning Path
            item {
                QuestItem(
                    title = "Intro to Money",
                    status = QuestStatus.COMPLETED,
                    showLine = true,
                    onClick = { onLessonSelect("intro") }
                )
            }
            item {
                QuestItem(
                    title = "The 50-30-20 Rule",
                    status = QuestStatus.COMPLETED,
                    showLine = true,
                    onClick = { onLessonSelect("budget") }
                )
            }
            item {
                QuestItem(
                    title = "Emergency Funds",
                    status = QuestStatus.COMPLETED,
                    showLine = true,
                    onClick = { onLessonSelect("emergency") }
                )
            }
            item {
                QuestItem(
                    title = "High Yield Savings",
                    status = QuestStatus.CURRENT,
                    description = "Grow your money faster with the right account.",
                    showLine = false,
                    onClick = { onLessonSelect("high_yield") }
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LockedGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Profile", tint = TextGray)
        }

        // XP Points
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
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
                    Text("$", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "1,250",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextDark
                )
            }
        }
    }
}

@Composable
fun ProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "High Yield Savings",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Level Progress", color = TextGray, fontSize = 14.sp)
                Text("550/700 XP", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.78f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape),
                color = PrimaryGreen,
                trackColor = Color(0xFFF0F0F0)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "150 XP to get Savings Guru badge!",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp
            )
        }
    }
}

enum class QuestStatus { COMPLETED, CURRENT, LOCKED }

@Composable
fun QuestItem(
    title: String,
    status: QuestStatus,
    description: String? = null,
    showLine: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = status != QuestStatus.LOCKED, onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        when (status) {
                            QuestStatus.COMPLETED -> PrimaryGreen
                            QuestStatus.CURRENT -> PrimaryGreen
                            QuestStatus.LOCKED -> Color.White
                        }
                    )
                    .then(
                        if (status == QuestStatus.LOCKED) Modifier.border(2.dp, LockedGray, CircleShape)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (status) {
                        QuestStatus.COMPLETED -> Icons.Default.Check
                        QuestStatus.CURRENT -> Icons.Default.PieChart
                        QuestStatus.LOCKED -> Icons.Default.Lock
                    },
                    contentDescription = null,
                    tint = if (status == QuestStatus.LOCKED) LockedGray else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            if (showLine) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(if (status == QuestStatus.CURRENT) 160.dp else 60.dp)
                        .background(if (status == QuestStatus.COMPLETED) PrimaryGreen else LockedGray)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content Column
        Column(modifier = Modifier.weight(1f)) {
            if (status == QuestStatus.CURRENT) {
                CurrentLessonCard(title, description ?: "", onClick)
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (status == QuestStatus.LOCKED) Color.LightGray else TextDark,
                        textDecoration = if (status == QuestStatus.COMPLETED) TextDecoration.LineThrough else null
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (status == QuestStatus.COMPLETED) Icons.Default.Star else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (status == QuestStatus.COMPLETED) PrimaryGreen else Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (status == QuestStatus.COMPLETED) "Completed" else "Locked",
                        color = if (status == QuestStatus.COMPLETED) PrimaryGreen else Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentLessonCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = SecondaryGreen,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "CURRENT",
                        color = DarkGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = TextGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(onNavigate: (String) -> Unit = {}) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Learn", Icons.Default.School, "home"),
            Triple("Portfolio", Icons.Default.PieChart, "portfolio"),
            Triple("Profile", Icons.Default.Person, "profile")
        )

        items.forEach { (label, icon, route) ->
            val selected = route == "home"
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(route) },
                icon = {
                    if (selected) {
                        Surface(
                            color = SecondaryGreen,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                icon,
                                contentDescription = label,
                                tint = PrimaryGreen,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    } else {
                        Icon(icon, contentDescription = label, tint = TextGray)
                    }
                },
                label = {
                    Text(
                        label,
                        color = if (selected) PrimaryGreen else TextGray,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FYP_PrototypeTheme {
        HomeScreen()
    }
}
