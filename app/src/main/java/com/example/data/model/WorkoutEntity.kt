package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // Push, Pull, Legs, Core, Skills, Camera Push-ups
    val durationSeconds: Int,
    val totalReps: Int,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val formAccuracyRate: Float = 1.0f,
    val notes: String = ""
)
