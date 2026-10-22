package com.example

import com.example.data.local.DefaultExercises
import com.example.data.local.NutritionCalculator
import com.example.data.local.SecurityHelper
import com.example.data.repository.BuildXRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testSecurityHelperHashAndVerification() {
        val secret = "my_secure_pin_1234"
        val hash = SecurityHelper.hashSecret(secret)
        assertTrue(hash.isNotEmpty())
        assertTrue(SecurityHelper.verifySecret(secret, hash))
        assertFalse(SecurityHelper.verifySecret("wrong_secret", hash))
    }

    @Test
    fun testRankProgressionTiers() {
        assertEquals("Rookie", BuildXRepository.determineRank(0))
        assertEquals("Rookie", BuildXRepository.determineRank(499))
        assertEquals("Beginner", BuildXRepository.determineRank(500))
        assertEquals("Warrior", BuildXRepository.determineRank(1500))
        assertEquals("Elite", BuildXRepository.determineRank(3500))
        assertEquals("Master", BuildXRepository.determineRank(7000))
        assertEquals("Apex", BuildXRepository.determineRank(12000))
        assertEquals("Apex", BuildXRepository.determineRank(25000))
    }

    @Test
    fun testAdolescentNutritionSafetyGuard() {
        val teenResult = NutritionCalculator.calculateNutrition(
            age = 15,
            heightCm = 168f,
            weightKg = 58f,
            goal = "Muscle Development"
        )
        assertTrue(teenResult.isAdolescent)
        assertTrue(teenResult.adolescentAdvice.contains("Adolescent Nutrition Protocol"))
        assertEquals(0, teenResult.maintenanceCalories)
        assertTrue(teenResult.proteinRecommendationGrams >= 65)

        val adultResult = NutritionCalculator.calculateNutrition(
            age = 22,
            heightCm = 175f,
            weightKg = 70f,
            goal = "Strength"
        )
        assertFalse(adultResult.isAdolescent)
        assertTrue(adultResult.maintenanceCalories > 1500)
        assertTrue(adultResult.muscleGainCalories > adultResult.maintenanceCalories)
    }

    @Test
    fun testExerciseLibraryCompleteness() {
        val exercises = DefaultExercises.list
        assertTrue(exercises.isNotEmpty())
        exercises.forEach { ex ->
            assertTrue(ex.name.isNotBlank())
            assertTrue(ex.instructions.isNotEmpty())
            assertTrue(ex.safetyTips.isNotBlank())
            assertTrue(ex.easierAlternative.isNotBlank())
        }
    }
}
