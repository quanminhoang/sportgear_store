package com.example.sportshop.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Màu sắc tùy chỉnh cho Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    tertiary = Color.White,
    outline = Color.LightGray
)

// Màu sắc tùy chỉnh cho Light Theme
private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    secondary = Color.Black,
    onSecondary = Color.White,
    tertiary = Color.White,
    outline = Color.LightGray
)

@Composable
fun SportShopTheme(
    theme: String,
    dynamicColor: Boolean = true, // Tùy chọn sử dụng dynamic color
    content: @Composable () -> Unit // Thêm tham số content
) {
    // Xác định darkTheme dựa trên theme từ ThemeManager
    val darkTheme = theme == "Dark"

    // Chọn colorScheme dựa trên theme và dynamicColor
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Áp dụng theme với colorScheme đã chọn
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
