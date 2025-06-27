package com.example.sportshop.ui.components.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.util.QuantityEditor
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun CartItemRow(
    cartItem: CartItem, cartViewModel: CartViewModel, productViewModel: ProductViewModel
) {

    val product by productViewModel.products.collectAsState()
    val productStock = product.find { it.id == cartItem.id }?.quantity ?: 99

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(160.dp),
    ) {
        AsyncImage(
            model = cartItem.imageUrl,
            contentDescription = cartItem.name,
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = cartItem.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = FormatAsVnd.format(cartItem.price * cartItem.quantity),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                QuantityEditor(
                    quantity = cartItem.quantity, onQuantityChange = { newQuantity ->
                        cartItem.id?.let { id ->
                            cartViewModel.updateQuantity(id, newQuantity)
                        }
                    }, min = 1, max = productStock // Cho phep ton kho la max
                )
            }
        }
        IconButton(onClick = {
            cartItem.id?.let { cartViewModel.removeItem(it) }
        }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Xoá",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline
    )
}