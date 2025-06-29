package com.example.sportshop.ui.components.profile

import UserViewModel
import android.content.Context
import android.util.Log
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

    ProfileCard(
        name = name,
        email = email,
        photoUrl = photoUrl,
        onProfileClick = { navController.navigate("edit_profile") },
        onSettingsClick = {
            // 👉 Đổi theme ngay khi click
            val newTheme = if (themeManager.currentTheme == "Light") "Dark" else "Light"
            themeManager.setTheme(newTheme)
            reloadApp()
        },
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
}