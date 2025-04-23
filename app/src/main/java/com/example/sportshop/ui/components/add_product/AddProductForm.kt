package com.example.sportshop.ui.components.add_product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.sportshop.model.data.Product
import com.example.sportshop.viewmodel.ProductViewModel


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
    onFeatureChange: (Boolean) -> Unit,
    onAddClick: (Product) -> Unit  // Thêm callback này thay vì sử dụng viewModel trực tiếp
) {
    var expandedCategoryMenu by remember { mutableStateOf(false) }
    val categories = listOf("Dụng Cụ Thể Thao", "Giày", "Quần", "Áo")
    val clipboardManager = LocalClipboardManager.current

    Column(modifier = Modifier.padding(padding)) {
        // Các TextField cho tên, giá, mô tả, số lượng
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

        // Dropdown cho danh mục
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
                    .fillMaxWidth(),
                isError = category.isBlank()
            )
            ExposedDropdownMenu(
                expanded = expandedCategoryMenu,
                onDismissRequest = { expandedCategoryMenu = false }
            ) {
                categories.forEach { categoryOption ->
                    DropdownMenuItem(
                        text = { Text(categoryOption) },
                        onClick = {
                            onCategoryChange(categoryOption)
                            expandedCategoryMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TextField cho URL ảnh sản phẩm
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = imageUrl,
                onValueChange = onImageUrlChange,
                label = { Text("URL ảnh sản phẩm") },
                modifier = Modifier.weight(0.8f),
                maxLines = 1
            )

            Button(
                onClick = {
                    clipboardManager.getText()?.let {
                        onImageUrlChange(it.text)
                    }
                },
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 8.dp)
            ) {
                Text("Dán")
            }
        }
        // Trong AddProductForm, thêm đoạn mã sau sau TextField cho URL ảnh
        if (imageUrl.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("URL hình ảnh đã được nhập: $imageUrl")

            // Tùy chọn: Hiển thị preview nếu URL hợp lệ
            AsyncImage(
                model = imageUrl,
                contentDescription = "Preview",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit,
//                error = painterResource(id = R.drawable.ic_broken_image), // Nếu URL không hợp lệ
//                placeholder = painterResource(id = R.drawable.ic_image_placeholder) // Trong khi đang tải
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox cho sản phẩm nổi bật
        Row {
            Checkbox(
                checked = feature,
                onCheckedChange = onFeatureChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Sản phẩm nổi bật",
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Nút thêm sản phẩm
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val newProduct = Product(
                    name = name,
                    price = price.toDouble(),
                    description = description,
                    quantity = quantity.toInt(),
                    category = category,
                    imageUrl = imageUrl,
                    feature = feature
                )
                onAddClick(newProduct)  // Gọi callback thay vì gọi viewModel trực tiếp
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm sản phẩm")
        }
    }
}