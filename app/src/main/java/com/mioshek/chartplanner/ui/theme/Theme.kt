package com.mioshek.chartplanner.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Material 3 color schemes
private val darkColorScheme = darkColorScheme(
    primary = darkPrimary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnPrimaryContainer,
    inversePrimary = darkInversePrimary,

    secondary = darkSecondary,
    onSecondary = darkOnSecondary,
    secondaryContainer = darkSecondaryContainer,
    onSecondaryContainer = darkOnSecondaryContainer,

    tertiary = darkTertiary,
    onTertiary = darkOnTertiary,
    tertiaryContainer = darkTertiaryContainer,
    onTertiaryContainer = darkOnTertiaryContainer,

    error = darkError,
    errorContainer = darkErrorContainer,
    onError = darkOnError,
    onErrorContainer = darkOnErrorContainer,

    background = darkBackground,
    onBackground = darkOnBackground,

    surface = darkSurface,
    onSurface = darkOnSurface,
    inverseSurface = darkInverseSurface,
    inverseOnSurface = darkInverseOnSurface,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkOnSurfaceVariant,

    outline = darkOutline
)

private val lightColorScheme = lightColorScheme(
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnPrimaryContainer,
    inversePrimary = lightInversePrimary,

    secondary = lightSecondary,
    onSecondary = lightOnSecondary,
    secondaryContainer = lightSecondaryContainer,
    onSecondaryContainer = lightOnSecondaryContainer,

    tertiary = lightTertiary,
    onTertiary = lightOnTertiary,
    tertiaryContainer = lightTertiaryContainer,
    onTertiaryContainer = lightOnTertiaryContainer,

    error = lightError,
    onError = lightOnError,
    errorContainer = lightErrorContainer,
    onErrorContainer = lightOnErrorContainer,

    background = lightBackground,
    onBackground = lightOnBackground,

    surface = lightSurface,
    onSurface = lightOnSurface,
    inverseSurface = lightInverseSurface,
    inverseOnSurface = lightInverseOnSurface,
    surfaceVariant = lightSurfaceVariant,
    onSurfaceVariant = lightOnSurfaceVariant,

    outline = lightOutline
)

@Composable
fun ChartPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val replyColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = replyColorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}