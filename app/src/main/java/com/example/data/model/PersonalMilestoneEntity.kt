package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_milestones")
data class PersonalMilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetRepsOrDays: Int,
    val currentProgress: Int = 0,
    val targetDay: Int = 30, // By which Winter Arc day
    val isAchieved: Boolean = false,
    val dateAchieved: Long = 0L
)
