package com.example.sportshop.ui.components.add_product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sportshop.R


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
    collection: String,
    onCollectionChange: (String) -> Unit,
    imageUrls: List<String>,
    onAddImage: (String) -> Unit,
    onRemoveImage: (Int) -> Unit,
    feature: Boolean,
    onFeatureChange: (Boolean) -> Unit,
    onAddClick: (Product) -> Unit,  // Callback khi thêm sản phẩm
    isLoading: Boolean = false      // Thêm biến trạng thái loading
) {
    var expandedCategoryMenu by remember { mutableStateOf(false) }
    val categories = listOf( "Giày", "Quần", "Áo")
    val collections = listOf("Nike", "Adidas", "Puma", "New Balance")
    val clipboardManager = LocalClipboardManager.current
    var imageUrlInput by remember { mutableStateOf("") }
    var imageUrlError by remember { mutableStateOf(false) } // Thêm bi���n báo lỗi

    fun isValidImageUrl(url: String): Boolean {
        val regex =
            Regex("^https?://.*\\.(jpg|jpeg|png|gif|webp)(\\?.*)?\$", RegexOption.IGNORE_CASE)
        return regex.matches(url.trim())
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(value = imageUrlInput,
                    onValueChange = { imageUrlInput = it },
                    label = { Text("Nhập URL ảnh sản phẩm") },
                    isError = imageUrlError,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        errorIndicatorColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (imageUrlInput.isNotBlank() && isValidImageUrl(imageUrlInput)) {
                            onAddImage(imageUrlInput.trim())
                            imageUrlInput = ""
                            imageUrlError = false
                        } else {
                            imageUrlError = true
                        }
                    }, modifier = Modifier.height(50.dp)
                ) {
                    Text("Thêm")
                }
            }
            if (imageUrlError) {
                Text(
                    text = "URL không hợp lệ. Vui lòng nhập link ảnh hợp lệ (.jpg, .png, ...)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                itemsIndexed(imageUrls) { index, url ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(160.dp)
                    ) {
                        var imageLoadError by remember(url) { mutableStateOf(false) }
                        var imageLoading by remember(url) { mutableStateOf(true) }
                        if (!imageLoadError) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(model = url,
                                    contentDescription = "Ảnh sản phẩm $index",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentScale = ContentScale.FillWidth,
                                    onLoading = { imageLoading = true },
                                    onSuccess = { imageLoading = false },
                                    onError = { imageLoadError = true })
                                if (imageLoading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = MaterialTheme.colorScheme.surface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { onRemoveImage(index) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .background(
                                            Color.Black.copy(alpha = 0.5f), shape = CircleShape
                                        )
                                        .size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Xóa",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        // Hiển thị ảnh lỗi
                        if (imageLoadError) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_no_picture_available),
                                    contentDescription = "Ảnh lỗi",
                                    contentScale = ContentScale.FillWidth,
                                )
                            }
                            // Nút xoá ảnh đang hiển thị
                            IconButton(
                                onClick = { onRemoveImage(index) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Xóa",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Tên sản phẩm") },
                isError = name.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text("Giá sản phẩm") },
                isError = price.isBlank() || price.replace(".", "").replace(",", "")
                    .toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  Row Danh muc và checkbox nổi bật
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = onQuantityChange,
                        label = { Text("Số lượng sản phẩm") },
                        isError = quantity.isBlank() || quantity.toIntOrNull() == null,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            errorContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                            errorIndicatorColor = MaterialTheme.colorScheme.error,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        )
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Nổi bật",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Checkbox(
                        checked = feature,
                        onCheckedChange = onFeatureChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(expanded = expandedCategoryMenu,
                onExpandedChange = { expandedCategoryMenu = it }) {
                OutlinedTextField(value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Danh mục") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    isError = category.isBlank(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        errorIndicatorColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                ExposedDropdownMenu(expanded = expandedCategoryMenu,
                    onDismissRequest = { expandedCategoryMenu = false }) {
                    categories.forEach { categoryOption ->
                        DropdownMenuItem(text = { Text(categoryOption) }, onClick = {
                            onCategoryChange(categoryOption)
                            expandedCategoryMenu = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            var expandedCollectionMenu by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(expanded = expandedCollectionMenu,
                onExpandedChange = { expandedCollectionMenu = it }) {
                OutlinedTextField(
                    value = collection,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Dòng giày (có thể bỏ trống)") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCollectionMenu)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        errorIndicatorColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )

                ExposedDropdownMenu(expanded = expandedCollectionMenu,
                    onDismissRequest = { expandedCollectionMenu = false }) {
                    collections.forEach { collectionOption ->
                        DropdownMenuItem(text = { Text(collectionOption) }, onClick = {
                            onCollectionChange(collectionOption)
                            expandedCollectionMenu = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Mô tả sản phẩm") },
                isError = description.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                singleLine = false,
            )
        }
    }
}
