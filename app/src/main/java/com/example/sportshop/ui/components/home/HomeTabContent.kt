package com.example.sportshop.ui.components.home

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.product.ProductCard
import com.example.sportshop.ui.components.product.ProductListWrapper
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun HomeTabContent(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val featuredProducts by productViewModel.featuredProducts.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Featured Products Section
        Text(
            text = "Sản Phẩm Nổi Bật",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        FeaturedProductsRow(
            products = featuredProducts,
            cartViewModel = cartViewModel,
            navController = navController
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Regular Products Section
        Text(
            text = "Tất Cả Sản Phẩm",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProductListWrapper(cartViewModel = cartViewModel)
    }
}

@Composable
fun FeaturedProductsRow(
    products: List<Product>,
    cartViewModel: CartViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                cartViewModel
            )
        }
    }
}