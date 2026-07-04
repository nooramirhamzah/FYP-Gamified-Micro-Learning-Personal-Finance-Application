package com.company.fyp_prototype.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val nickname by userViewModel.nickname.collectAsState()
    val avatarEmoji by userViewModel.avatarEmoji.collectAsState()
    val coins by userViewModel.coins.collectAsState()
    val completedLessons by userViewModel.completedLessons.collectAsState()
    val activeRewards by userViewModel.activeRewards.collectAsState()
    val displayName = nickname.ifBlank { "Learner" }
    val hasGoldenFrame = "golden_saver_frame" in activeRewards
    val hasBudgetHeroTitle = "budget_hero_title" in activeRewards
    val hasEmergencyReadyTitle = "emergency_ready_title" in activeRewards
    val profileTitle = when {
        hasEmergencyReadyTitle -> "Emergency Ready"
        hasBudgetHeroTitle -> "Budget Hero"
        else -> "Financial Literacy Enthusiast"
    }
    val profileTitleIcon = when {
        hasEmergencyReadyTitle -> Icons.Default.Shield
        hasBudgetHeroTitle -> Icons.Default.PieChart
        else -> Icons.Default.Stars
    }
    val profileTitleColor = when {
        hasEmergencyReadyTitle -> Color(0xFF00C853)
        hasBudgetHeroTitle -> LessonDarkBlue
        else -> PrimaryGreen
    }
    val fullLeaderboardItems = buildRankedLeaderboard(displayName, coins)
    val leaderboardItems = buildNearbyLeaderboardItems(fullLeaderboardItems)
    var showFullLeaderboard by remember { mutableStateOf(false) }

    if (showFullLeaderboard) {
        FullLeaderboardScreen(
            leaderboardItems = fullLeaderboardItems,
            onBack = { showFullLeaderboard = false },
            onNavigate = onNavigate
        )
    } else {
        Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = { ProfileBottomNavigationBar(onNavigate) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }
            
            // Profile Header
            item {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(
                                width = if (hasGoldenFrame) 6.dp else 4.dp,
                                color = if (hasGoldenFrame) Color(0xFFFFC107) else PrimaryGreen,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasGoldenFrame) Color(0xFFFFF8E1)
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = avatarEmoji,
                            fontSize = 58.sp
                        )
                    }
                    if (hasGoldenFrame) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFC107))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.WorkspacePremium,
                                contentDescription = "Golden frame active",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    displayName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        profileTitleIcon,
                        contentDescription = null,
                        tint = profileTitleColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        profileTitle,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
                if (hasGoldenFrame || hasBudgetHeroTitle || hasEmergencyReadyTitle) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ActiveRewardEffectsRow(
                        hasGoldenFrame = hasGoldenFrame,
                        hasBudgetHeroTitle = hasBudgetHeroTitle,
                        hasEmergencyReadyTitle = hasEmergencyReadyTitle
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
            
            // Stats Row
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        value = completedLessons.size.toString(),
                        label = "LESSONS COMPLETED",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    StatCard(
                        icon = Icons.Default.MonetizationOn,
                        value = formatCoins(coins),
                        label = "COINS EARNED",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
            
            // Leaderboard Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Leaderboard",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { showFullLeaderboard = true }) {
                        Text(
                            "View All >",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            items(leaderboardItems) { item ->
                LeaderboardRow(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                DemoResetSection(
                    onReset = {
                        userViewModel.resetUserProgress()
                        onNavigate("home")
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
    }
}

@Composable
private fun ActiveRewardEffectsRow(
    hasGoldenFrame: Boolean,
    hasBudgetHeroTitle: Boolean,
    hasEmergencyReadyTitle: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasGoldenFrame) {
            RewardEffectChip(
                icon = Icons.Default.WorkspacePremium,
                text = "Golden frame active",
                color = Color(0xFFFFC107)
            )
        }
        if (hasBudgetHeroTitle) {
            RewardEffectChip(
                icon = Icons.Default.PieChart,
                text = "Budget Hero title active",
                color = LessonDarkBlue
            )
        }
        if (hasEmergencyReadyTitle) {
            RewardEffectChip(
                icon = Icons.Default.Shield,
                text = "Emergency Ready title active",
                color = Color(0xFF00C853)
            )
        }
    }
}

@Composable
private fun RewardEffectChip(
    icon: ImageVector,
    text: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DemoResetSection(onReset: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Evaluator Tools",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Reset progress, coins, and badges for a fresh onboarding demo.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD32F2F)
                ),
                border = BorderStroke(1.dp, Color(0xFFD32F2F).copy(alpha = 0.45f)),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset Demo Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullLeaderboardScreen(
    leaderboardItems: List<LeaderboardItemData>,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Leaderboard",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = { ProfileBottomNavigationBar(onNavigate) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(
                    text = "Local demo ranking",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your row uses your saved Room profile and coin total. Other learners are simulated peers for presentation.",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            items(leaderboardItems) { item ->
                LeaderboardRow(item)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

data class LeaderboardItemData(val rank: Int, val name: String, val coins: Int, val isUser: Boolean)

private data class PeerProfile(val name: String, val coins: Int, val isUser: Boolean = false)

private fun buildRankedLeaderboard(userName: String, userCoins: Int): List<LeaderboardItemData> {
    val profiles = listOf(
        PeerProfile("Marcus Vance", 1220),
        PeerProfile("Elena Rossi", 980),
        PeerProfile("Priya Tan", 740),
        PeerProfile("Noah Lim", 520),
        PeerProfile("Sara Wong", 310),
        PeerProfile(userName, userCoins, isUser = true),
        PeerProfile("Daniel Cho", 120),
        PeerProfile("Maya Lee", 60)
    ).sortedByDescending { it.coins }

    return profiles.mapIndexed { index, profile ->
        LeaderboardItemData(
            rank = index + 1,
            name = if (profile.isUser) "You (${profile.name})" else profile.name,
            coins = profile.coins,
            isUser = profile.isUser
        )
    }
}

private fun buildNearbyLeaderboardItems(rankedProfiles: List<LeaderboardItemData>): List<LeaderboardItemData> {
    val userIndex = rankedProfiles.indexOfFirst { it.isUser }.coerceAtLeast(0)
    val endIndex = (userIndex + 2).coerceAtMost(rankedProfiles.size)
    val startIndex = (endIndex - 3).coerceAtLeast(0)

    return rankedProfiles.subList(startIndex, endIndex)
}

private fun formatCoins(value: Int): String = "%,d".format(value)

@Composable
fun LeaderboardRow(item: LeaderboardItemData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (item.isUser) PrimaryGreen else MaterialTheme.colorScheme.surface,
        shadowElevation = if (item.isUser) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.rank.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (item.isUser) Color.White else PrimaryGreen,
                modifier = Modifier.width(30.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (item.isUser) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (item.isUser) Color.White else PrimaryGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = if (item.isUser) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (item.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = if (item.isUser) Color.White else Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatCoins(item.coins),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (item.isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Coins",
                    fontSize = 12.sp,
                    color = if (item.isUser) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
fun ProfileBottomNavigationBar(onNavigate: (String) -> Unit) {
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
            val selected = route == "profile"
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
fun ProfileScreenPreview() {
    FYP_PrototypeTheme {
        DemoResetSection(onReset = {})
    }
}
