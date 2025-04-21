package com.example.sportshop.ui.components.product

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.model.data.Product
import com.example.sportshop.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductEditDialog(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var imageUrl by remember { mutableStateOf(product.imageUrl) }
    var description by remember { mutableStateOf(product.description) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var category by remember { mutableStateOf(product.category) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val uploadedUrl = viewModel.uploadImage(it)
                Log.e("hinh", uploadedUrl.toString())
                if (uploadedUrl != null) {
                    imageUrl = uploadedUrl
                } else {
                    // Optional: show error
                }
            }
        }
    }

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
                TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Chọn ảnh từ thiết bị")
                }
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Số lượng") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Danh mục") })
            }
        }
    )
}
