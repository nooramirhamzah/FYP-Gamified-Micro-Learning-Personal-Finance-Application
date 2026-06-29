package com.company.fyp_prototype.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET coins = :newCoins WHERE id = 1")
    suspend fun updateCoins(newCoins: Int)

    @Query("DELETE FROM user_progress")
    suspend fun clearUserProgress()
}
