package com.company.fyp_prototype.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

    PortfolioContent(
        coins = coins,
        unlockedBadges = unlockedBadges,
        onBack = onBack,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortfolioContent(
    coins: Int,
    unlockedBadges: List<String>,
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundWhite)
            )
        },
        bottomBar = { PortfolioBottomNavigationBar(onNavigate) },
        containerColor = BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "TOTAL ASSETS",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Gray,
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
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AssetCard(
                    label = "Unlocked",
                    value = unlockedBadges.size.toString(),
                    unit = "Badges",
                    icon = Icons.Default.MilitaryTech,
                    iconColor = LessonDarkBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CategoryFilters()

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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(standardBadges) { badge ->
                    BadgeCard(
                        badge = badge,
                        isUnlocked = unlockedBadges.contains(badge.name)
                    )
                }
            }
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                Text(label, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
            Text(unit, color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoryFilters() {
    val categories = listOf("All Badges", "Learning", "Savings", "Mastery")
    var selectedCategory by remember { mutableStateOf("All Badges") }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { selectedCategory = category },
                label = { Text(category) },
                shape = RoundedCornerShape(24.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGreen,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Gray
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    disabledSelectedBorderColor = Color.Transparent,
                    borderWidth = 0.dp
                )
            )
        }
    }
}

data class BadgeItem(
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BadgeCard(
    badge: BadgeItem,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    val badgeColor = if (isUnlocked) badge.color else Color.Gray
    val contentAlpha = if (isUnlocked) 1f else 0.3f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                color = if (isUnlocked) TextDark else Color.Gray
            )
            Text(
                text = badge.subtitle,
                color = Color.Gray,
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
fun PortfolioBottomNavigationBar(onNavigate: (String) -> Unit) {
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

@Preview(showBackground = true)
@Composable
fun PortfolioScreenPreview() {
    FYP_PrototypeTheme {
        PortfolioContent(
            coins = 2450,
            unlockedBadges = listOf("Thrifty Saver", "Savings Master")
        )
    }
}
