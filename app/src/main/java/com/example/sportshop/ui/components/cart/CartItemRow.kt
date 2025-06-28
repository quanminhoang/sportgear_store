package com.example.sportshop.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemRow(
    cartItem: CartItem,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel
) {
    val product by productViewModel.products.collectAsState()
    val productStock = product.find { it.id == cartItem.id }?.quantity ?: 99
    val scope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                cartItem.id?.let { id ->
                    scope.launch {
                        cartViewModel.removeItem(id)
                    }
                }
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false, // Only allow swipe from end to start
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xoá",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp)
                )
            }
        },
        content = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 12.dp),
                ) {
                    AsyncImage(
                        model = cartItem.imageUrl,
                        contentDescription = cartItem.name,
                        modifier = Modifier
                            .size(160.dp)
                            .padding(start = 12.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 12.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = cartItem.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = FormatAsVnd.format(cartItem.price * cartItem.quantity),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        QuantityEditor(
                            quantity = cartItem.quantity,
                            onQuantityChange = { newQuantity ->
                                cartItem.id?.let { id ->
                                    cartViewModel.updateQuantity(id, newQuantity)
                                }
                            },
                            min = 1,
                            max = productStock
                        )

                        Spacer(Modifier.padding(bottom = 2.dp))
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}