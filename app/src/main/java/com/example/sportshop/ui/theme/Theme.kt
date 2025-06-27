package com.example.sportshop.ui.theme

import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Màu sắc tùy chỉnh cho Dark Theme (Apple style: dark, nhấn xanh dương, xám, trắng)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),           // Blue (Apple accent)
    onPrimary = Color(0xFFFFFFFF),         // White text on blue
    secondary = Color(0xFF5E5E5E),         // Neutral gray
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF1C1C1E),        // Apple dark background
    onBackground = Color(0xFFF2F2F7),      // Light text
    surface = Color(0xFF2C2C2E),           // Slightly lighter than background
    onSurface = Color(0xFFF2F2F7),
    error = Color(0xFFFF453A),             // Apple red
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFF3A3A3C),           // Subtle outline
    tertiary = Color(0xFF64D2FF),          // Light blue accent
    surfaceContainer = Color(0xFF242426),  // Card background
    onSurfaceVariant = Color(0xFFB0B0B0), // Muted gray for secondary text

)

// Màu sắc tùy chỉnh cho Light Theme (Apple style: trắng, nhấn xanh dương, xám, text đen)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),           // Blue (Apple accent)
    onPrimary = Color(0xFFFFFFFF),         // White text on blue
    secondary = Color(0xFFF2F2F7),         // Very light gray
    onSecondary = Color(0xFF1C1C1E),       // Dark text on gray
    background = Color(0xFFFFFFFF),        // Pure white
    onBackground = Color(0xFF1C1C1E),      // Almost black text
    surface = Color(0xFFF8F8F8),           // Slightly off-white
    onSurface = Color(0xFF1C1C1E),
    error = Color(0xFFFF3B30),             // Apple red
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFFE5E5EA),           // Subtle outline
    tertiary = Color(0xFF64D2FF),          // Light blue accent
    surfaceContainer = Color(0xFFF2F2F7),  // Card background
    onSurfaceVariant = Color(0xFF636366)   // Muted gray for secondary text
)

@Composable
fun SportShopTheme(
    theme: String,
    content: @Composable () -> Unit
) {
    val darkTheme = theme == "Dark"
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
