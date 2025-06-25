package com.example.sportshop.ui.components.profile

import UserViewModel
import android.content.Context
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.sportshop.ui.theme.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import com.example.sportshop.util.getActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileMenu(
    navController: NavController,
    themeManager: ThemeManager,
    userViewModel: UserViewModel,
    reloadApp: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "yourname@gmail.com"
    val name by userViewModel.fullName.collectAsState()
    val photoUrl = user?.photoUrl?.toString()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    val currentLanguage = sharedPreferences.getString("language", "Eng") ?: "Eng"
    var showSettingsSheet: Boolean by remember { mutableStateOf(false) }
    var theme by remember { mutableStateOf(themeManager.currentTheme) }
    var language by remember { mutableStateOf(currentLanguage) }

    val onSaveSettings: () -> Unit = {
        val themeChanged = theme != themeManager.currentTheme
        themeManager.setTheme(theme)
        sharedPreferences.edit().apply {
            putString("language", language)
            apply()
        }
        if (language != currentLanguage) {
            val locale = if (language == "Eng") Locale("en") else Locale("vi")
            val config = context.resources.configuration
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context.getActivity()?.recreate()
        }
        if (themeChanged) {
            reloadApp()
        }
    }

    ProfileCard(
        name = name,
        email = email,
        photoUrl = photoUrl,
        onProfileClick = { navController.navigate("edit_profile") },
        onSettingsClick = { showSettingsSheet = true },
        onLogout = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("welcome") {
                popUpTo("home") { inclusive = true }
            }
        },
        onOrderHistoryClick = {
            navController.navigate("order_history")
        }
    )

    if (showSettingsSheet) {
        // Truyền themeManager.currentTheme để giữ giao diện bottom sheet theo theme hiện tại của app
        SettingsBottomSheet(
            theme = theme,
            language = language,
            onThemeChange = { theme = it }, // chỉ cập nhật biến tạm, chưa lưu
            onLanguageChange = { language = it }, // chỉ cập nhật biến tạm, chưa lưu
            onSave = {
                onSaveSettings()
                showSettingsSheet = false
            },
            onDismiss = {
                theme = themeManager.currentTheme
                language = currentLanguage
                showSettingsSheet = false
            },
            reloadApp = {},
        )
    }
}
