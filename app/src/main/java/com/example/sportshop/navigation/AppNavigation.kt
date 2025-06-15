package com.example.sportshop.navigation

import MainScreen
import UserViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.profile.MainProfileMenu
import com.example.sportshop.ui.components.screen.AllProductsScreen
import com.example.sportshop.ui.screen.AddProductScreen
import com.example.sportshop.ui.screen.AdminScreen
import com.example.sportshop.ui.screen.CheckoutScreen
import com.example.sportshop.ui.screen.EditProfileScreen
import com.example.sportshop.ui.screen.ProductDetailScreen
import com.example.sportshop.ui.components.search.SearchScreen
import com.example.sportshop.ui.screen.SplashScreen
import com.example.sportshop.ui.screen.WelcomeScreen
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun AppNavigation(
    themeManager: ThemeManager,
    cartViewModel: CartViewModel,
    adminViewModel: AdminViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel
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
        composable("all_products/{category}?featured={featured}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "default_category" // Giá trị mặc định nếu không có category
            val featured = backStackEntry.arguments?.getString("featured")?.toBoolean() ?: false

            AllProductsScreen(
                productViewModel = productViewModel,
                navController = navController,
                category = category,
                featured = featured
            )
        }
        composable("main_profile") {
            MainProfileMenu(
                navController = navController,
                themeManager = themeManager,
                userViewModel = userViewModel
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
        composable("checkout") {
            CheckoutScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                userViewModel = userViewModel
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
                category = null, // hoặc "" tùy theo bạn setup
                featured = featured
            )
        }

    }
}
