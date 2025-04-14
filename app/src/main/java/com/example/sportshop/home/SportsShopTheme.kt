package com.example.sportshop.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Theme cho ứng dụng
@Composable
fun SportsShopTheme(theme: String, content: @Composable () -> Unit) {
    val colorScheme = if (theme == "Light") {
        lightColorScheme(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF03DAC6),
            tertiary = Color(0xFFFFFAFA)
        )
    } else {
        darkColorScheme(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF03DAC6),
            tertiary = Color(0xFF2E2E2E)
        )
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}