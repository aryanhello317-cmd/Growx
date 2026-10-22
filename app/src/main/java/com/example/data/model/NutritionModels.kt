package com.example.data.model

data class AffordableFood(
    val name: String,
    val category: String, // Protein, Carbs, Healthy Fats
    val estimatedCostPerServing: String, // e.g. "$0.30" or "Low"
    val proteinGrams: Float,
    val calories: Int,
    val isVegetarian: Boolean,
    val prepTip: String
)

data class MealSuggestion(
    val mealType: String, // Breakfast, Lunch, Pre-Workout, Post-Workout, Dinner
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val approxProteinGrams: Int,
    val approxCalories: Int,
    val isVegetarian: Boolean,
    val budgetTier: String
)

data class CalorieCalculationResult(
    val isAdolescent: Boolean,
    val maintenanceCalories: Int,
    val muscleGainCalories: Int,
    val fatLossCalories: Int,
    val proteinRecommendationGrams: Int,
    val adolescentAdvice: String = ""
)
