package com.company.fyp_prototype.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.fyp_prototype.data.local.UserDao
import com.company.fyp_prototype.data.local.UserProgressEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    private val _userProgress = userDao.getUserProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProgressEntity())

    val coins: StateFlow<Int> = _userProgress.map { it?.coins ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedLessons: StateFlow<List<String>> = _userProgress.map { 
        it?.completedLessons?.split(",")?.filter { id -> id.isNotEmpty() } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedLessonSet: StateFlow<Set<String>> = completedLessons.map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val earnedBadges: StateFlow<List<String>> = _userProgress.map { 
        it?.earnedBadges?.split(",")?.filter { name -> name.isNotEmpty() } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val purchasedRewards: StateFlow<Set<String>> = _userProgress.map {
        it?.purchasedRewards?.toMutableCsvSet()?.toSet() ?: emptySet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val activeRewards: StateFlow<Set<String>> = _userProgress.map {
        it?.activeRewards?.toMutableCsvSet()?.toSet() ?: emptySet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val nickname: StateFlow<String> = _userProgress.map { it?.nickname.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val avatarEmoji: StateFlow<String> = _userProgress.map { it?.avatarEmoji ?: "🙂" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "🙂")

    val hasCompletedOnboarding: StateFlow<Boolean> = _userProgress.map { it?.hasCompletedOnboarding == true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun saveUserProfile(nickname: String, avatarEmoji: String, hasCompletedOnboarding: Boolean = true) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            userDao.insertOrUpdate(
                currentProgress.copy(
                    nickname = nickname.trim(),
                    avatarEmoji = avatarEmoji,
                    hasCompletedOnboarding = hasCompletedOnboarding
                )
            )
        }
    }

    fun calculateLessonReward(correctAnswers: Int, totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 0

        val baseReward = correctAnswers * 10
        val accuracy = correctAnswers.toFloat() / totalQuestions.toFloat()
        val performanceBonus = if (accuracy >= 0.80f) 5 else 0

        return baseReward + performanceBonus
    }

    fun addCoins(amount: Int) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val updatedProgress = currentProgress.copy(coins = currentProgress.coins + amount)
            userDao.insertOrUpdate(checkAndUnlockBadges(updatedProgress))
        }
    }

    fun completeLesson(lessonId: String) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val currentLessons = currentProgress.completedLessons.split(",").toMutableList()
            if (!currentLessons.contains(lessonId)) {
                currentLessons.add(lessonId)
                val updatedProgress = currentProgress.copy(
                    completedLessons = currentLessons.filter { it.isNotEmpty() }.joinToString(",")
                )
                userDao.insertOrUpdate(checkAndUnlockBadges(updatedProgress))
            }
        }
    }

    fun completeQuiz(lessonId: String, score: Int, totalQuestions: Int) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val completedLessonIds = currentProgress.completedLessons.toMutableCsvSet()
            completedLessonIds.add(lessonId)
            val reward = calculateLessonReward(score, totalQuestions)

            val updatedProgress = currentProgress.copy(
                coins = currentProgress.coins + reward,
                completedLessons = completedLessonIds.toCsvString()
            )

            userDao.insertOrUpdate(
                checkAndUnlockBadges(
                    progress = updatedProgress,
                    completedQuizLessonId = lessonId,
                    quizScore = score
                )
            )
        }
    }

    fun unlockBadge(badgeName: String) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val badgeNames = currentProgress.earnedBadges.toMutableCsvSet()
            if (badgeNames.add(badgeName)) {
                userDao.insertOrUpdate(currentProgress.copy(earnedBadges = badgeNames.toCsvString()))
            }
        }
    }

    fun checkAndUnlockBadges() {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            userDao.insertOrUpdate(checkAndUnlockBadges(currentProgress))
        }
    }

    fun resetUserProgress() {
        viewModelScope.launch {
            userDao.clearUserProgress()
            userDao.insertOrUpdate(
                UserProgressEntity(
                    coins = 0,
                    completedLessons = "",
                    earnedBadges = "",
                    purchasedRewards = "",
                    activeRewards = "",
                    nickname = "",
                    avatarEmoji = "🙂",
                    hasCompletedOnboarding = false
                )
            )
        }
    }

    private fun checkAndUnlockBadges(
        progress: UserProgressEntity,
        completedQuizLessonId: String? = null,
        quizScore: Int? = null
    ): UserProgressEntity {
        val badgeNames = progress.earnedBadges.toMutableCsvSet()

        if (progress.coins >= 100) {
            badgeNames.add("Thrifty Saver")
        }

        progress.completedLessons.toMutableCsvSet().forEach { lessonId ->
            quizMasterBadgeFor(lessonId)?.let { badgeNames.add(it) }
        }

        if (quizScore != null && quizScore >= 8 && completedQuizLessonId != null) {
            quizMasterBadgeFor(completedQuizLessonId)?.let { badgeNames.add(it) }
        }

        return progress.copy(earnedBadges = badgeNames.toCsvString())
    }

    private fun quizMasterBadgeFor(lessonId: String): String? = when (lessonId) {
        "intro" -> "Money Basics Master"
        "budget" -> "Budget Master"
        "emergency" -> "Emergency Fund Master"
        "high_yield" -> "Savings Master"
        else -> null
    }

    fun buyReward(rewardId: String, cost: Int): Boolean {
        val currentProgress = _userProgress.value ?: UserProgressEntity()
        val ownedRewards = currentProgress.purchasedRewards.toMutableCsvSet()
        val activeRewards = currentProgress.activeRewards.toMutableCsvSet()

        if (rewardId in ownedRewards || currentProgress.coins < cost) {
            return false
        }

        ownedRewards.add(rewardId)
        activeRewards.add(rewardId)
        viewModelScope.launch {
            userDao.insertOrUpdate(
                currentProgress.copy(
                    coins = currentProgress.coins - cost,
                    purchasedRewards = ownedRewards.toCsvString(),
                    activeRewards = activeRewards.toCsvString()
                )
            )
        }
        return true
    }

    fun setRewardActive(rewardId: String, isActive: Boolean) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val ownedRewards = currentProgress.purchasedRewards.toMutableCsvSet()

            if (rewardId !in ownedRewards) return@launch

            val activeRewardIds = currentProgress.activeRewards.toMutableCsvSet()
            if (isActive) {
                activeRewardIds.add(rewardId)
            } else {
                activeRewardIds.remove(rewardId)
            }

            userDao.insertOrUpdate(currentProgress.copy(activeRewards = activeRewardIds.toCsvString()))
        }
    }

    fun hasPurchasedReward(rewardId: String): Boolean {
        return rewardId in getPurchasedRewards()
    }

    fun getPurchasedRewards(): Set<String> {
        return (_userProgress.value ?: UserProgressEntity()).purchasedRewards.toMutableCsvSet()
    }

    fun buyItem(price: Int, itemName: String): Boolean {
        return buyReward(itemName, price)
    }
    
    // Initial data setup if empty
    init {
        viewModelScope.launch {
            userDao.getUserProgress().firstOrNull()?.let {
                // Already exists
            } ?: run {
                userDao.insertOrUpdate(UserProgressEntity())
            }
        }
    }
}

private fun String.toMutableCsvSet(): MutableSet<String> =
    split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toMutableSet()

private fun Set<String>.toCsvString(): String = joinToString(",")
