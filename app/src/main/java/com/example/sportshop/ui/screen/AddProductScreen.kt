package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val categories = listOf("Giày", "Quần", "Áo", "Phụ kiện")
    var expandedCategoryMenu by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chỉnh sửa sản phẩm") },
                navigationIcon = {
                    Btn_Back(navcontroller)
                },
                actions = {
                    IconButton(onClick = {
                        val parsedPrice = price.replace(",", "").toDoubleOrNull()
                        val parsedQuantity = quantity.toIntOrNull()

                        if (parsedPrice != null && parsedQuantity != null) {
                            onSave(
                                product.copy(
                                    name = name,
                                    price = parsedPrice,
                                    description = description,
                                    quantity = parsedQuantity,
                                    category = category
                                )
                            )
                        } else {
                            // Thông báo nếu giá hoặc số lượng không hợp lệ
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Lưu")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên Sản Phẩm") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Giá") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = price.replace(",", "").toDoubleOrNull() == null
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Số lượng") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = quantity.toIntOrNull() == null
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCategoryMenu,
                    onExpandedChange = { expandedCategoryMenu = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Danh mục") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategoryMenu,
                        onDismissRequest = { expandedCategoryMenu = false }
                    ) {
                        categories.forEach { categoryOption ->
                            DropdownMenuItem(
                                text = { Text(categoryOption) },
                                onClick = {
                                    category = categoryOption
                                    expandedCategoryMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
