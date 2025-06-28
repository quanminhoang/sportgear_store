package com.example.sportshop

import UserViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.model.data.CartViewModelFactory
import com.example.sportshop.navigation.AppNavigation
import com.example.sportshop.ui.theme.SportShopTheme
import com.example.sportshop.ui.theme.rememberThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.OrderViewModelFactory
import com.example.sportshop.viewmodel.ProductViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySavedLanguageSettings()

        setContent {
            val themeManager = rememberThemeManager()
            val productViewModel: ProductViewModel = viewModel()
            val userViewModel: UserViewModel = viewModel()
            val adminViewModel: AdminViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel(
                factory = CartViewModelFactory(application, productViewModel)
            )

            val factory = remember { OrderViewModelFactory(productViewModel) }
            val orderViewModel: OrderViewModel = viewModel(factory = factory)

            val currentTheme by themeManager.themeFlow.collectAsState()

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
                    orderViewModel = orderViewModel,
                    reloadApp = reloadApp
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