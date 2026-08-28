package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SmartChiclayoColorScheme = lightColorScheme(
    primary = MinimalPrimary,
    onPrimary = MinimalOnPrimary,
    primaryContainer = MinimalPrimaryContainer,
    onPrimaryContainer = MinimalOnPrimaryContainer,
    secondary = MinimalSecondary,
    onSecondary = Color.White,
    secondaryContainer = MinimalSecondaryContainer,
    onSecondaryContainer = MinimalOnSecondaryContainer,
    tertiary = TrafficGreen,
    onTertiary = Color.White,
    background = MinimalBackground,
    onBackground = TextPrimary,
    surface = MinimalSurface,
    onSurface = TextPrimary,
    surfaceVariant = MinimalSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = MinimalBorder,
    outlineVariant = MinimalBorderSubtle,
    error = TrafficRed,
    onError = Color.White
)

@Composable
fun SmartChiclayoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SmartChiclayoColorScheme,
        typography = Typography,
        content = content
    )
}

