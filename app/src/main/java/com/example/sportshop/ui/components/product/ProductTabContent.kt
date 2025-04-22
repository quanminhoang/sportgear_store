package com.example.sportshop.components.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.ui.components.product.ProductListWrapper
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun ProductTabContent(
    productViewModel: ProductViewModel,  // Add ProductViewModel to get the products
    cartViewModel: CartViewModel,
    navController: NavController, // Add navController as a parameter
    modifier: Modifier = Modifier
) {
    // Collecting the list of all products from ProductViewModel
    val allProducts = productViewModel.allProducts.collectAsState(initial = emptyList()).value

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tất Cả Sản Phẩm",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display message if there are no products
        if (allProducts.isEmpty()) {
            Text("Chưa có sản phẩm nào")
        } else {
            // Pass the allProducts to ProductListWrapper for display
            ProductListWrapper(
                products = allProducts,  // Pass the list of products
                cartViewModel = cartViewModel,
                navController = navController
            )
        }
    }
}
