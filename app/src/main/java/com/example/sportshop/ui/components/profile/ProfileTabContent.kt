package com.example.sportshop.components.profile

import UserViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.theme.ThemeManager

@Composable
fun ProfileTabContent(
    navController: NavController,
    themeManager: ThemeManager,
    userViewModel: UserViewModel

) {
    val userViewModel: UserViewModel = viewModel()

    MainProfileMenu(
        navController = navController,
        themeManager = themeManager,
        userViewModel = userViewModel
        
    )
}