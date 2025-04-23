package com.example.sportshop.ui.components.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.product.ProductList
import com.example.sportshop.viewmodel.CartViewModel

@Composable
fun ProductListWrapper(
    cartViewModel: CartViewModel,
    products: List<Product>,
    searchQuery: String = "",
    navController: NavController
) {
    val trimmedQuery = searchQuery.trim()

    if (trimmedQuery.isBlank()) {
        return
    }

    val displayProducts = getFilteredProducts(products, trimmedQuery)

    // Nếu không tìm thấy sản phẩm
    if (displayProducts.isEmpty()) {
        Text("Không tìm thấy sản phẩm")
    } else {
        // Hiển thị danh sách sản phẩm
        ProductList(
            products = displayProducts,
            cartViewModel = cartViewModel,
            navController = navController
        )
    }
}

private fun getFilteredProducts(products: List<Product>, query: String): List<Product> {
    val lowerQuery = query.lowercase()
    return products.filter {
        listOf(it.name, it.description).any { field ->
            field?.lowercase()?.contains(lowerQuery) == true
        }
    }
}
