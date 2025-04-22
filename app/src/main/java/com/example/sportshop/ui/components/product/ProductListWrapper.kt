package com.example.sportshop.ui.components.product

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.viewmodel.ProductViewModel
import com.example.sportshop.viewmodel.CartViewModel

@Composable
fun ProductListWrapper(
    cartViewModel: CartViewModel,
    searchQuery: String? = null,
    navController: NavController
) {
    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()

    val displayProducts = when {
        searchQuery == null -> products
        searchQuery.isBlank() -> emptyList()
        else -> getFilteredProducts(products, searchQuery)
    }

    if (searchQuery != null && searchQuery.isNotBlank() && displayProducts.isEmpty()) {
        Text("Không tìm thấy sản phẩm")
    } else {
        ProductList( displayProducts, cartViewModel = cartViewModel,navController = navController)
    }
}


private fun getFilteredProducts(products: List<Product>, query: String): List<Product> {
    val lowerCaseQuery = query.lowercase()
    return products.filter {
        it.name.lowercase().contains(lowerCaseQuery) ||
                it.description.lowercase().contains(lowerCaseQuery)
    }
}
