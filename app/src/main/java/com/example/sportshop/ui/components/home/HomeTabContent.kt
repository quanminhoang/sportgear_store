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

@Composable
fun HomeTabContent(
    productViewModel: ProductViewModel, navController: NavController, modifier: Modifier = Modifier
) {
    val featuredProducts by productViewModel.featuredProducts.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
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

        FeaturedProductsRow(
            products = featuredProducts,
            navController = navController
        )
    }
}

@Composable
fun FeaturedProductsRow(
    products: List<Product>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(products) { product ->
            FeatureProductCard(product = product, onClick = { productId ->
                navController.navigate("product_detail/$productId")
            })
        }
    }
}
