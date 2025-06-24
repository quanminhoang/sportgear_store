package com.example.sportshop.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.product.FeatureProductCard
import com.example.sportshop.viewmodel.ProductViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

@Composable
fun HomeTabContent(
    productViewModel: ProductViewModel, navController: NavController, modifier: Modifier = Modifier
) {
    val featuredProducts by productViewModel.featuredProducts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp) // tránh tràn lề
    ) {
        BannerSlider()

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dành riêng cho bạn",
                style = MaterialTheme.typography.titleLarge, // Sửa thành titleLarge
                color = MaterialTheme.colorScheme.onBackground // Sửa color
            )
            TextButton(onClick = {
                navController.navigate("all_products?featured=true")
            }) {
                Text(
                    text = "Xem tất cả",
                    style = MaterialTheme.typography.titleMedium, // Đổi style cho "Xem tất cả"
                    color = MaterialTheme.colorScheme.primary // Đổi màu
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FeaturedProductsGrid(
            products = featuredProducts,
            navController = navController
        )
    }
}

@Composable
fun FeaturedProductsGrid(
    products: List<Product>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(products) { product ->
            FeatureProductCard(product = product, onClick = { productId ->
                navController.navigate("product_detail/$productId")
            })
        }
    }
}
