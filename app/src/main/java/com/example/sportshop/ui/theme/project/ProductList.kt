package com.example.sportshop.ui.theme.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportshop.R

// Product List
@Composable
fun ProductList() {
    val products = listOf(
        Product("Giày Thể Thao", "5,233,470 VND", "5 WID (%)", R.drawable.nike_shoes),
        Product("Áo thể thao", "1,019,217 VND", "56 WID (23%)", R.drawable.adidas_shirt),
        Product("Quả Bóng Đá", "1,019,217 VND", "56 WID (23%)", R.drawable.puma_ball),
        Product("Quần Jogger", "500,000 VND", "56 WID (23%)", R.drawable.jogger)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        products.forEach { product ->
            ProductCard(product)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}