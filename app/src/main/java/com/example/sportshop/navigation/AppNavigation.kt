package com.example.sportshop.navigation

import MainScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.components.edit_profile.ProfileScreen
import com.example.sportshop.ui.screen.AdminScreen
import com.example.sportshop.ui.screen.CheckoutScreen
import com.example.sportshop.ui.screen.SearchScreen
import com.example.sportshop.ui.screen.SplashScreen
import com.example.sportshop.ui.screen.WelcomeScreen
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavaigation(
    themeManager: ThemeManager, cartViewModel: CartViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("home") {
            MainScreen(
                navController = navController, cartViewModel = cartViewModel
            )
        }

        composable("main_profile") {
            MainProfileMenu(
                navController = navController, themeManager = themeManager
            )
        }
        composable("profile") {
            ProfileScreen(navController)
        }

        composable("admin_screen") {
            AdminScreen(navController)
        }
        composable("search_screen") {
            SearchScreen(
                navController = navController, cartViewModel = cartViewModel
            )
        }
        composable("checkout") {
            CheckoutScreen(
                navController = navController, cartViewModel = cartViewModel
            )
        }

    }
}