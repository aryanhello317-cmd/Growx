package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String = "user_default"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String = "user_default"): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET xp = xp + :xpDelta, totalWorkoutsCompleted = totalWorkoutsCompleted + 1 WHERE id = :userId")
    suspend fun addXpAndIncrementWorkout(userId: String = "user_default", xpDelta: Int)

    @Query("UPDATE users SET totalPushUpReps = totalPushUpReps + :reps WHERE id = :userId")
    suspend fun addPushUpReps(userId: String = "user_default", reps: Int)

    @Query("UPDATE users SET isAppLockEnabled = :enabled, pinHash = :pinHash WHERE id = :userId")
    suspend fun updateAppLock(userId: String = "user_default", enabled: Boolean, pinHash: String)

    @Query("UPDATE users SET isBiometricEnabled = :enabled WHERE id = :userId")
    suspend fun updateBiometric(userId: String = "user_default", enabled: Boolean)

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}
