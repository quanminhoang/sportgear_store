package com.example.sportshop.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTabContent(navController: NavController, cartViewModel: CartViewModel) {
    // Lấy userId từ Firebase
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    // Gọi setUser một lần để load cart của user
    LaunchedEffect(userId) {
        cartViewModel.setUser(userId)
    }

    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    // Nếu giỏ hàng trống
    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Giỏ hàng đang trống", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng tiền")
                        Text(
                            "₫${"%,.0f".format(totalPrice)}",
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            navController.navigate("checkout")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Mua Hàng", modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
    ) { paddingValues ->

        // Hiển thị danh sách các sản phẩm trong giỏ hàng
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = 6.dp)
        ) {
            item { Spacer(modifier = Modifier.height(5.dp)) }

            // Duyệt qua các sản phẩm trong giỏ hàng và hiển thị chúng
            items(cartItems) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.outline)
                ) {
                    CartItemRow(cartItem = item, cartViewModel = cartViewModel)  // Sửa lại từ cartItems = item thành cartItem = item
                }
            }
        }
    }
}

