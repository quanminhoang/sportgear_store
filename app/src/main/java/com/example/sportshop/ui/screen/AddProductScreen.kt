package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.add_product.AddProductForm
import com.example.sportshop.ui.components.add_product.AddProductTopBar
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun AddProductScreen(
    navcontroller: NavController,
    product: Product,
    onSave: (Product) -> Unit,
    adminviewModel: AdminViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var description by remember { mutableStateOf(product.description) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var category by remember { mutableStateOf(product.category) }
    var imageUrl by remember { mutableStateOf(product.imageUrl) }
    var feature by remember { mutableStateOf(product.feature) }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AddProductTopBar(
                navController = navcontroller,
                onSaveClick = {
                    val parsedPrice = price.replace(",", "").toDoubleOrNull()
                    val parsedQuantity = quantity.toIntOrNull()

                    when {
                        name.isBlank() || description.isBlank() || category.isBlank() -> {
                            errorMessage = "Vui lòng nhập đầy đủ thông tin"
                            showError = true
                        }

                        parsedPrice == null -> {
                            errorMessage = "Giá không hợp lệ"
                            showError = true
                        }

                        parsedQuantity == null -> {
                            errorMessage = "Số lượng không hợp lệ"
                            showError = true
                        }

                        else -> {
                            showError = false
                            val updatedProduct = product.copy(
                                name = name,
                                price = parsedPrice,
                                description = description,
                                quantity = parsedQuantity,
                                category = category,
                                imageUrl = imageUrl,
                                feature = feature
                            )
                            onSave(updatedProduct)
                            showSuccessDialog = true
                        }
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize()) {
                AddProductForm(
                    padding = padding,
                    name = name,
                    onNameChange = { name = it },
                    price = price,
                    onPriceChange = { price = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    category = category,
                    onCategoryChange = { category = it },
                    imageUrl = imageUrl,
                    onImageUrlChange = { imageUrl = it },
                    feature = feature,
                    onFeatureChange = { feature = it },
                    onAddClick = { newProduct ->
                        productViewModel.addProduct(newProduct)
                        navcontroller.popBackStack()
                    }
                )

                if (showError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showSuccessDialog = false
                        navcontroller.popBackStack()
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showSuccessDialog = false
                            navcontroller.popBackStack()
                        }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Thành công") },
                    text = { Text("Sản phẩm đã được lưu thành công.") }
                )
            }
        }
    )
}
