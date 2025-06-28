package com.example.sportshop.ui.components.cart

import UserViewModel
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
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTabContent(
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    LaunchedEffect(userId) {
        cartViewModel.setUser(userId)
    }

    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    var isCheckoutOpen by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    if (isCheckoutOpen && cartItems.isNotEmpty()) {
        CheckoutBottomSheet(
            onDismiss = { isCheckoutOpen = false },
            onSuccess = {},
            navController = navController,
            cartViewModel = cartViewModel,
            userViewModel = userViewModel,
        )
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng tiền")
                        Text(
                            FormatAsVnd.format(totalPrice),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { isCheckoutOpen = true },
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(cartItems) { item ->
                if (item.quantity > 0) {
                    CartItemRow(
                        cartItem = item,
                        cartViewModel = cartViewModel,
                        productViewModel = productViewModel // Pass ProductViewModel
                    )

                    Spacer(Modifier.padding(bottom = 16.dp))
                }
            }
        }
    }
}


