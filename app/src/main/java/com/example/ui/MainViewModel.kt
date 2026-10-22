package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BuildXDatabase
import com.example.data.local.DefaultExercises
import com.example.data.local.NutritionCalculator
import com.example.data.local.SecurityHelper
import com.example.data.model.DailyWinterArcEntity
import com.example.data.model.Exercise
import com.example.data.model.PersonalMilestoneEntity
import com.example.data.model.UserEntity
import com.example.data.model.WorkoutEntity
import com.example.data.repository.BuildXRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination {
    HOME,
    WORKOUTS,
    WINTER_ARC,
    PROGRESS,
    NUTRITION,
    PROFILE,
    ACTIVE_WORKOUT,
    CAMERA_COUNTER,
    ONBOARDING
}

enum class AuthMode {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BuildXDatabase.getInstance(application)
    val repository = BuildXRepository(database)

    val user: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val allWorkouts: StateFlow<List<WorkoutEntity>> = repository.allWorkoutsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentWorkouts: StateFlow<List<WorkoutEntity>> = repository.recentWorkoutsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val winterArcDays: StateFlow<List<DailyWinterArcEntity>> = repository.winterArcDaysFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedWinterDaysCount: StateFlow<Int> = repository.completedDaysCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val milestones: StateFlow<List<PersonalMilestoneEntity>> = repository.milestonesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPushUpReps: StateFlow<Int?> = repository.totalRepsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDurationSeconds: StateFlow<Int?> = repository.totalDurationFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalWorkoutsCount: StateFlow<Int> = repository.workoutCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Navigation & App Lock States
    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _isAppLocked = MutableStateFlow(false)
    val isAppLocked: StateFlow<Boolean> = _isAppLocked.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authMode = MutableStateFlow(AuthMode.LOGIN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    // Active Workout Session State
    private val _activeExercise = MutableStateFlow<Exercise?>(null)
    val activeExercise: StateFlow<Exercise?> = _activeExercise.asStateFlow()

    private val _completedSets = MutableStateFlow(0)
    val completedSets: StateFlow<Int> = _completedSets.asStateFlow()

    private val _activeRepsLogged = MutableStateFlow(0)
    val activeRepsLogged: StateFlow<Int> = _activeRepsLogged.asStateFlow()

    private val _restTimeRemaining = MutableStateFlow(0)
    val restTimeRemaining: StateFlow<Int> = _restTimeRemaining.asStateFlow()

    private val _isRestTimerRunning = MutableStateFlow(false)
    val isRestTimerRunning: StateFlow<Boolean> = _isRestTimerRunning.asStateFlow()

    private var restTimerJob: Job? = null
    private var workoutStartTime: Long = 0L

    // Camera Push-Up Rep Counter State
    private val _cameraReps = MutableStateFlow(0)
    val cameraReps: StateFlow<Int> = _cameraReps.asStateFlow()

    private val _cameraSets = MutableStateFlow(1)
    val cameraSets: StateFlow<Int> = _cameraSets.asStateFlow()

    private val _cameraFormFeedback = MutableStateFlow("Position device & begin push-ups")
    val cameraFormFeedback: StateFlow<String> = _cameraFormFeedback.asStateFlow()

    private val _cameraIsActive = MutableStateFlow(false)
    val cameraIsActive: StateFlow<Boolean> = _cameraIsActive.asStateFlow()

    private val _cameraSessionDuration = MutableStateFlow(0)
    val cameraSessionDuration: StateFlow<Int> = _cameraSessionDuration.asStateFlow()

    private var cameraDurationJob: Job? = null

    init {
        viewModelScope.launch {
            val u = repository.getOrCreateDefaultUser()
            if (u.isAppLockEnabled && u.pinHash.isNotBlank()) {
                _isAppLocked.value = true
            }
        }
    }

    fun navigateTo(dest: AppDestination) {
        _currentDestination.value = dest
    }

    fun setAuthMode(mode: AuthMode) {
        _authMode.value = mode
        _authErrorMessage.value = null
        _authSuccessMessage.value = null
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authErrorMessage.value = null
            val result = repository.loginUser(email, pass)
            result.onSuccess {
                _isLoggedIn.value = true
                if (it.isAppLockEnabled && it.pinHash.isNotBlank()) {
                    _isAppLocked.value = true
                }
                if (!it.isOnboarded) {
                    _currentDestination.value = AppDestination.ONBOARDING
                } else {
                    _currentDestination.value = AppDestination.HOME
                }
            }.onFailure {
                _authErrorMessage.value = it.message ?: "Authentication failed."
            }
        }
    }

