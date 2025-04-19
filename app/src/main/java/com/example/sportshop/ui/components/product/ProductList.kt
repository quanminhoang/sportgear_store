package com.example.sportshop.ui.components.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.viewmodel.CartViewModel

@Composable
fun ProductList(products: List<Product>, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        products.forEach { product ->
            ProductCard(product = product, cartViewModel = cartViewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



