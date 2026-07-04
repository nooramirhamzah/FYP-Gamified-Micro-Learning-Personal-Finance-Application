package com.company.fyp_prototype.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.fyp_prototype.ui.theme.FYP_PrototypeTheme
import com.company.fyp_prototype.ui.theme.LockedGray
import com.company.fyp_prototype.ui.theme.PrimaryGreen
import com.company.fyp_prototype.ui.viewmodel.UserViewModel

@Composable
fun OnboardingScreen(
    userViewModel: UserViewModel,
    onComplete: () -> Unit = {}
) {
    var nickname by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(avatarOptions.first()) }
    val canContinue = nickname.trim().isNotEmpty()

    OnboardingContent(
        nickname = nickname,
        selectedAvatar = selectedAvatar,
        canContinue = canContinue,
        onNicknameChange = { nickname = it },
        onAvatarSelect = { selectedAvatar = it },
        onContinue = {
            userViewModel.saveUserProfile(
                nickname = nickname,
                avatarEmoji = selectedAvatar,
                hasCompletedOnboarding = true
            )
            onComplete()
        }
    )
}

@Composable
private fun OnboardingContent(
    nickname: String,
    selectedAvatar: String,
    canContinue: Boolean,
    onNicknameChange: (String) -> Unit,
    onAvatarSelect: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Your Profile",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a name and avatar for your financial learning journey.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nickname") },
            singleLine = true,
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Pick an avatar",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            avatarOptions.forEach { avatar ->
                AvatarChoice(
                    avatar = avatar,
                    isSelected = avatar == selectedAvatar,
                    onClick = { onAvatarSelect(avatar) }
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onContinue,
            enabled = canContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                disabledContainerColor = LockedGray
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Start Learning", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun AvatarChoice(
    avatar: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(if (isSelected) PrimaryGreen.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrimaryGreen else LockedGray,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = avatar, fontSize = 28.sp)

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

private val avatarOptions = listOf("🙂", "😎", "🧠", "🌱", "🚀")

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    FYP_PrototypeTheme {
        OnboardingContent(
            nickname = "Alex",
            selectedAvatar = avatarOptions.first(),
            canContinue = true,
            onNicknameChange = {},
            onAvatarSelect = {},
            onContinue = {}
        )
    }
}