    fun register(email: String, pass: String) {
        viewModelScope.launch {
            _authErrorMessage.value = null
            if (email.isBlank() || pass.length < 6) {
                _authErrorMessage.value = "Password must be at least 6 characters."
                return@launch
            }
            val result = repository.registerUser(email, pass)
            result.onSuccess {
                _isLoggedIn.value = true
                _currentDestination.value = AppDestination.ONBOARDING
            }.onFailure {
                _authErrorMessage.value = it.message ?: "Registration error."
            }
        }
    }

    fun resetPassword(email: String, newPass: String) {
        viewModelScope.launch {
            val success = repository.resetPassword(email, newPass)
            if (success) {
                _authSuccessMessage.value = "Password reset successfully. You can now login."
                _authMode.value = AuthMode.LOGIN
            } else {
                _authErrorMessage.value = "Failed to reset password."
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _authMode.value = AuthMode.LOGIN
    }

    fun unlockWithPin(pin: String): Boolean {
        val currentUser = user.value ?: return true
        if (currentUser.pinHash.isBlank()) {
            _isAppLocked.value = false
            return true
        }
        val valid = SecurityHelper.verifySecret(pin, currentUser.pinHash)
        if (valid) {
            _isAppLocked.value = false
        }
        return valid
    }

    fun unlockBiometric() {
        _isAppLocked.value = false
    }

    fun setAppLock(enabled: Boolean, pin: String) {
        viewModelScope.launch {
            repository.setAppLock(enabled, pin)
            if (enabled) {
                _authSuccessMessage.value = "App Lock configured securely."
            }
        }
    }

    fun toggleBiometric(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleBiometric(enabled)
        }
    }

    fun saveOnboardingProfile(
        age: Int,
        experience: String,
        height: Float,
        weight: Float,
        goal: String,
        time: Int,
        equip: String,
        budget: String,
        isVeg: Boolean
    ) {
        viewModelScope.launch {
            val current = user.value ?: return@launch
            val updated = current.copy(
                age = age,
                experienceLevel = experience,
                heightCm = height,
                weightKg = weight,
                fitnessGoal = goal,
                availableTimeMinutes = time,
                equipment = equip,
                budgetTier = budget,
                isVegetarian = isVeg,
                isOnboarded = true
            )
            repository.updateUserProfile(updated)
            _currentDestination.value = AppDestination.HOME
        }
    }

    // Active Workout Flow
    fun startWorkoutSession(exercise: Exercise) {
        _activeExercise.value = exercise
        _completedSets.value = 0
        _activeRepsLogged.value = 0
        _restTimeRemaining.value = 0
        _isRestTimerRunning.value = false
        workoutStartTime = System.currentTimeMillis()
        _currentDestination.value = AppDestination.ACTIVE_WORKOUT
    }

    fun logSetCompleted(reps: Int) {
        _completedSets.value += 1
        _activeRepsLogged.value += reps
        val ex = _activeExercise.value
        if (ex != null) {
            startRestTimer(ex.restSeconds)
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _restTimeRemaining.value = seconds
        _isRestTimerRunning.value = true
        restTimerJob = viewModelScope.launch {
            while (_restTimeRemaining.value > 0) {
                delay(1000)
                _restTimeRemaining.value -= 1
            }
            _isRestTimerRunning.value = false
        }
    }

    fun skipRestTimer() {
        restTimerJob?.cancel()
        _restTimeRemaining.value = 0
        _isRestTimerRunning.value = false
    }

    fun finishWorkoutSession() {
        val ex = _activeExercise.value ?: return
        val duration = ((System.currentTimeMillis() - workoutStartTime) / 1000).toInt().coerceAtLeast(30)
        val reps = _activeRepsLogged.value.coerceAtLeast(10)
        val xp = 150 + (reps * 2)

        viewModelScope.launch {
            repository.logCompletedWorkout(
                title = ex.name,
                category = ex.category,
                durationSeconds = duration,
                totalReps = reps,
                xpEarned = xp,
                formAccuracyRate = 0.98f,
                notes = "Completed ${ex.sets} sets of ${ex.name}"
            )
            _currentDestination.value = AppDestination.WORKOUTS
        }
    }

    // Camera Push-Up Rep Counter
    fun startCameraPushUpSession() {
        _cameraReps.value = 0
        _cameraSets.value = 1
        _cameraFormFeedback.value = "Position phone 4-6 ft away at chest level"
        _cameraIsActive.value = true
        _cameraSessionDuration.value = 0

        cameraDurationJob?.cancel()
        cameraDurationJob = viewModelScope.launch {
            while (_cameraIsActive.value) {
                delay(1000)
                _cameraSessionDuration.value += 1
            }
        }
        _currentDestination.value = AppDestination.CAMERA_COUNTER
    }

    fun onCameraRepDetected(feedback: String) {
        _cameraReps.value += 1
        _cameraFormFeedback.value = feedback
    }

    fun manualIncrementRep() {
        _cameraReps.value += 1
        _cameraFormFeedback.value = "Rep added manually (+1)"
    }

    fun manualDecrementRep() {
        if (_cameraReps.value > 0) {
            _cameraReps.value -= 1
            _cameraFormFeedback.value = "Rep adjusted (-1)"
        }
    }

    fun nextCameraSet() {
        _cameraSets.value += 1
        _cameraFormFeedback.value = "Set ${_cameraSets.value} started! Get ready."
    }

    fun finishCameraPushUpSession() {
        _cameraIsActive.value = false
        cameraDurationJob?.cancel()
        val totalReps = _cameraReps.value
        val duration = _cameraSessionDuration.value.coerceAtLeast(15)
        val xp = 100 + (totalReps * 3)

        viewModelScope.launch {
            if (totalReps > 0) {
                repository.logCompletedWorkout(
                    title = "Camera Push-Up Challenge",
                    category = "Push",
                    durationSeconds = duration,
                    totalReps = totalReps,
                    xpEarned = xp,
                    formAccuracyRate = 0.95f,
                    notes = "Camera tracked: $totalReps reps in ${_cameraSets.value} sets"
                )
            }
            _currentDestination.value = AppDestination.HOME
        }
    }

    // Winter Arc Tasks
    fun toggleWinterArcTask(
        day: DailyWinterArcEntity,
        taskType: String // routine, hydration, mental, cold, nutrition
    ) {
        viewModelScope.launch {
            val newRoutine = if (taskType == "routine") !day.routineDone else day.routineDone
            val newHydration = if (taskType == "hydration") !day.hydrationDone else day.hydrationDone
            val newMental = if (taskType == "mental") !day.mentalGrowthDone else day.mentalGrowthDone
            val newCold = if (taskType == "cold") !day.coldShowerDone else day.coldShowerDone
            val newNutrition = if (taskType == "nutrition") !day.cleanNutritionDone else day.cleanNutritionDone

            repository.updateWinterArcTasks(
                dayNumber = day.dayNumber,
                routine = newRoutine,
                hydration = newHydration,
                mental = newMental,
                cold = newCold,
                nutrition = newNutrition
            )
        }
    }

    fun toggleRestRecovery(dayNumber: Int, isRest: Boolean) {
        viewModelScope.launch {
            repository.toggleRestDay(dayNumber, isRest)
        }
    }

    fun addNewMilestone(title: String, target: Int, targetDay: Int) {
        viewModelScope.launch {
            repository.addMilestone(title, target, targetDay)
        }
    }

    fun toggleMilestone(id: Long, achieved: Boolean) {
        viewModelScope.launch {
            repository.toggleMilestoneAchieved(id, achieved)
        }
    }
}
