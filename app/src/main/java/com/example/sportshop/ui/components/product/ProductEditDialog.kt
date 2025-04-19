package com.example.sportshop.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.sportshop.model.data.Product


@Composable
fun ProductEditDialog(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var imageUrl by remember { mutableStateOf(product.imageUrl) }
    var description by remember { mutableStateOf(product.description) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var category by remember { mutableStateOf(product.category) }
    var discount by remember { mutableStateOf(product.discount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSave(
                    product.copy(
                        name = name,
                        price = price.replace(",", "").toDoubleOrNull() ?: 0.0,
                        imageUrl = imageUrl,
                        description = description,
                        quantity = quantity.toIntOrNull() ?: 0,
                        category = category,
                        discount = discount.toDoubleOrNull() ?: 0.0
                    )
                )
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        title = { Text("Sản phẩm") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá") })
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Ảnh URL") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Số lượng") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Danh mục") })
                OutlinedTextField(value = discount, onValueChange = { discount = it }, label = { Text("Giảm giá (%)") })
            }
        }
    )
}
