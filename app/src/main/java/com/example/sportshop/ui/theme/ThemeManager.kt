package com.example.sportshop.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class ThemeManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    private val themeState = mutableStateOf(sharedPreferences.getString("theme", "Light") ?: "Light")

    val currentTheme: String
        get() = themeState.value

    fun setTheme(theme: String) {
        themeState.value = theme
        sharedPreferences.edit().putString("theme", theme).apply()
    }
}

@Composable
fun rememberThemeManager(): ThemeManager {
    val context = LocalContext.current
    return remember { ThemeManager(context) }
}