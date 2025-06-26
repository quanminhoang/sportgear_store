package com.example.sportshop.ui.components.add_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import coil.compose.AsyncImage
import com.example.sportshop.model.data.Product


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
        // TextField cho URL ảnh sản phẩm
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.surface),// Add padding around the card
            shape = MaterialTheme.shapes.medium // Customize the card's shape if needed
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)  // Add padding inside the card for spacing
            ) {
                // Image URL section
                TextField(
                    value = imageUrl,
                    onValueChange = onImageUrlChange,
                    label = { Text("URL ảnh sản phẩm") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Button(
                    onClick = {
                        clipboardManager.getText()?.let {
                            onImageUrlChange(it.text)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Dán")
                }

                // Image preview if URL is not empty
                if (imageUrl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    var imageLoading by remember { mutableStateOf(true) }
                    var imageError by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!imageError) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Preview",
                                modifier = Modifier
                                    .matchParentSize(),
                                contentScale = ContentScale.Fit,
                                onLoading = { imageLoading = true },
                                onSuccess = {
                                    imageLoading = false
                                    imageError = false
                                },
                                onError = {
                                    imageLoading = false
                                    imageError = true
                                }
                            )
                        }
                        if (imageLoading) {
                            CircularProgressIndicator()
                        }
                        if (imageError) {
                            // Hiển thị hình "No image available" (dùng icon mặc định của Material)
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BrokenImage,
                                    contentDescription = "No image available",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "No image available",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Product name, price, category, and description fields
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

                TextField(
                    value = quantity,
                    onValueChange = onQuantityChange,
                    label = { Text("Số lượng") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Mô tả sản phẩm") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Featured checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = feature,
                        onCheckedChange = onFeatureChange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Sản phẩm nổi bật",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}