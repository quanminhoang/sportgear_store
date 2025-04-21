package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.product.ProductEditDialog
import com.example.sportshop.viewmodel.AdminViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController, viewModel: AdminViewModel = viewModel()) {
    val products = viewModel.products
    val showDialog = viewModel.showDialog
    val editingProduct = viewModel.editingProduct

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Quản Lý Sản Phẩm", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("admin") {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay về Trang Chủ")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.editingProduct = Product()
                    viewModel.showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm sản phẩm")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text("₫${product.price}", color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                viewModel.editingProduct = product
                                viewModel.showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Sửa")
                            }
                            IconButton(onClick = {
                                viewModel.deleteProduct(product.id!!)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Xoá")
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            ProductEditDialog(
                product = editingProduct ?: Product(),
                onDismiss = { viewModel.showDialog = false },
                onSave = {
                    viewModel.saveProduct(it)
                    viewModel.showDialog = false
                }
            )
        }
    }
}

