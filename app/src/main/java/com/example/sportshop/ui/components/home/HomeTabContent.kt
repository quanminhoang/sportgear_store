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
    // Lấy danh sách sản phẩm nổi bật từ view model
    val featuredProducts by productViewModel.featuredProducts.collectAsState()

    // Lấy danh sách tất cả sản phẩm từ view model
    val allProducts by productViewModel.products.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tiêu đề sản phẩm nổi bật
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Sản Phẩm Nổi Bật",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Hiển thị các sản phẩm nổi bật
        FeaturedProductsRow(
            products = featuredProducts,
            cartViewModel = cartViewModel,
            navController = navController
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tiêu đề sản phẩm tất cả
        Text(
            text = "Tất Cả Sản Phẩm",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Hiển thị tất cả các sản phẩm
        ProductListWrapper(
            products = allProducts,  // Chắc chắn truyền đúng danh sách
            navController = navController,
            cartViewModel = cartViewModel
        )
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
                cartViewModel = cartViewModel,
                onClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }
    }
}
