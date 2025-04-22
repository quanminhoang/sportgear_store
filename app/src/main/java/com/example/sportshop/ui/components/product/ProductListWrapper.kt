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
    products: List<Product>, // Dữ liệu này truyền từ bên ngoài
    searchQuery: String = "", // Default to empty string instead of null
    navController: NavController
) {
    // Sử dụng sản phẩm từ tham số truyền vào (không cần lấy lại từ viewModel nữa)
    val displayProducts = when {
        searchQuery.isBlank() -> products // Nếu không có tìm kiếm, hiển thị tất cả sản phẩm
        else -> getFilteredProducts(products, searchQuery) // Lọc theo truy vấn tìm kiếm
    }

    // Nếu tìm thấy sản phẩm
    if (displayProducts.isEmpty()) {
        Text("Không tìm thấy sản phẩm")
    } else {
        // Hiển thị danh sách sản phẩm
        ProductList(displayProducts, cartViewModel = cartViewModel, navController = navController)
    }
}

private fun getFilteredProducts(products: List<Product>, query: String): List<Product> {
    val lowerCaseQuery = query.lowercase()
    return products.filter {
        it.name.lowercase().contains(lowerCaseQuery) ||
                it.description.lowercase().contains(lowerCaseQuery)
    }
}
