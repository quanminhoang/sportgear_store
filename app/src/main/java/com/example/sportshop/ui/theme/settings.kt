package com.example.sportshop.ui.theme

import android.content.Context
import android.content.SharedPreferences

object ThemePreferences {
    private const val PREFS_NAME = "theme_preferences"
    private const val KEY_THEME = "selected_theme"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Lưu lựa chọn theme
    fun saveTheme(context: Context, theme: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_THEME, theme)
        editor.apply()
    }

    // Lấy lựa chọn theme
    fun getTheme(context: Context): String {
        return getPreferences(context).getString(KEY_THEME, "Light") ?: "Light"
    }
}