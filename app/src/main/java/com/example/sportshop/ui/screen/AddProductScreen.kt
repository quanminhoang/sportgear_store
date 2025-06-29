package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.add_product.AddProductForm
import com.example.sportshop.ui.components.add_product.AddProductTopBar
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navcontroller: NavController,
    product: Product,
    onSave: (Product, (Boolean, String) -> Unit) -> Unit,
    adminviewModel: AdminViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var description by remember { mutableStateOf(product.description) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var collection by remember { mutableStateOf(product.collection) }
    var category by remember { mutableStateOf(product.category) }
    var imageUrls by remember { mutableStateOf(product.imageUrls.toMutableList()) }
    var feature by remember {
        mutableStateOf(product.feature)
    }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = {  SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            shape = RoundedCornerShape(12.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 30.dp)
        )
    }
    }, topBar = {
        AddProductTopBar(navController = navcontroller, onSaveClick = {
            val parsedPrice = price.replace(",", "").toDoubleOrNull()
            val parsedQuantity = quantity.toIntOrNull()

            when {
                name.isBlank() || description.isBlank() || category.isBlank() {
                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
                    showError = true
                }

                name.length <= 10 -> {
                    errorMessage = "Tên sp phải có ít nhất 10 kí tự "
                    showError = true
                }

                description.length <= 10 -> {
                    errorMessage = "Mô tả sp phải có ít nhất 10 kí tự"
                    showError = true
                }

                parsedPrice == null -> {
                    errorMessage = "Giá không hợp lệ"
                    showError = true
                }

                parsedPrice < 1000 -> {
                    errorMessage = "Giá phải ít nhất 1.000"
                    showError = true
                }

                parsedPrice == null -> {
                    errorMessage = "Giá không hợp lệ"
                    showError = true
                }

                parsedQuantity == null -> {
                    errorMessage = "Số lượng phai o dang so"
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
                        collection = collection,
                        imageUrls = imageUrls,
                        feature = feature
                    )
                    onSave(updatedProduct) { success, message ->
                        if (success) {
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                                delay(500)
                                navcontroller.popBackStack()
                            }
                        } else {
                            errorMessage = message
                            showError = true
                        }
                    }
                }
            }
        })
    }, content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            AddProductForm(padding = PaddingValues(0.dp),
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
                collection = collection,
                onCollectionChange = { collection = it },
                imageUrls = imageUrls,
                onAddImage = { url ->
                    imageUrls = imageUrls.toMutableList().apply { add(url) }
                },
                onRemoveImage = { index ->
                    imageUrls = imageUrls.toMutableList().apply { removeAt(index) }
                },
                feature = feature,
                onFeatureChange = { feature = it },
                onAddClick = { newProduct ->
                    productViewModel.addProduct(newProduct)
                    navcontroller.popBackStack()
                })

            if (showError) {
                LaunchedEffect(errorMessage) {
                    val result = snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = "Ẩn",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    showError = false
                    if (result == SnackbarResult.ActionPerformed) {
                    }
                }
            }

        }
    })
}