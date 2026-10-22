package com.example.data.model

data class Exercise(
    val id: String,
    val name: String,
    val category: String, // Push, Pull, Legs, Core, Skills
    val level: String, // Beginner, Intermediate, Advanced
    val sets: Int,
    val repsOrHold: String,
    val restSeconds: Int,
    val targetMuscles: String,
    val instructions: List<String>,
    val formCheckpoints: List<String>,
    val safetyTips: String,
    val easierAlternative: String,
    val isCameraSupported: Boolean = false
)
