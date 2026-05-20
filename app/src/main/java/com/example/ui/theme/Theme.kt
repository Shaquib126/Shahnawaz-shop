package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FarmGreenPrimaryContainer,
    onPrimary = FarmGreenOnPrimaryContainer,
    primaryContainer = FarmGreenPrimary,
    onPrimaryContainer = FarmGreenOnPrimary,
    secondary = FarmGreenSecondaryContainer,
    onSecondary = FarmGreenOnSecondaryContainer,
    tertiary = EarthyTertiaryContainer,
    onTertiary = EarthyOnTertiaryContainer,
    background = Color(0xFF121411),
    onBackground = Color(0xFFE2E3DF),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DF),
)

private val LightColorScheme = lightColorScheme(
    primary = FarmGreenPrimary,
    onPrimary = FarmGreenOnPrimary,
    primaryContainer = FarmGreenPrimaryContainer,
    onPrimaryContainer = FarmGreenOnPrimaryContainer,
    secondary = FarmGreenSecondary,
    onSecondary = FarmGreenOnSecondary,
    tertiary = EarthyTertiary,
    onTertiary = EarthyOnTertiary,
    background = FarmBackground,
    onBackground = FarmOnBackground,
    surface = FarmSurface,
    onSurface = FarmOnSurface,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // specific brand theme preferred
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
