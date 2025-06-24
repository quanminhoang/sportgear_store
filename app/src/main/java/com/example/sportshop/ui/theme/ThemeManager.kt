package com.example.sportshop.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    private val _themeFlow = MutableStateFlow(sharedPreferences.getString("theme", "Light") ?: "Light")
    val themeFlow: StateFlow<String> = _themeFlow

    var currentTheme: String
        get() = _themeFlow.value
        set(value) {
            _themeFlow.value = value
            sharedPreferences.edit().putString("theme", value).apply()
        }

    fun toggleTheme() {
        currentTheme = if (currentTheme == "Dark") "Light" else "Dark"
    }

    fun setTheme(theme: String) {
        currentTheme = theme
    }
}

@Composable
fun rememberThemeManager(): ThemeManager {
    val context = LocalContext.current
    return remember { ThemeManager(context) }
}