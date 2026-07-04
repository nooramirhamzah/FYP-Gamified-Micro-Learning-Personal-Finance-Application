package com.company.fyp_prototype.ui.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    onLessonSelect: (String) -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val coins by userViewModel.coins.collectAsState()
    val completedLessons by userViewModel.completedLessons.collectAsState()
    val nickname by userViewModel.nickname.collectAsState()
    val avatarEmoji by userViewModel.avatarEmoji.collectAsState()

    HomeContent(
        coins = coins,
        nickname = nickname,
        avatarEmoji = avatarEmoji,
        completedLessons = completedLessons,
        onLessonSelect = onLessonSelect,
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeContent(
    coins: Int,
    nickname: String,
    avatarEmoji: String,
    completedLessons: List<String>,
    onLessonSelect: (String) -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(onNavigate) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TopHeader(coins, nickname, avatarEmoji) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { ProgressCard(completedLessons = completedLessons) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item {
                Text(
                    text = "Your Quest",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            
            // Learning Path
            val currentLessonId = nextCurrentLessonId(completedLessons)
            item {
                val status = questStatusFor(
                    lessonId = "intro",
                    completedLessons = completedLessons,
                    currentLessonId = currentLessonId,
                    isUnlocked = true
                )
                QuestItem(
                    title = "Intro to Money",
                    status = status,
                    showLine = true,
                    onClick = { onLessonSelect("intro") }
                )
            }
            item {
                val status = questStatusFor(
                    lessonId = "budget",
                    completedLessons = completedLessons,
                    currentLessonId = currentLessonId,
                    isUnlocked = completedLessons.contains("intro")
                )
                QuestItem(
                    title = "The 50-30-20 Rule",
                    status = status,
                    showLine = true,
                    onClick = { onLessonSelect("budget") }
                )
            }
            item {
                val status = questStatusFor(
                    lessonId = "emergency",
                    completedLessons = completedLessons,
                    currentLessonId = currentLessonId,
                    isUnlocked = completedLessons.contains("budget")
                )
                QuestItem(
                    title = "Emergency Funds",
                    status = status,
                    showLine = true,
                    onClick = { onLessonSelect("emergency") }
                )
            }
            item {
                val status = questStatusFor(
                    lessonId = "high_yield",
                    completedLessons = completedLessons,
                    currentLessonId = currentLessonId,
                    isUnlocked = completedLessons.contains("emergency")
                )
                QuestItem(
                    title = "High Yield Savings",
                    status = status,
                    description = "Grow your money faster with the right account.",
                    showLine = false,
                    onClick = { onLessonSelect("high_yield") }
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

private fun questStatusFor(
    lessonId: String,
    completedLessons: List<String>,
    currentLessonId: String?,
    isUnlocked: Boolean
): QuestStatus = when {
    completedLessons.contains(lessonId) -> QuestStatus.COMPLETED
    isUnlocked && lessonId == currentLessonId -> QuestStatus.CURRENT
    else -> QuestStatus.LOCKED
}

private fun nextCurrentLessonId(completedLessons: List<String>): String? {
    return learningTree.firstOrNull { lesson ->
        lesson.id !in completedLessons && lesson.isUnlocked(completedLessons)
    }?.id
}

private fun LearningTreeItem.isUnlocked(completedLessons: List<String>): Boolean {
    return when (id) {
        "intro" -> true
        "budget" -> "intro" in completedLessons
        "emergency" -> "budget" in completedLessons
        "high_yield" -> "emergency" in completedLessons
        else -> false
    }
}

@Composable
fun TopHeader(coins: Int, nickname: String, avatarEmoji: String) {
    val displayName = nickname.ifBlank { "Learner" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatarEmoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Hi, $displayName",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // XP Points
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
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
                    text = coins.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ProgressCard(completedLessons: List<String>) {
    val totalLessons = learningTree.size
    val completedCount = learningTree.count { it.id in completedLessons }
    val progress = completedCount.toFloat() / totalLessons.toFloat()
    val nextLesson = learningTree.firstOrNull { it.id !in completedLessons }
    val title = nextLesson?.title ?: "Learning Tree Complete"
    val progressText = "$completedCount/$totalLessons lessons"
    val helperText = if (nextLesson == null) {
        "All lessons complete. Great work!"
    } else {
        "${totalLessons - completedCount} lesson${if (totalLessons - completedCount == 1) "" else "s"} left to finish the learning tree."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Level Progress", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
                Text(progressText, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape),
                color = PrimaryGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = helperText,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                fontSize = 12.sp
            )
        }
    }
}

private data class LearningTreeItem(val id: String, val title: String)

private val learningTree = listOf(
    LearningTreeItem("intro", "Intro to Money"),
    LearningTreeItem("budget", "The 50-30-20 Rule"),
    LearningTreeItem("emergency", "Emergency Funds"),
    LearningTreeItem("high_yield", "High Yield Savings")
)

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
                            QuestStatus.LOCKED -> LockedGray.copy(alpha = 0.4f)
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
                CurrentLessonCard(
                    title = title,
                    description = description ?: "",
                    isUnlocked = true,
                    onClick = onClick
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (status == QuestStatus.LOCKED) Color.LightGray else MaterialTheme.colorScheme.onBackground,
                        textDecoration = if (status == QuestStatus.COMPLETED) TextDecoration.LineThrough else null
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (status) {
                            QuestStatus.COMPLETED -> Icons.Default.Star
                            else -> Icons.Default.Lock
                        },
                        contentDescription = null,
                        tint = if (status == QuestStatus.COMPLETED) PrimaryGreen else Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (status) {
                            QuestStatus.COMPLETED -> "Completed"
                            else -> "Locked"
                        },
                        color = if (status == QuestStatus.COMPLETED) PrimaryGreen else Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentLessonCard(
    title: String,
    description: String,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(enabled = isUnlocked, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                enabled = isUnlocked,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    disabledContainerColor = LockedGray.copy(alpha = 0.6f)
                ),
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
        containerColor = MaterialTheme.colorScheme.surface,
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
                        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
                    }
                },
                label = {
                    Text(
                        label,
                        color = if (selected) PrimaryGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
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
        HomeContent(
            coins = 1250,
            nickname = "Alex",
            avatarEmoji = ":)",
            completedLessons = listOf("intro")
        )
    }
}
