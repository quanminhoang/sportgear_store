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
    category: String? = null,
    collection: String? = null
) {
    val products by productViewModel.allProducts.collectAsState()

    // Clean parameters
    val validCategory = category?.takeIf { it.isNotBlank() }
    val validCollection = collection?.takeIf { it.isNotBlank() }

    val displayProducts = products.filter { product ->
        (!featured || product.feature) &&
                (validCategory == null || product.category.equals(validCategory, ignoreCase = true)) &&
                (validCollection == null || product.collection.equals(validCollection, ignoreCase = true))
    }

    val dynamicTitle = when {
        validCategory != null -> validCategory.replaceFirstChar { it.uppercase() }
        featured -> "Nổi bật"
        validCollection != null -> validCollection.replaceFirstChar { it.uppercase() }
        else -> "Sản phẩm"
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

