package com.example.sportshop.ui.components


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportshop.ui.viewmodel.ProductViewModel

@Composable
fun ProductListWrapper() {
    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()

    ProductList(products = products)
}
