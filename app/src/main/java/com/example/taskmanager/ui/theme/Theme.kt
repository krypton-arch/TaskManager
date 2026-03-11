package com.example.taskmanager.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = DarkSurface,
    onPrimary = Color.White,
    secondary = PurpleAccent,
    onSecondary = DarkSurface,
    tertiary = LimeGreen,
    onTertiary = DarkSurface,
    background = OffWhiteBackground,
    onBackground = PrimaryText,
    surface = OffWhiteBackground,
    onSurface = PrimaryText,
    surfaceVariant = CardWhite,
    onSurfaceVariant = MutedText,
    outline = ChipBackground
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleAccent,
    onPrimary = DarkSurface,
    secondary = LimeGreen,
    onSecondary = DarkSurface,
    tertiary = CardBlue,
    onTertiary = DarkSurface,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF3A3A3A)
)

@Composable
fun TaskmanagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePreference: String = "System",
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themePreference) {
        "Light" -> false
        "Dark" -> true
        else -> darkTheme  // "System" falls through to isSystemInDarkTheme()
    }

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}