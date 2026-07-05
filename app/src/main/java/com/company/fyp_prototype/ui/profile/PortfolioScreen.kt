package com.company.fyp_prototype.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.*
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val coins by userViewModel.coins.collectAsState()
    val unlockedBadges by userViewModel.earnedBadges.collectAsState()
    val purchasedRewards by userViewModel.purchasedRewards.collectAsState()
    val activeRewards by userViewModel.activeRewards.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.checkAndUnlockBadges()
    }

    PortfolioContent(
        coins = coins,
        unlockedBadges = unlockedBadges,
        purchasedRewards = purchasedRewards,
        activeRewards = activeRewards,
        onBuyReward = userViewModel::buyReward,
        onSetRewardActive = userViewModel::setRewardActive,
        onBack = onBack,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortfolioContent(
    coins: Int,
    unlockedBadges: List<String>,
    purchasedRewards: Set<String>,
    activeRewards: Set<String>,
    onBuyReward: (String, Int) -> Boolean = { _, _ -> false },
    onSetRewardActive: (String, Boolean) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val isDarkModeActive = "dark_mode_theme" in activeRewards
    val screenBackground = if (isDarkModeActive) MaterialTheme.colorScheme.background else BackgroundWhite
    val mutedTextColor = if (isDarkModeActive) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f) else TextGray

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Portfolio and Reward Store",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = screenBackground)
            )
        },
        bottomBar = { PortfolioBottomNavigationBar(onNavigate, isDarkModeActive) },
        containerColor = screenBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "TOTAL ASSETS",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = mutedTextColor,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                AssetCard(
                    label = "Available",
                    value = coins.toString(),
                    unit = "Coins",
                    icon = Icons.Default.MonetizationOn,
                    iconColor = PrimaryGreen,
                    isDarkMode = isDarkModeActive,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AssetCard(
                    label = "Unlocked",
                    value = unlockedBadges.size.toString(),
                    unit = "Badges",
                    icon = Icons.Default.MilitaryTech,
                    iconColor = LessonDarkBlue,
                    isDarkMode = isDarkModeActive,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Badge Collection",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "${unlockedBadges.size}/${standardBadges.size}",
                    color = PrimaryGreen,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            standardBadges.chunked(2).forEach { rowBadges ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowBadges.forEach { badge ->
                        BadgeCard(
                            badge = badge,
                            isUnlocked = unlockedBadges.contains(badge.name),
                            isDarkMode = isDarkModeActive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowBadges.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Reward Store",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Spend Game Coins on virtual rewards for your learning profile.",
                color = mutedTextColor,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            rewardStoreItems.forEach { reward ->
                RewardStoreCard(
                    reward = reward,
                    coins = coins,
                    isOwned = reward.id in purchasedRewards,
                    isActive = reward.id in activeRewards,
                    isDarkMode = isDarkModeActive,
                    onBuy = { onBuyReward(reward.id, reward.cost) },
                    onActiveChange = { isActive -> onSetRewardActive(reward.id, isActive) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Owned Rewards",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OwnedRewardsSection(
                purchasedRewards = purchasedRewards,
                activeRewards = activeRewards,
                isDarkMode = isDarkModeActive
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AssetCard(
    label: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isDarkMode) MaterialTheme.colorScheme.surface else Color.White
    val titleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface else TextDark
    val labelColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f) else Color.Gray

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, color = labelColor, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )
            Text(unit, color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

data class BadgeItem(
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

data class RewardStoreItem(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BadgeCard(
    badge: BadgeItem,
    isUnlocked: Boolean,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val badgeColor = if (isUnlocked) badge.color else Color.Gray
    val contentAlpha = if (isUnlocked) 1f else 0.3f
    val cardColor = if (isDarkMode) MaterialTheme.colorScheme.surface else Color.White
    val titleColor = when {
        !isUnlocked -> Color.Gray
        isDarkMode -> MaterialTheme.colorScheme.onSurface
        else -> TextDark
    }
    val subtitleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f) else Color.Gray

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeColor.copy(alpha = if (isUnlocked) 0.12f else 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badge.icon,
                    contentDescription = badge.name,
                    modifier = Modifier
                        .size(64.dp)
                        .alpha(contentAlpha),
                    tint = badgeColor
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = if (isUnlocked) SecondaryGreen else Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (isUnlocked) "UNLOCKED" else "LOCKED",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) DarkGreen else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = badge.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = titleColor
            )
            Text(
                text = badge.subtitle,
                color = subtitleColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isUnlocked) "Achieved" else "Not yet achieved",
                    color = badgeColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun RewardStoreCard(
    reward: RewardStoreItem,
    coins: Int,
    isOwned: Boolean,
    isActive: Boolean,
    isDarkMode: Boolean = false,
    onBuy: () -> Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var feedbackMessage by remember(reward.id, isOwned) { mutableStateOf<String?>(null) }
    val canBuy = coins >= reward.cost && !isOwned
    val cardColor = if (isDarkMode) MaterialTheme.colorScheme.surface else Color.White
    val titleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface else TextDark
    val subtitleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f) else TextGray

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(reward.color.copy(alpha = if (isOwned) 0.16f else 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = reward.icon,
                    contentDescription = null,
                    tint = reward.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = titleColor
                )
                Text(
                    text = reward.description,
                    color = subtitleColor,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${reward.cost} coins",
                        color = PrimaryGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                feedbackMessage?.let { message ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message,
                        color = if (isOwned) PrimaryGreen else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (isOwned) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Switch(
                        checked = isActive,
                        onCheckedChange = onActiveChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryGreen,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFBDBDBD)
                        )
                    )
                    Text(
                        text = if (isActive) "On" else "Off",
                        color = if (isActive) PrimaryGreen else subtitleColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = {
                        feedbackMessage = if (onBuy()) {
                            "Purchased"
                        } else {
                            "Unable to buy"
                        }
                    },
                    enabled = canBuy,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE0E0E0),
                        disabledContentColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (canBuy) "Buy" else "Not enough",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OwnedRewardsSection(
    purchasedRewards: Set<String>,
    activeRewards: Set<String>,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val ownedRewards = rewardStoreItems.filter { it.id in purchasedRewards }
    val cardColor = if (isDarkMode) MaterialTheme.colorScheme.surface else Color.White
    val titleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface else TextDark
    val subtitleColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f) else TextGray

    if (ownedRewards.isEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "No reward items owned yet.",
                    color = subtitleColor,
                    fontSize = 13.sp
                )
            }
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ownedRewards.forEach { reward ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = reward.icon,
                    contentDescription = null,
                    tint = reward.color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${reward.name} - ${if (reward.id in activeRewards) "On" else "Off"}",
                    color = titleColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PortfolioBottomNavigationBar(
    onNavigate: (String) -> Unit,
    isDarkMode: Boolean = false
) {
    val containerColor = if (isDarkMode) MaterialTheme.colorScheme.surface else Color.White
    val inactiveColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f) else TextGray

    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Learn", Icons.Default.School, "home"),
            Triple("Portfolio", Icons.Default.PieChart, "portfolio"),
            Triple("Profile", Icons.Default.Person, "profile")
        )

        items.forEach { (label, icon, route) ->
            val selected = route == "portfolio"
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
                        Icon(icon, contentDescription = label, tint = inactiveColor)
                    }
                },
                label = {
                    Text(
                        label,
                        color = if (selected) PrimaryGreen else inactiveColor,
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

private val standardBadges = listOf(
    BadgeItem(
        name = "Thrifty Saver",
        subtitle = "Reach 100 total coins.",
        icon = Icons.Default.Savings,
        color = Color(0xFFFFD700)
    ),
    BadgeItem(
        name = "Money Basics Master",
        subtitle = "Score 8+ on Intro to Money.",
        icon = Icons.Default.School,
        color = PrimaryGreen
    ),
    BadgeItem(
        name = "Budget Master",
        subtitle = "Score 8+ on budgeting.",
        icon = Icons.Default.PieChart,
        color = LessonDarkBlue
    ),
    BadgeItem(
        name = "Emergency Fund Master",
        subtitle = "Score 8+ on emergency funds.",
        icon = Icons.Default.Shield,
        color = Color(0xFF00C853)
    ),
    BadgeItem(
        name = "Savings Master",
        subtitle = "Score 8+ on high-yield savings.",
        icon = Icons.AutoMirrored.Filled.TrendingUp,
        color = Color(0xFF4CAF50)
    )
)

private val rewardStoreItems = listOf(
    RewardStoreItem(
        id = "golden_saver_frame",
        name = "Golden Saver Badge Frame",
        description = "Adds a gold premium frame around your profile avatar.",
        cost = 50,
        icon = Icons.Default.WorkspacePremium,
        color = Color(0xFFFFC107)
    ),
    RewardStoreItem(
        id = "budget_hero_title",
        name = "Budget Hero Title",
        description = "Changes your profile title to Budget Hero.",
        cost = 75,
        icon = Icons.Default.PieChart,
        color = LessonDarkBlue
    ),
    RewardStoreItem(
        id = "emergency_ready_title",
        name = "Emergency Ready Title",
        description = "Changes your profile title to Emergency Ready.",
        cost = 100,
        icon = Icons.Default.Shield,
        color = Color(0xFF00C853)
    ),
    RewardStoreItem(
        id = "dark_mode_theme",
        name = "Dark Mode Theme",
        description = "Switch the app into a darker interface after purchase.",
        cost = 130,
        icon = Icons.Default.DarkMode,
        color = Color(0xFF5C6BC0)
    ),
    RewardStoreItem(
        id = "double_coin_multiplier",
        name = "Double Coin Multiplier",
        description = "Doubles coins earned from completed quizzes while active.",
        cost = 200,
        icon = Icons.Default.Bolt,
        color = Color(0xFFFF9800)
    )
)

@Preview(showBackground = true)
@Composable
fun PortfolioScreenPreview() {
    FYP_PrototypeTheme {
        PortfolioContent(
            coins = 2450,
            unlockedBadges = listOf("Thrifty Saver", "Savings Master"),
            purchasedRewards = setOf("golden_saver_frame", "budget_hero_title", "dark_mode_theme", "double_coin_multiplier"),
            activeRewards = setOf("dark_mode_theme", "double_coin_multiplier")
        )
    }
}
