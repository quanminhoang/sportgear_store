package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, cartViewModel: CartViewModel) {
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("cash") }
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh Toán") },
                navigationIcon = { Btn_Back(navController) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Địa chỉ nhận hàng") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Phương thức thanh toán", style = MaterialTheme.typography.titleMedium)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = paymentMethod == "cash",
                    onClick = { paymentMethod = "cash" }
                )
                Icon(
                    Icons.Default.AttachMoney,
                    contentDescription = "Tiền mặt",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Tiền mặt")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = paymentMethod == "bank",
                    onClick = { paymentMethod = "bank" }
                )
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = "Ngân hàng",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Ngân hàng")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sản phẩm trong giỏ hàng:", style = MaterialTheme.typography.titleMedium)

            cartItems.forEach { item ->
                CheckoutCartItem(item = item)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Tổng tiền: ₫${"%.2f".format(totalPrice)}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (address.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Vui lòng nhập địa chỉ nhận hàng.")
                        }
                        return@Button
                    }

                    cartViewModel.placeOrder(
                        address = address,
                        paymentMethod = paymentMethod,
                        onSuccess = {
                            showSuccessDialog = true
                            cartViewModel.clearCart()
                        },
                        onFailure = { e ->
                            scope.launch {
                                snackbarHostState.showSnackbar("Lỗi: ${e.localizedMessage}")
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Xác nhận đơn hàng")
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("🎉 Thành công", style = MaterialTheme.typography.titleLarge) },
                text = { Text("Đặt hàng thành công! Cảm ơn bạn đã mua sắm tại SportShop.") },
                confirmButton = {
                    TextButton(onClick = {
                        showSuccessDialog = false
                        navController.navigate("home") {
                            popUpTo(0) // Xoá toàn bộ back stack
                        }
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// ✅ Di chuyển ra ngoài hàm CheckoutScreen
@Composable
fun CheckoutCartItem(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "₫${"%.2f".format(item.price)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(text = "x${item.quantity}", style = MaterialTheme.typography.bodyMedium)
    }
}
