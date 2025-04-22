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

@Composable
fun AddProductScreen(
    navcontroller: NavController,
    product: Product,
    onSave: (Product) -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var description by remember { mutableStateOf(product.description) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var category by remember { mutableStateOf(product.category) }
    var imageUrl by remember { mutableStateOf("") }
    var feature by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
                            onSave(
                                product.copy(
                                    name = name,
                                    price = parsedPrice,
                                    description = description,
                                    quantity = parsedQuantity,
                                    category = category,
                                    imageUrl = imageUrl // Lưu giá trị imageUrl
                                )
                            )
                        }
                    }
                }
            )
        },
        content = { padding ->
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
                onImageUrlChange = { imageUrl = it }, // Trường nhập URL ảnh
                feature = feature,
                onFeatureChange = { feature = it }
            )

            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    )
}
