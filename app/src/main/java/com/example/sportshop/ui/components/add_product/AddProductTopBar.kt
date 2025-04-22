package com.example.sportshop.ui.components.add_product

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.sportshop.ui.components.buttons.Btn_Back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductTopBar(
    navController: NavController,
    onSaveClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Thêm sản phẩm",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        navigationIcon = {
            Btn_Back(navController)
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
