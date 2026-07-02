package com.company.fyp_prototype.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1, // Only one user for this prototype
    val coins: Int = 0,
    val completedLessons: String = "", // Comma separated IDs: "budget,emergency"
    val earnedBadges: String = "", // Comma separated names
    val nickname: String = "",
    val avatarEmoji: String = "🙂",
    val hasCompletedOnboarding: Boolean = false
)
