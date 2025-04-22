package com.example.sportshop.ui.components.add_product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(
    padding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    feature: Boolean,
    onFeatureChange: (Boolean) -> Unit
) {
    // State quản lý expanded của menu category
    var expandedCategoryMenu by remember { mutableStateOf(false) }

    // Dữ liệu danh mục (ví dụ chỉ có 3 danh mục)
    val categories = listOf("Dụng Cụ Thể Thao", "Giày", "Quần Áo")

    // State để quản lý giá trị của category
    var selectedCategory by remember { mutableStateOf(category) }

    Column(modifier = Modifier.padding(padding)) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Tên sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("Giá sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Mô tả sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = quantity,
            onValueChange = onQuantityChange,
            label = { Text("Số lượng") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown menu cho danh mục
        ExposedDropdownMenuBox(
            expanded = expandedCategoryMenu,
            onExpandedChange = { expandedCategoryMenu = it }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Danh mục") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = selectedCategory.isBlank()
            )

            ExposedDropdownMenu(
                expanded = expandedCategoryMenu,
                onDismissRequest = { expandedCategoryMenu = false }
            ) {
                categories.forEach { categoryOption ->
                    DropdownMenuItem(
                        text = { Text(categoryOption) },
                        onClick = {
                            selectedCategory = categoryOption
                            expandedCategoryMenu = false
                            onCategoryChange(categoryOption)  // Thực thi hàm truyền vào
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = imageUrl,
            onValueChange = onImageUrlChange,
            label = { Text("URL ảnh sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Checkbox(
            checked = feature,
            onCheckedChange = onFeatureChange,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Sản phẩm nổi bật",
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
