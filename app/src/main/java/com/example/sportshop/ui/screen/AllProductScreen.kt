package com.example.sportshop.ui.components.screen

import Btn_Search
import ProductCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.ui.screen.ProductsGrid
import com.example.sportshop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    productViewModel: ProductViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    featured: Boolean = false,
    category: String? = null
) {
    val products by productViewModel.allProducts.collectAsState()

    val displayProducts = products.filter { product ->
        (!featured || product.feature) && (category == null || product.category.equals(category, ignoreCase = true))
    }
    val dynamicTitle = when {
        category != null -> "Category: ${category.replaceFirstChar { it.uppercase() }}"
        featured -> "Sản phẩm nổi bật"
        else -> ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = dynamicTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    Btn_Back(navController)
                },
                actions = {
                    Btn_Search(navController)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        content = { innerPadding ->
            ProductsGrid(
                displayProducts = displayProducts,
                navController = navController,
                modifier = modifier,
                innerPadding = innerPadding // Apply the padding here
            )
        }
    )
}

