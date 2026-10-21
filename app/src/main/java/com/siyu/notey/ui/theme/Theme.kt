package com.siyu.notey.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Colors = lightColorScheme(
    primary = Violet, onPrimary = Color.White,
    secondary = Pink, onSecondary = InkDark,
    tertiary = Sunny, onTertiary = InkDark,
    error = Coral, onError = Color.White,
    background = AppBackground, onBackground = InkDark,
    surface = CardWhite, onSurface = InkDark,
    secondaryContainer = VioletSoft, onSecondaryContainer = InkDark,
    outlineVariant = BorderSoft
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun NoteyTheme(content: @Composable () -> Unit) =
    MaterialTheme(
        colorScheme = Colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )