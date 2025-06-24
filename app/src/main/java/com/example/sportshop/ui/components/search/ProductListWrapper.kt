package com.example.sportshop.ui.components.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.viewmodel.CartViewModel
import java.text.Normalizer
import java.util.Locale

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

    if (displayProducts.isEmpty()) {
        Text("Không tìm thấy sản phẩm")
    } else {
        // Hiển thị danh sách sản phẩm bằng SearchProductCard riêng cho search
        Column {
            displayProducts.forEach { product ->
                SearchProductCard(
                    product = product,
                    onClick = { productId ->
                        navController.navigate("product_detail/$productId")
                    }
                )
            }
        }
    }
}

private fun getFilteredProducts(products: List<Product>, query: String): List<Product> {
    val normalizedQuery = normalizeText(query)

    return products.filter {
        listOf(it.name, it.description).any { field ->
            field?.let { normalizeText(it).contains(normalizedQuery) } == true
        }
    }
}

private fun normalizeText(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    return normalized
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "") // Loại bỏ dấu
        .lowercase(Locale.getDefault()) // Chuyển thành chữ thường
}