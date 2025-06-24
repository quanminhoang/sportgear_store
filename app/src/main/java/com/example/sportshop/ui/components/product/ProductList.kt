package com.example.sportshop.ui.components.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportshop.model.data.Product
import com.example.sportshop.viewmodel.CartViewModel
import androidx.navigation.NavController

@Composable
fun ProductList(
    products: List<Product>,
    cartViewModel: CartViewModel,
    navController: NavController // <- thêm dòng này
) {
    Column(modifier = Modifier.padding(16.dp)) {
        products.forEach { product ->
            FeatureProductCard(
                product = product,
                onClick = {
                    navController.navigate("product_detail/${product.id}")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



