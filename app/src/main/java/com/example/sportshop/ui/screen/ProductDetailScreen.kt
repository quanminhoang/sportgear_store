package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.sportshop.model.data.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    navController: NavController,
    onAddToCart: (Product) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hiển thị hình ảnh sản phẩm từ URL mà không có placeholder
            AsyncImage(
                model = product.imageUrl,  // URL của hình ảnh sản phẩm
                contentDescription = "Hình ảnh sản phẩm",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)  // Đặt kích thước của hình ảnh
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tên sản phẩm
            Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Giá sản phẩm
            Text(
                text = "₫${product.price}",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Red)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Mô tả sản phẩm
            Text(text = "Mô tả:", style = MaterialTheme.typography.titleSmall)
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Nút thêm vào giỏ hàng
            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thêm vào giỏ hàng")
            }
        }
    }
}

