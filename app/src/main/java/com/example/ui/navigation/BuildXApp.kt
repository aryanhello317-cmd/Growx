package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.MainViewModel
import com.example.ui.auth.AppLockScreen
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.OnboardingProfileScreen
import com.example.ui.camera.CameraPushUpScreen
import com.example.ui.home.HomeScreen
import com.example.ui.nutrition.NutritionScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.progress.ProgressScreen
import com.example.ui.theme.BuildXBlack
import com.example.ui.theme.BuildXCardBackground
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.workouts.WorkoutPlayerScreen
import com.example.ui.workouts.WorkoutsScreen

data class NavItem(
    val destination: AppDestination,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun BuildXApp(viewModel: MainViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isAppLocked by viewModel.isAppLocked.collectAsState()
    val currentDestination by viewModel.currentDestination.collectAsState()
    val user by viewModel.user.collectAsState()

    // 1. Auth Gatekeeper
    if (!isLoggedIn) {
        AuthScreen(viewModel = viewModel)
        return
    }

    // 2. PIN / Biometric App Lock Gatekeeper
    if (isAppLocked) {
        AppLockScreen(viewModel = viewModel)
        return
    }

    // 3. First-Launch Onboarding Gatekeeper
    if (user != null && !user!!.isOnboarded && currentDestination == AppDestination.ONBOARDING) {
        OnboardingProfileScreen(viewModel = viewModel)
        return
    }

    // BackHandler for secondary screens
    if (currentDestination == AppDestination.ACTIVE_WORKOUT ||
        currentDestination == AppDestination.CAMERA_COUNTER ||
        currentDestination == AppDestination.ONBOARDING
    ) {
        BackHandler {
            viewModel.navigateTo(AppDestination.HOME)
        }
    } else if (currentDestination != AppDestination.HOME) {
        BackHandler {
            viewModel.navigateTo(AppDestination.HOME)
        }
    }

    val navItems = listOf(
        NavItem(AppDestination.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_item_home"),
        NavItem(AppDestination.WORKOUTS, "Workouts", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter, "nav_item_workouts"),
        NavItem(AppDestination.WINTER_ARC, "Winter Arc", Icons.Filled.AcUnit, Icons.Outlined.AcUnit, "nav_item_winter_arc"),
        NavItem(AppDestination.PROGRESS, "Progress", Icons.Filled.Assessment, Icons.Outlined.Assessment, "nav_item_progress"),
        NavItem(AppDestination.NUTRITION, "Nutrition", Icons.Filled.Restaurant, Icons.Outlined.Restaurant, "nav_item_nutrition"),
        NavItem(AppDestination.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_item_profile")
    )

    val showBottomBar = currentDestination != AppDestination.ACTIVE_WORKOUT &&
            currentDestination != AppDestination.CAMERA_COUNTER &&
            currentDestination != AppDestination.ONBOARDING

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = BuildXBlack,
                    contentColor = TextPrimary,
                    tonalElevation = 8.dp,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    navItems.forEach { item ->
                        val selected = currentDestination == item.destination
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.navigateTo(item.destination) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BuildXBlack,
                                unselectedIconColor = TextMuted,
                                selectedTextColor = ElectricCyan,
                                unselectedTextColor = TextMuted,
                                indicatorColor = ElectricCyan
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        },
        containerColor = BuildXBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { dest ->
                when (dest) {
                    AppDestination.HOME -> HomeScreen(viewModel = viewModel)
                    AppDestination.WORKOUTS -> WorkoutsScreen(viewModel = viewModel)
                    AppDestination.WINTER_ARC -> com.example.ui.winterarc.WinterArcScreen(viewModel = viewModel)
                    AppDestination.PROGRESS -> ProgressScreen(viewModel = viewModel)
                    AppDestination.NUTRITION -> NutritionScreen(viewModel = viewModel)
                    AppDestination.PROFILE -> ProfileScreen(viewModel = viewModel)
                    AppDestination.ACTIVE_WORKOUT -> WorkoutPlayerScreen(viewModel = viewModel)
                    AppDestination.CAMERA_COUNTER -> CameraPushUpScreen(viewModel = viewModel)
                    AppDestination.ONBOARDING -> OnboardingProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}
