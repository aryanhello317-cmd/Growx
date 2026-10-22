package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "winter_arc_days")
data class DailyWinterArcEntity(
    @PrimaryKey
    val dayNumber: Int, // 1 to 90
    val routineTitle: String,
    val routineDone: Boolean = false,
    val hydrationDone: Boolean = false,
    val mentalGrowthDone: Boolean = false,
    val coldShowerDone: Boolean = false,
    val cleanNutritionDone: Boolean = false,
    val isRestOrRecoveryDay: Boolean = false,
    val completedAt: Long = 0L,
    val notes: String = ""
) {
    val completedTasksCount: Int
        get() = (if (routineDone) 1 else 0) +
                (if (hydrationDone) 1 else 0) +
                (if (mentalGrowthDone) 1 else 0) +
                (if (coldShowerDone) 1 else 0) +
                (if (cleanNutritionDone) 1 else 0)

    val isAllTasksCompleted: Boolean
        get() = if (isRestOrRecoveryDay) (hydrationDone && mentalGrowthDone && cleanNutritionDone) else completedTasksCount == 5
}
