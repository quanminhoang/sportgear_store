package com.example.sportshop.ui.theme.project

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sportshop.R



// Ứng dụng chính với Navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(themeManager: ThemeManager) {
    val navController = rememberNavController()

    val dummyCartItems = listOf(
        CartItem(
            id = 1,
            name = "Giày Nike Air",
            price = 99.99,
            quantity = 1,
            imageResId = R.drawable.adidas_shirt
        ),
    )

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("main_profile") { MainProfileMenu(navController, themeManager) }
        composable("profile") { ProfileScreen(navController) }
        composable("cart_screen") {
            // Dữ liệu mẫu để test, bạn có thể thay bằng dữ liệu thật trong project
            val sampleCartItems = listOf(
                CartItem(
                    id = 1,
                    name = "Giày Nike",
                    price = 99.99,
                    quantity = 1,
                    imageResId = R.drawable.adidas_shirt
                )
            )
            CartScreen(navController, cartItems = sampleCartItems)
        }
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
