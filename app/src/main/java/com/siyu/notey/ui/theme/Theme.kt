package com.siyu.notey.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Theme.kt
private val Colors = lightColorScheme(
    primary = CoralGlow, onPrimary = InkDark,
    secondary = SkyBlue, onSecondary = InkDark,
    tertiary = SoftPeach, onTertiary = InkDark,
    error = TomatoJam, onError = Color.White,
    background = Honeydew, onBackground = InkDark,
    surface = WarmWhite, onSurface = InkDark,
    secondaryContainer = SoftPeach, onSecondaryContainer = InkDark
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun NoteyTheme(content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = Colors, shapes = AppShapes, content = content)