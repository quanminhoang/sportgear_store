package com.example.sportshop.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.viewmodel.CartViewModel

@Composable
fun CartItemRow(cartItem: CartItem, cartViewModel: CartViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = cartItem.imageUrl,
            contentDescription = cartItem.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${FormatAsVnd.format(cartItem.price)}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Số lượng: ${cartItem.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(
            onClick = {
                cartItem.id?.let { cartViewModel.removeItem(it) }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Xoá",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

