package com.example.sportshop.components.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.theme.ThemeManager

@Composable
fun ProfileTabContent(
    navController: NavController,
    themeManager: ThemeManager
) {
    MainProfileMenu(
        navController = navController,
        themeManager = themeManager
    )
}