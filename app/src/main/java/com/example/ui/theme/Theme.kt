package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BuildXColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = BuildXBlack,
    primaryContainer = DeepNeonBlue,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricBlue,
    onSecondary = BuildXBlack,
    secondaryContainer = BuildXCardBackground,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonMint,
    onTertiary = BuildXBlack,
    background = BuildXBlack,
    onBackground = TextPrimary,
    surface = BuildXDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = BuildXCardBackground,
    onSurfaceVariant = TextSecondary,
    outline = BuildXCardBorder,
    error = CriticalRed,
    onError = TextPrimary
)

@Composable
fun BuildXTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = BuildXBlack.toArgb()
                it.navigationBarColor = BuildXBlack.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = BuildXColorScheme,
        typography = Typography,
        content = content
    )
}
