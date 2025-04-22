package com.example.sportshop.navigation

import MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.components.edit_profile.ProfileScreen
import com.example.sportshop.ui.screen.AddProductScreen
import com.example.sportshop.ui.screen.AdminScreen
import com.example.sportshop.ui.screen.CheckoutScreen
import com.example.sportshop.ui.screen.ProductDetailScreen
import com.example.sportshop.ui.screen.SearchScreen
import com.example.sportshop.ui.screen.SplashScreen
import com.example.sportshop.ui.screen.WelcomeScreen
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun AppNavaigation(
    themeManager: ThemeManager,
    cartViewModel: CartViewModel,
    adminViewModel: AdminViewModel,
    productViewModel: ProductViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("home") {
            MainScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        composable("main_profile") {
            MainProfileMenu(
                navController = navController,
                themeManager = themeManager
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
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        composable("checkout") {
            CheckoutScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // Thêm sản phẩm
        composable("add_product") {
            AddProductScreen(
                navcontroller = navController,
                product = Product(),
                onSave = { updatedProduct ->
                    adminViewModel.saveProduct(updatedProduct)
                }
            )
        }

        // Sửa sản phẩm
        composable(
            route = "add_product/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                nullable = true
                defaultValue = ""
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val product = adminViewModel.products.find { it.id == productId } ?: Product()

            AddProductScreen(
                navcontroller = navController,
                product = product,
                onSave = { updatedProduct ->
                    adminViewModel.saveProduct(updatedProduct)
                }
            )
        }

        // Trang chi tiết sản phẩm
        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val product = productViewModel.getProductById(productId)

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    navController = navController,
                    onAddToCart = {
                        val cartItem = CartItem(
                            id = it.id,
                            name = it.name,
                            price = it.price,
                            imageUrl = it.imageUrl,
                            quantity = 1
                        )
                        cartViewModel.addToCart(cartItem)
                    }
                )
            }
        }
    }
}
