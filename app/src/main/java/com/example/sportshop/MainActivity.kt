package com.example.sportshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.navigation.AppNavaigation
import com.example.sportshop.ui.theme.SportShopTheme
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.ui.viewmodel.CartViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySavedLanguageSettings()

        setContent {
            val themeManager = rememberThemeManager()
            val cartViewModel: CartViewModel = viewModel()

            SportShopTheme(themeManager.currentTheme) {
                AppNavaigation(
                    themeManager = themeManager,
                    cartViewModel = cartViewModel
                )
            }
        }
    }

    private fun applySavedLanguageSettings() {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "Eng") ?: "Eng"
        val locale = if (language == "Eng") Locale("en") else Locale("vi")
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

@Composable
private fun rememberThemeManager(): ThemeManager {
    return com.example.sportshop.ui.theme.rememberThemeManager()
}