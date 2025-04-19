package com.example.sportshop.ui.theme

import android.os.Build
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Màu sắc cho Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Màu sắc cho Light Theme
private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.Red,
    secondary = PurpleGrey40,
    tertiary = Pink40
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}