package com.example.sportshop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.sportshop.ui.components.MyApp
import com.example.sportshop.ui.theme.SportsShopTheme
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
            SportsShopTheme(themeManager.currentTheme) {
                MyApp(themeManager)
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


// Product Data Class
data class Product(
    val name: String,
    val price: String,
    val discount: String,
    val imageRes: Int
)

data class CartItem(
    val id: Int,
    val name: String,
    val imageResId: Int,
    val price: Double,
    var quantity: Int
)






