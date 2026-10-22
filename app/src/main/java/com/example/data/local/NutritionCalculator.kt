package com.example.data.local

import com.example.data.model.AffordableFood
import com.example.data.model.CalorieCalculationResult
import com.example.data.model.MealSuggestion

object NutritionCalculator {

    fun calculateNutrition(
        age: Int,
        heightCm: Float,
        weightKg: Float,
        goal: String
    ): CalorieCalculationResult {
        if (age < 18) {
            return CalorieCalculationResult(
                isAdolescent = true,
                maintenanceCalories = 0,
                muscleGainCalories = 0,
                fatLossCalories = 0,
                proteinRecommendationGrams = (weightKg * 1.5f).toInt().coerceAtLeast(65),
                adolescentAdvice = "Adolescent Nutrition Protocol: At age $age, your body is actively developing bones, tendons, and muscular foundations. Strictly avoid calorie restriction, severe cutting, or excessive bulking diets. Prioritize whole nutrient-dense foods, adequate hydration (2.5L+ water), 7-9 hours of sleep, and protein from wholesome sources (eggs, milk, lentils, peanut butter) to fuel natural growth."
            )
        }

        // Adult Mifflin-St Jeor estimate (using athletic baseline)
        val bmr = (10f * weightKg) + (6.25f * heightCm) - (5f * age) + 5f
        val tdee = (bmr * 1.4f).toInt() // Active calisthenics factor
        val muscleGain = tdee + 350
        val fatLoss = (tdee - 400).coerceAtLeast(1500)
        val proteinTarget = (weightKg * 1.8f).toInt()

        return CalorieCalculationResult(
            isAdolescent = false,
            maintenanceCalories = tdee,
            muscleGainCalories = muscleGain,
            fatLossCalories = fatLoss,
            proteinRecommendationGrams = proteinTarget,
            adolescentAdvice = ""
        )
    }

    val affordableStaples: List<AffordableFood> = listOf(
        AffordableFood(
            name = "Whole Eggs",
            category = "Protein & Healthy Fats",
            estimatedCostPerServing = "$0.35 / 2 eggs",
            proteinGrams = 12f,
            calories = 140,
            isVegetarian = true,
            prepTip = "Hard-boil a batch of 6 for rapid post-workout protein snacking."
        ),
        AffordableFood(
            name = "Rolled Oats",
            category = "Complex Carbs & Fiber",
            estimatedCostPerServing = "$0.20 / cup",
            proteinGrams = 6f,
            calories = 150,
            isVegetarian = true,
            prepTip = "Cook with water or milk, top with a spoonful of peanut butter."
        ),
        AffordableFood(
            name = "Lentils / Dals (Yellow/Brown)",
            category = "Plant Protein & Complex Carbs",
            estimatedCostPerServing = "$0.25 / serving",
            proteinGrams = 18f,
            calories = 230,
            isVegetarian = true,
            prepTip = "Simmer with garlic, cumin, and turmeric; pair with brown rice."
        ),
        AffordableFood(
            name = "Peanut Butter (100% Roasted)",
            category = "Dense Energy & Healthy Fats",
            estimatedCostPerServing = "$0.25 / 2 tbsp",
            proteinGrams = 8f,
            calories = 190,
            isVegetarian = true,
            prepTip = "Spread on whole wheat toast or mix directly into hot oatmeal."
        ),
        AffordableFood(
            name = "Canned Tuna or Chicken Breast",
            category = "Lean Complete Protein",
            estimatedCostPerServing = "$1.10 / can",
            proteinGrams = 28f,
            calories = 130,
            isVegetarian = false,
            prepTip = "Drain and toss with chopped cucumber, lemon juice, and black pepper."
        ),
        AffordableFood(
            name = "Soya Chunks / TVP",
            category = "High-Density Protein",
            estimatedCostPerServing = "$0.20 / 50g",
            proteinGrams = 26f,
            calories = 175,
            isVegetarian = true,
            prepTip = "Boil in salted water for 5 min, squeeze out moisture, and pan-sauté."
        ),
        AffordableFood(
            name = "Curd / Plain Greek Yogurt",
            category = "Protein & Probiotics",
            estimatedCostPerServing = "$0.50 / cup",
            proteinGrams = 15f,
            calories = 130,
            isVegetarian = true,
            prepTip = "Eat with a sliced banana for an ideal pre-workout digestion boost."
        ),
        AffordableFood(
            name = "Bananas",
            category = "Fast Glycogen & Potassium",
            estimatedCostPerServing = "$0.18 each",
            proteinGrams = 1.3f,
            calories = 105,
            isVegetarian = true,
            prepTip = "Eat 30 minutes before training for quick, clean energy and muscle cramp prevention."
        )
    )

    val sampleMeals: List<MealSuggestion> = listOf(
        MealSuggestion(
            mealType = "Breakfast",
            title = "Power Oatmeal with Peanut Butter & Banana",
            description = "Sustained carbohydrate release paired with healthy monounsaturated fats.",
            ingredients = listOf("1 cup Rolled Oats", "2 tbsp Peanut Butter", "1 Banana", "Pinch of Cinnamon"),
            approxProteinGrams = 16,
            approxCalories = 420,
            isVegetarian = true,
            budgetTier = "Low Budget"
        ),
        MealSuggestion(
            mealType = "Pre-Workout",
            title = "Banana & Salted Toast / Curd",
            description = "Rapid muscle glycogen top-off with electrolytes to prevent mid-set cramping.",
            ingredients = listOf("1 Large Ripe Banana", "1 Slice Toast with Honey", "1 Glass Water + Light Salt"),
            approxProteinGrams = 6,
            approxCalories = 220,
            isVegetarian = true,
            budgetTier = "Low Budget"
        ),
        MealSuggestion(
            mealType = "Post-Workout",
            title = "3 Scrambled Eggs with Steamed Potatoes",
            description = "Optimal bioavailable amino acids and fast glycogen replenishment without costly shakes.",
            ingredients = listOf("3 Whole Eggs", "2 Medium Boiled/Air-Fried Potatoes", "Spinach or Onions"),
            approxProteinGrams = 22,
            approxCalories = 380,
            isVegetarian = true,
            budgetTier = "Low Budget"
        ),
        MealSuggestion(
            mealType = "Lunch / Dinner",
            title = "High-Protein Lentil / Soya Bowl with Rice",
            description = "Complete amino acid profile from staple legumes, extremely budget-friendly.",
            ingredients = listOf("1.5 cups Cooked Lentils/Dal or Soya Chunks", "1 cup Rice", "Tomato-Garlic Tadka"),
            approxProteinGrams = 28,
            approxCalories = 480,
            isVegetarian = true,
            budgetTier = "Low Budget"
        ),
        MealSuggestion(
            mealType = "Post-Workout (Non-Veg)",
            title = "Tuna & Chickpea Salad Bowl",
            description = "Massive 36g protein punch from affordable canned tuna and tender chickpeas.",
            ingredients = listOf("1 Can Chunk Light Tuna", "0.5 cup Canned Chickpeas", "Olive Oil / Lemon"),
            approxProteinGrams = 36,
            approxCalories = 340,
            isVegetarian = false,
            budgetTier = "Moderate"
        )
    )
}
