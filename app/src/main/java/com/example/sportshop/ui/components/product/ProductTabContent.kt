package com.example.sportshop.components.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sportshop.ui.components.product.ProductListWrapper
import com.example.sportshop.ui.viewmodel.CartViewModel

@Composable
fun ProductTabContent(
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
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
        ProductListWrapper(cartViewModel = cartViewModel)
    }
}