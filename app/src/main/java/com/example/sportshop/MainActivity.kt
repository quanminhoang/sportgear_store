package com.example.sportshop

import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.navigation.AppNavigation
import com.example.sportshop.ui.theme.SportShopTheme
import com.example.sportshop.ui.theme.rememberThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySavedLanguageSettings()

        setContent {
            val themeManager = rememberThemeManager()
            val cartViewModel: CartViewModel = viewModel()
            val adminViewModel: AdminViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel()
            val userViewModel: UserViewModel = viewModel()

            val currentTheme by themeManager.themeFlow.collectAsState()

            // Callback để reload lại Activity
            val reloadApp: () -> Unit = {
                finish()
                startActivity(intent)
            }

            SportShopTheme(currentTheme) {
                AppNavigation(
                    themeManager = themeManager,
                    cartViewModel = cartViewModel,
                    adminViewModel = adminViewModel,
                    productViewModel = productViewModel,
                    userViewModel = userViewModel,
                    reloadApp = reloadApp // thêm dòng này để truyền callback reloadApp
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
