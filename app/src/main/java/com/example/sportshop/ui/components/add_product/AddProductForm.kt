package com.example.sportshop.ui.components.add_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color

import java.text.NumberFormat
import java.util.Locale


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
    imageUrls: List<String>,
    onAddImage: (String) -> Unit,
    onRemoveImage: (Int) -> Unit,
    feature: Boolean,
    onFeatureChange: (Boolean) -> Unit,
    onAddClick: (Product) -> Unit  // Callback khi thêm sản phẩm
) {
    var expandedCategoryMenu by remember { mutableStateOf(false) }
    val categories = listOf("Dụng Cụ Thể Thao", "Giày", "Quần", "Áo")
    val clipboardManager = LocalClipboardManager.current
    var imageUrlInput by remember { mutableStateOf("") }
    var imageUrlError by remember { mutableStateOf(false) } // Thêm bi���n báo lỗi

    fun isValidImageUrl(url: String): Boolean {
        val regex =
            Regex("^https?://.*\\.(jpg|jpeg|png|gif|webp)(\\?.*)?\$", RegexOption.IGNORE_CASE)
        return regex.matches(url.trim())
    }

    // Hàm format số tiền: 3000 -> 3.000
    fun formatCurrencyInput(input: String): String {
        val clean = input.replace(".", "").replace(",", "")
        if (clean.isBlank()) return ""
        return try {
            val parsed = clean.toLong()
            NumberFormat.getInstance(Locale("vi", "VN")).format(parsed)
        } catch (e: Exception) {
            input
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = imageUrlInput,
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
                },
                modifier = Modifier.height(50.dp)
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
                    if (!imageLoadError) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Ảnh sản phẩm $index",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Crop,
                            onError = { imageLoadError = true }
                        )
                        IconButton(
                            onClick = { onRemoveImage(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp) // Khoảng cách với mép
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
                    if (imageLoadError) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = "Không thể tải ảnh"
                            )
                        }
                        // Thêm nút xoá cho ảnh lỗi
                        IconButton(
                            onClick = { onRemoveImage(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Xoá ảnh"
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
            onValueChange = {
                // Chỉ cho phép nhập số, tự động format kiểu Việt Nam (dùng dấu . cho hàng nghìn)
                val clean = it.replace(".", "").replace(",", "")
                if (clean.all { c -> c.isDigit() } || clean.isBlank()) {
                    val formatted = try {
                        if (clean.isNotBlank()) {
                            NumberFormat.getNumberInstance(Locale("vi", "VN"))
                                .format(clean.toLong())
                        } else ""
                    } catch (e: Exception) {
                        clean
                    }
                    onPriceChange(formatted)
                }
            },
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
            ExposedDropdownMenu(
                expanded = expandedCategoryMenu,
                onDismissRequest = { expandedCategoryMenu = false }
            ) {
                categories.forEach { categoryOption ->
                    DropdownMenuItem(text = { Text(categoryOption) }, onClick = {
                        onCategoryChange(categoryOption)
                        expandedCategoryMenu = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = quantity,
            onValueChange = onQuantityChange,
            label = { Text("Số lượng sản phẩm") },
            isError = quantity.isBlank() || quantity.toIntOrNull() == null,
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
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Mô tả sản phẩm") },
            isError = description.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            maxLines = 5,
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
                text = "Sản phẩm nổi bật", modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
