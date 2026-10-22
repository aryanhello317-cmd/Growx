package com.example.data.repository

import com.example.data.local.BuildXDatabase
import com.example.data.local.SecurityHelper
import com.example.data.model.DailyWinterArcEntity
import com.example.data.model.PersonalMilestoneEntity
import com.example.data.model.UserEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.roundToInt

class BuildXRepository(private val database: BuildXDatabase) {
    private val userDao = database.userDao()
    private val workoutDao = database.workoutDao()
    private val winterArcDao = database.winterArcDao()
    private val milestoneDao = database.milestoneDao()

    val userFlow: Flow<UserEntity?> = userDao.getUserFlow()
    val allWorkoutsFlow: Flow<List<WorkoutEntity>> = workoutDao.getAllWorkoutsFlow()
    val recentWorkoutsFlow: Flow<List<WorkoutEntity>> = workoutDao.getRecentWorkoutsFlow(10)
    val winterArcDaysFlow: Flow<List<DailyWinterArcEntity>> = winterArcDao.getAllDaysFlow()
    val completedDaysCountFlow: Flow<Int> = winterArcDao.getCompletedDaysCount()
    val milestonesFlow: Flow<List<PersonalMilestoneEntity>> = milestoneDao.getAllMilestonesFlow()
    val totalRepsFlow: Flow<Int?> = workoutDao.getTotalRepsSum()
    val totalDurationFlow: Flow<Int?> = workoutDao.getTotalDurationSum()
    val workoutCountFlow: Flow<Int> = workoutDao.getWorkoutCount()

    suspend fun getOrCreateDefaultUser(): UserEntity {
        var user = userDao.getUser()
        if (user == null) {
            val defaultPass = SecurityHelper.hashSecret("buildx123")
            user = UserEntity(
                id = "user_default",
                email = "athlete@buildx.app",
                passwordHash = defaultPass,
                pinHash = "",
                isAppLockEnabled = false,
                isBiometricEnabled = false,
                isRememberMe = true,
                age = 22,
                experienceLevel = "Beginner",
                heightCm = 175f,
                weightKg = 70f,
                fitnessGoal = "Strength",
                availableTimeMinutes = 30,
                equipment = "Bodyweight Only",
                budgetTier = "Low Budget",
                isVegetarian = false,
                currentRank = "Rookie",
                xp = 150,
                streakDays = 3,
                winterArcDay = 1,
                winterArcStartedDate = System.currentTimeMillis(),
                totalWorkoutsCompleted = 1,
                totalPushUpReps = 25,
                lastWorkoutDate = System.currentTimeMillis(),
                isOnboarded = true
            )
            userDao.insertUser(user)
            initializeWinterArcDays()
            initializeDefaultMilestones()
        }
        return user
    }

    suspend fun initializeWinterArcDays() {
        val routines = listOf(
            "Chest & Triceps Push Foundation",
            "Core Rigidity & Hollow Body Control",
            "Lower Body Knee & Hip Strength",
            "Back & Scapular Pull Mechanics",
            "Shoulder Mobility & Handstand Prep",
            "Active Recovery & Joint Decompression",
            "Push-Up Max Density & Form Check"
        )

        val days = (1..90).map { dayNum ->
            val routineName = if (dayNum % 7 == 6) {
                "Active Rest & Joint Recovery"
            } else {
                routines[(dayNum - 1) % routines.size]
            }
            DailyWinterArcEntity(
                dayNumber = dayNum,
                routineTitle = routineName,
                isRestOrRecoveryDay = (dayNum % 7 == 6),
                routineDone = dayNum == 1,
                hydrationDone = dayNum == 1,
                mentalGrowthDone = dayNum == 1,
                coldShowerDone = false,
                cleanNutritionDone = dayNum == 1,
                completedAt = if (dayNum == 1) System.currentTimeMillis() else 0L
            )
        }
        winterArcDao.insertDays(days)
    }

    suspend fun initializeDefaultMilestones() {
        val defaults = listOf(
            PersonalMilestoneEntity(
                title = "Hit 50 Push-Ups in a Single Session",
                targetRepsOrDays = 50,
                currentProgress = 25,
                targetDay = 15,
                isAchieved = false
            ),
            PersonalMilestoneEntity(
                title = "Complete 30 Consecutive Winter Arc Days",
                targetRepsOrDays = 30,
                currentProgress = 3,
                targetDay = 30,
                isAchieved = false
            ),
            PersonalMilestoneEntity(
                title = "Hold 60-Second Perfect Hollow Body",
                targetRepsOrDays = 60,
                currentProgress = 30,
                targetDay = 45,
                isAchieved = false
            ),
            PersonalMilestoneEntity(
                title = "Reach 1,000 Total Push-Up Reps",
                targetRepsOrDays = 1000,
                currentProgress = 25,
                targetDay = 75,
                isAchieved = false
            ),
            PersonalMilestoneEntity(
                title = "Ascend to Apex Rank (12,000 XP)",
                targetRepsOrDays = 12000,
                currentProgress = 150,
                targetDay = 90,
                isAchieved = false
            )
        )
        milestoneDao.insertMilestones(defaults)
    }

    suspend fun registerUser(email: String, plainPassword: String): Result<UserEntity> {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val newUser = UserEntity(
            id = "user_default",
            email = email,
            passwordHash = SecurityHelper.hashSecret(plainPassword),
            pinHash = "",
            isAppLockEnabled = false,
            isRememberMe = true,
            isOnboarded = false,
            xp = 100,
            currentRank = "Rookie"
        )
        userDao.insertUser(newUser)
        initializeWinterArcDays()
        initializeDefaultMilestones()
        return Result.success(newUser)
    }

