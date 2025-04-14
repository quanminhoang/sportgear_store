package com.example.sportshop.project

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Ứng dụng chính với Navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(themeManager: ThemeManager) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("main_profile") { MainProfileMenu(navController, themeManager) }
        composable("profile") { ProfileScreen(navController) }
        composable("login_google") { GoogleLoginScreen(navController) }
        composable("register_info") { RegisterInfoScreen(navController) }
        composable(
            "register_credential?name={name}&dob={dob}&phone={phone}&email={email}&address={address}",
            arguments = listOf(
                navArgument("name") { defaultValue = "" },
                navArgument("dob") { defaultValue = "" },
                navArgument("phone") { defaultValue = "" },
                navArgument("email") { defaultValue = "" },
                navArgument("address") { defaultValue = "" }
            )
        ) { backStackEntry ->
            RegisterCredentialScreen(
                navController,
                name = backStackEntry.arguments?.getString("name") ?: "",
                dob = backStackEntry.arguments?.getString("dob") ?: "",
                phone = backStackEntry.arguments?.getString("phone") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                address = backStackEntry.arguments?.getString("address") ?: ""
            )
        }
    }
}
