package com.example.sportshop.ui.components.cart

import UserViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    onDismiss: () -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel
) {
    val fullName by userViewModel.fullName.collectAsState()
    val phone by userViewModel.phone.collectAsState()
    val userAddress by userViewModel.address.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("cash") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(userAddress) {
        if (address.isBlank()) {
            address = userAddress
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }, modifier = Modifier.fillMaxWidth()
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(
                    "Thanh toán",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { userViewModel.updateUserInfo(it, phone, address) },
                    label = { Text("Người nhận") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { userViewModel.updateUserInfo(fullName, it, address) },
                    label = { Text("SĐT (0XXXXXXXXX)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = address, onValueChange = {
                    address = it
                    userViewModel.updateUserInfo(fullName, phone, it)
                }, label = { Text("Địa chỉ nhận hàng") }, modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Phương thức thanh toán", style = MaterialTheme.typography.titleMedium)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = paymentMethod == "cash",
                        onClick = { paymentMethod = "cash" })
                    Icon(
                        Icons.Default.AttachMoney,
                        contentDescription = "Tiền mặt",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Tiền mặt")

                    Spacer(modifier = Modifier.width(12.dp))

                    RadioButton(
                        selected = paymentMethod == "bank",
                        onClick = { paymentMethod = "bank" })
                    Icon(
                        Icons.Default.CreditCard,
                        contentDescription = "Ngân hàng",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Ngân hàng")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Tổng tiền: ${FormatAsVnd.format(totalPrice)}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                val userForm by userViewModel.getUserForm.collectAsState()
                val isValid = userForm.isValid()
                val formError = userForm.getErrorMessage()

                if (formError != null) {
                    Text(
                        text = "Nhập thông tin hợp lệ để tiếp tục thanh toán.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            cartViewModel.placeOrder(fullName = fullName,
                                phone = phone,
                                address = address,
                                paymentMethod = paymentMethod,
                                onSuccess = { stockUpdates ->
                                    val message =
                                        stockUpdates.joinToString("\n") { (productId, stock) ->
                                            val product = productViewModel.getProductById(productId)
                                            "${product?.name ?: productId}: còn $stock"
                                        }
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = message, duration = SnackbarDuration.Long
                                        )
                                    }
                                    showSuccessDialog = true
                                    orderViewModel.fetchOrders() // Refresh orders
                                },
                                onFailure = { e ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = e.message ?: "Đặt hàng thất bại",
                                            duration = SnackbarDuration.Long
                                        )
                                    }
                                })
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                ) {
                    Text(
                        "Xác nhận đơn hàng",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showSuccessDialog) {
                    AlertDialog(onDismissRequest = { showSuccessDialog = false },
                        title = {
                            Text(
                                "🎉 Thành công", style = MaterialTheme.typography.titleLarge
                            )
                        },
                        text = { Text("Đặt hàng thành công! Cảm ơn bạn đã mua sắm tại SportShop.") },
                        confirmButton = {
                            TextButton(onClick = {
                                showSuccessDialog = false
                                onDismiss()
                                navController.navigate("orders") {
                                    popUpTo(navController.graph.startDestinationId)
                                }
                            }) {
                                Text("OK")
                            }
                        })
                }
            }
        }
    }
}