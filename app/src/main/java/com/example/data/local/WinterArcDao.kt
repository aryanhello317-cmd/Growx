package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyWinterArcEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinterArcDao {
    @Query("SELECT * FROM winter_arc_days ORDER BY dayNumber ASC")
    fun getAllDaysFlow(): Flow<List<DailyWinterArcEntity>>

    @Query("SELECT * FROM winter_arc_days WHERE dayNumber = :dayNumber LIMIT 1")
    fun getDayFlow(dayNumber: Int): Flow<DailyWinterArcEntity?>

    @Query("SELECT * FROM winter_arc_days WHERE dayNumber = :dayNumber LIMIT 1")
    suspend fun getDay(dayNumber: Int): DailyWinterArcEntity?

    @Query("SELECT COUNT(*) FROM winter_arc_days WHERE routineDone = 1 OR isRestOrRecoveryDay = 1")
    fun getCompletedDaysCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days: List<DailyWinterArcEntity>)

    @Update
    suspend fun updateDay(day: DailyWinterArcEntity)

    @Query("UPDATE winter_arc_days SET routineDone = :routineDone, hydrationDone = :hydration, mentalGrowthDone = :mental, coldShowerDone = :cold, cleanNutritionDone = :nutrition, completedAt = :completedAt WHERE dayNumber = :dayNumber")
    suspend fun updateTasks(
        dayNumber: Int,
        routineDone: Boolean,
        hydration: Boolean,
        mental: Boolean,
        cold: Boolean,
        nutrition: Boolean,
        completedAt: Long
    )

    @Query("UPDATE winter_arc_days SET isRestOrRecoveryDay = :isRest WHERE dayNumber = :dayNumber")
    suspend fun setRestDay(dayNumber: Int, isRest: Boolean)

    @Query("DELETE FROM winter_arc_days")
    suspend fun clearDays()
}
