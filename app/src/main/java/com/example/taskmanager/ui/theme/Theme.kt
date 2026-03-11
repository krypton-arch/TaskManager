package com.example.taskmanager.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

@Composable
fun TaskmanagerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}