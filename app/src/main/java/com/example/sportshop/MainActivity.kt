package com.example.sportshop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.ui.components.MyApp
import com.example.sportshop.ui.theme.SportShopTheme
import com.example.sportshop.ui.viewmodel.CartViewModel
import java.util.Locale

// Theme Manager để quản lý theme
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


// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cập nhật ngôn ngữ khi khởi động ứng dụng
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "Eng") ?: "Eng"
        val locale = if (language == "Eng") Locale("en") else Locale("vi")
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContent {
            val themeManager = rememberThemeManager()
            val cartViewModel: CartViewModel = viewModel()
            SportShopTheme(themeManager.currentTheme) {
                MyApp(themeManager,cartViewModel)
            }
        }
    }
}

// Utility để lấy Activity từ Context
fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}


data class Product(
    val id: String? = null,
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val category: String = "",
    val discount: Double = 0.0
)

data class CartItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    var quantity: Int
)







