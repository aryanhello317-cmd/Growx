package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = "user_default",
    val email: String,
    val passwordHash: String,
    val pinHash: String = "",
    val isAppLockEnabled: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isRememberMe: Boolean = true,
    val age: Int = 20,
    val experienceLevel: String = "Beginner", // Beginner, Intermediate, Advanced
    val heightCm: Float = 175f,
    val weightKg: Float = 70f,
    val fitnessGoal: String = "Strength", // Strength, General Fitness, Muscle Development, Skill Training
    val availableTimeMinutes: Int = 30, // 15, 30, 45, 60
    val equipment: String = "Bodyweight Only", // Bodyweight Only, Pull-up Bar, Parallel Bars, Resistance Bands
    val budgetTier: String = "Low Budget", // Low Budget, Moderate, Flexible
    val isVegetarian: Boolean = false,
    val currentRank: String = "Rookie",
    val xp: Int = 100,
    val streakDays: Int = 1,
    val winterArcDay: Int = 1,
    val winterArcStartedDate: Long = System.currentTimeMillis(),
    val totalWorkoutsCompleted: Int = 0,
    val totalPushUpReps: Int = 0,
    val lastWorkoutDate: Long = 0L,
    val isOnboarded: Boolean = false
)
