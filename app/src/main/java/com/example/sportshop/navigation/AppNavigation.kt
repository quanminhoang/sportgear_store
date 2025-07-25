package com.example.sportshop.navigation

import MainScreen
import UserViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.components.screen.AllProductsScreen
import com.example.sportshop.ui.screen.*
import com.example.sportshop.ui.components.search.SearchScreen
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.OrderViewModelFactory
import com.example.sportshop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    themeManager: ThemeManager,
    cartViewModel: CartViewModel,
    adminViewModel: AdminViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    orderViewModel: OrderViewModel, // Add parameter
    reloadApp: () -> Unit
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
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel
            )
        }
        composable(
            route = "all_products?featured={featured}&category={category}&collection={collection}",
            arguments = listOf(
                navArgument("featured") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("collection") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val featured = backStackEntry.arguments?.getBoolean("featured") ?: false
            val category = backStackEntry.arguments?.getString("category")
            val collection = backStackEntry.arguments?.getString("collection")

            AllProductsScreen(
                productViewModel = productViewModel,
                navController = navController,
                featured = featured,
                category = category,
                collection = collection
            )
        }

        composable("main_profile") {
            MainProfileMenu(
                navController = navController,
                themeManager = themeManager,
                userViewModel = userViewModel,
                reloadApp = reloadApp
            )
        }
        composable("edit_profile") {
            EditProfileScreen(navController, userViewModel)
        }
        composable("admin_screen") {
            AdminScreen(navController)
        }
        composable("search_screen") {
            SearchScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composable("add_product") {
            AddProductScreen(
                navcontroller = navController,
                product = Product(),
                onSave = { updatedProduct, callback ->
                    adminViewModel.saveProduct(updatedProduct, callback)
                },
            )
        }

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
                onSave = { updatedProduct, callback ->
                    adminViewModel.saveProduct(updatedProduct, callback)
                },
            )
        }

        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val product = adminViewModel.products.find { it.id == productId }
                ?: productViewModel.getProductById(productId)
            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    navController = navController,
                    productViewModel = productViewModel,
                    onAddToCart = { product, quantity ->
                        val cartItem = CartItem(
                            id = product.id,
                            name = product.name,
                            price = product.price,
                            imageUrl = product.imageUrls.firstOrNull() ?: "",
                            quantity = quantity
                        )
                        cartViewModel.addToCart(cartItem)
                    }
                )
            }
        }
        composable(
            route = "all_products?featured={featured}",
            arguments = listOf(
                navArgument("featured") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val featured = backStackEntry.arguments?.getBoolean("featured") ?: false
            AllProductsScreen(
                productViewModel = productViewModel,
                navController = navController,
                category = null,
                featured = featured
            )
        }

        composable("order_history") {
            val factory = remember { OrderViewModelFactory(productViewModel) }
            val orderViewModel: OrderViewModel = viewModel(factory = factory)
            OrderHistoryScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                productViewModel = productViewModel
            )
        }

        composable("order_detail/{id}") { backStackEntry ->
            val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                cartViewModel.setUser(userId)
            }
            OrderDetailScreen(
                backStackEntry = backStackEntry,
                orderViewModel = orderViewModel,
                navController = navController,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel // Add for stock display
            )
        }
    }
}