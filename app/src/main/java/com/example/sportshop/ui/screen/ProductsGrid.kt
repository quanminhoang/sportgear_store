package com.example.sportshop.ui.screen

import ProductCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product

@Composable
fun ProductsGrid(
    displayProducts: List<Product>,
    navController: NavController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp) // Receives padding from Scaffold
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding()), // Apply top padding here
        contentPadding = PaddingValues(0.dp), // Apply no additional padding here
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(displayProducts) { product ->
            ProductCard(product = product, onClick = { productId ->
                navController.navigate("product_detail/$productId")
            })
        }
    }
}

