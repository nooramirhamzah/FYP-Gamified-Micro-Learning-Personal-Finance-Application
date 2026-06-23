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

    val earnedBadges: StateFlow<List<String>> = _userProgress.map { 
        it?.earnedBadges?.split(",")?.filter { name -> name.isNotEmpty() } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCoins(amount: Int) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            userDao.insertOrUpdate(currentProgress.copy(coins = currentProgress.coins + amount))
        }
    }

    fun completeLesson(lessonId: String) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity()
            val currentLessons = currentProgress.completedLessons.split(",").toMutableList()
            if (!currentLessons.contains(lessonId)) {
                currentLessons.add(lessonId)
                userDao.insertOrUpdate(currentProgress.copy(
                    completedLessons = currentLessons.filter { it.isNotEmpty() }.joinToString(",")
                ))
            }
        }
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
