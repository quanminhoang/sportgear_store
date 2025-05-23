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
fun MainProfileMenu(navController: NavController, themeManager: ThemeManager, userViewModel: UserViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "yourname@gmail.com"
    val name by userViewModel.fullName.collectAsState() // Lấy tên từ UserViewModel
    val photoUrl = user?.photoUrl?.toString()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    val currentLanguage = sharedPreferences.getString("language", "Eng") ?: "Eng"

    var notificationSetting by remember { mutableStateOf("Allow") }
    var showSettingsSheet: Boolean by remember { mutableStateOf(false) }
    var theme by remember { mutableStateOf(themeManager.currentTheme) }
    var language by remember { mutableStateOf(currentLanguage) }

    val onSaveSettings: () -> Unit = {
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
    }

    ProfileCard(
        name = name,
        email = email,
        photoUrl = photoUrl,
        notificationSetting = notificationSetting,
        onProfileClick = { navController.navigate("edit_profile") },
        onSettingsClick = { showSettingsSheet = true },
        onNotificationToggle = {
            notificationSetting = if (notificationSetting == "Allow") "Mute" else "Allow"
        },
        onLogout = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("welcome") {
                popUpTo("home") { inclusive = true }
            }
        }
    )

    if (showSettingsSheet) {
        SettingsBottomSheet(
            theme = theme,
            language = language,
            onThemeChange = { theme = it },
            onLanguageChange = { language = it },
            onSave = {
                onSaveSettings()
                showSettingsSheet = false
            },
            onDismiss = { showSettingsSheet = false }
        )
    }
}
