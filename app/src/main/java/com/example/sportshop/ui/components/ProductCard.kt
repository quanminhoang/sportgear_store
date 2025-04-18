package com.example.sportshop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sportshop.Product
import com.example.sportshop.CartItem
import com.example.sportshop.ui.viewmodel.CartViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.UUID

@Composable
fun ProductCard(
    product: Product,
    cartViewModel: CartViewModel // Sử dụng ViewModel mặc định
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Load ảnh từ URL
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 👇 Hiển thị giá
                Text(
                    text = "₫${product.price}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            IconButton(
                onClick = {
                    val cartItem = CartItem(
                        id = product.id ?: UUID.randomUUID().toString(),
                        name = product.name,
                        price = product.price,
                        quantity = 1,
                        imageUrl = product.imageUrl
                    )
                    cartViewModel.addToCart(cartItem)
                },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Thêm vào giỏ hàng"
                )
            }
        }
    }
}
