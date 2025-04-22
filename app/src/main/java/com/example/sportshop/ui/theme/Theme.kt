package com.example.sportshop.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


// Màu sắc tùy chỉnh cho Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color(0xFF121212),
    secondary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF151515),
    background = Color(0xFF2a2a2a),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF3f3f3f),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFCF6679),
    onError = Color.Red,
    outline = Color(0X54545454),
    tertiary = Color(0xFF6a6a6a),
    surfaceContainer = Color(0xFF6a6a6a),
    onSurfaceVariant = Color(0xFFd3d3d3)
)

// Màu sắc tùy chỉnh cho Light Theme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003366),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF151515),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF121212),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onError = Color.Red,
    outline = Color(0xFFd3d3d3),
    tertiary = Color(0xFFbebebe),
    surfaceContainer = Color(0xFFd3d3d3),
    onSurfaceVariant = Color(0xFF6a6a6a)
)

@Composable
fun SportShopTheme(
    theme: String,
    content: @Composable () -> Unit
) {
    val darkTheme = theme == "Dark"
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