    suspend fun loginUser(email: String, plainPassword: String): Result<UserEntity> {
        val user = userDao.getUser() ?: return Result.failure(Exception("No user profile found. Please register."))
        if (user.email.equals(email, ignoreCase = true) &&
            SecurityHelper.verifySecret(plainPassword, user.passwordHash)
        ) {
            return Result.success(user)
        }
        // Allow fallback matching default demo account if password matches
        if (SecurityHelper.verifySecret(plainPassword, user.passwordHash)) {
            return Result.success(user)
        }
        return Result.failure(Exception("Invalid email or password."))
    }

    suspend fun resetPassword(email: String, newPassword: String): Boolean {
        val user = userDao.getUser() ?: return false
        val updated = user.copy(passwordHash = SecurityHelper.hashSecret(newPassword))
        userDao.updateUser(updated)
        return true
    }

    suspend fun verifyPin(pin: String): Boolean {
        val user = userDao.getUser() ?: return false
        if (user.pinHash.isBlank()) return true
        return SecurityHelper.verifySecret(pin, user.pinHash)
    }

    suspend fun setAppLock(enabled: Boolean, pin: String) {
        val pinHash = if (pin.isNotBlank()) SecurityHelper.hashSecret(pin) else ""
        userDao.updateAppLock("user_default", enabled, pinHash)
    }

    suspend fun toggleBiometric(enabled: Boolean) {
        userDao.updateBiometric("user_default", enabled)
    }

    suspend fun updateUserProfile(updated: UserEntity) {
        val rankedUser = calculateRankAndUpdate(updated)
        userDao.updateUser(rankedUser)
    }

    suspend fun logCompletedWorkout(
        title: String,
        category: String,
        durationSeconds: Int,
        totalReps: Int,
        xpEarned: Int,
        formAccuracyRate: Float = 1.0f,
        notes: String = ""
    ) {
        val workout = WorkoutEntity(
            title = title,
            category = category,
            durationSeconds = durationSeconds,
            totalReps = totalReps,
            xpEarned = xpEarned,
            timestamp = System.currentTimeMillis(),
            formAccuracyRate = formAccuracyRate,
            notes = notes
        )
        workoutDao.insertWorkout(workout)

        val currentUser = userDao.getUser() ?: return
        val newXp = currentUser.xp + xpEarned
        val newReps = currentUser.totalPushUpReps + if (category.contains("Push", ignoreCase = true)) totalReps else 0
        val newTotalWorkouts = currentUser.totalWorkoutsCompleted + 1
        
        // Calculate rank
        val newRank = determineRank(newXp)
        val updatedUser = currentUser.copy(
            xp = newXp,
            currentRank = newRank,
            totalPushUpReps = newReps,
            totalWorkoutsCompleted = newTotalWorkouts,
            lastWorkoutDate = System.currentTimeMillis()
        )
        userDao.updateUser(updatedUser)
    }

    suspend fun updateWinterArcTasks(
        dayNumber: Int,
        routine: Boolean,
        hydration: Boolean,
        mental: Boolean,
        cold: Boolean,
        nutrition: Boolean
    ) {
        winterArcDao.updateTasks(
            dayNumber = dayNumber,
            routineDone = routine,
            hydration = hydration,
            mental = mental,
            cold = cold,
            nutrition = nutrition,
            completedAt = System.currentTimeMillis()
        )
        // Award XP for newly checked tasks
        val user = userDao.getUser() ?: return
        val updatedUser = user.copy(xp = user.xp + 25)
        userDao.updateUser(calculateRankAndUpdate(updatedUser))
    }

    suspend fun toggleRestDay(dayNumber: Int, isRest: Boolean) {
        winterArcDao.setRestDay(dayNumber, isRest)
    }

    suspend fun addMilestone(title: String, target: Int, targetDay: Int) {
        milestoneDao.insertMilestone(
            PersonalMilestoneEntity(
                title = title,
                targetRepsOrDays = target,
                currentProgress = 0,
                targetDay = targetDay,
                isAchieved = false
            )
        )
    }

    suspend fun toggleMilestoneAchieved(id: Long, achieved: Boolean) {
        milestoneDao.toggleAchieved(
            id = id,
            isAchieved = achieved,
            dateAchieved = if (achieved) System.currentTimeMillis() else 0L
        )
    }

    private fun calculateRankAndUpdate(user: UserEntity): UserEntity {
        val rank = determineRank(user.xp)
        return user.copy(currentRank = rank)
    }

    companion object {
        fun determineRank(xp: Int): String {
            return when {
                xp >= 12000 -> "Apex"
                xp >= 7000 -> "Master"
                xp >= 3500 -> "Elite"
                xp >= 1500 -> "Warrior"
                xp >= 500 -> "Beginner"
                else -> "Rookie"
            }
        }

        fun getXpThresholdsForRank(rank: String): Pair<Int, Int> {
            return when (rank) {
                "Rookie" -> Pair(0, 500)
                "Beginner" -> Pair(500, 1500)
                "Warrior" -> Pair(1500, 3500)
                "Elite" -> Pair(3500, 7000)
                "Master" -> Pair(7000, 12000)
                "Apex" -> Pair(12000, 20000)
                else -> Pair(0, 500)
            }
        }
    }
}
