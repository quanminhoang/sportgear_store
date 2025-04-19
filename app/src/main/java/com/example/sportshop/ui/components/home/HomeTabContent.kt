package com.example.sportshop.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.ui.components.product.ProductListWrapper
import com.example.sportshop.ui.viewmodel.AdminViewModel
import com.example.sportshop.ui.viewmodel.CartViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeTabContent(
    isAdmin: Boolean,
    navController: NavController,
    cartViewModel: CartViewModel,
    adminViewModel: AdminViewModel
) {
    val isRefreshing by adminViewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { adminViewModel.fetchProducts() }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SpecialOfferCard()
            Spacer(modifier = Modifier.height(24.dp))
            TopCategoriesSection()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sản Phẩm Nổi Bật",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProductListWrapper(cartViewModel = cartViewModel)
            Spacer(modifier = Modifier.height(32.dp))

            if (isAdmin) {
                Button(
                    onClick = { navController.navigate("admin") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vào trang Admin")
                }
            }
        }
    }
}