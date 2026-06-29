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

    fun completeQuiz(lessonId: String, score: Int, coinsPerCorrectAnswer: Int = 15) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val completedLessonIds = currentProgress.completedLessons.toMutableCsvSet()
            completedLessonIds.add(lessonId)

            val updatedProgress = currentProgress.copy(
                coins = currentProgress.coins + (score * coinsPerCorrectAnswer),
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
                    earnedBadges = ""
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

    fun buyItem(price: Int, itemName: String): Boolean {
        val currentCoins = coins.value
        if (currentCoins >= price) {
            viewModelScope.launch {
                val currentProgress = _userProgress.value ?: UserProgressEntity()
                userDao.insertOrUpdate(currentProgress.copy(coins = currentCoins - price))
                // Optionally add to badges or owned items if needed
            }
            return true
        }
        return false
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
