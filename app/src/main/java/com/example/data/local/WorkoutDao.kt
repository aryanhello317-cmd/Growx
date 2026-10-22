package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC")
    fun getAllWorkoutsFlow(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentWorkoutsFlow(limit: Int = 10): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workout_history WHERE timestamp >= :sinceEpoch ORDER BY timestamp ASC")
    suspend fun getWorkoutsSince(sinceEpoch: Long): List<WorkoutEntity>

    @Query("SELECT SUM(totalReps) FROM workout_history")
    fun getTotalRepsSum(): Flow<Int?>

    @Query("SELECT SUM(durationSeconds) FROM workout_history")
    fun getTotalDurationSum(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM workout_history")
    fun getWorkoutCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Query("DELETE FROM workout_history")
    suspend fun clearHistory()
}
