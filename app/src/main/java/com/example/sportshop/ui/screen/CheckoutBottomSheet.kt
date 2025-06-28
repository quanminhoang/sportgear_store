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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("cash") }
    var isCheckoutSuccess by remember { mutableStateOf(false) }
    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Local validation
    val isValid by remember(fullName, phone, address) {
        derivedStateOf {
            fullName.isNotBlank() &&
                    phone.isNotBlank() && phone.matches(Regex("^0\\d{9}$")) &&
                    address.isNotBlank()
        }
    }
    val formError by remember(fullName, phone, address) {
        derivedStateOf {
            when {
                fullName.isBlank() -> "Vui lòng nhập tên người nhận."
                phone.isBlank() -> "Vui lòng nhập số điện thoại."
                !phone.matches(Regex("^0\\d{9}$")) -> "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số."
                address.isBlank() -> "Vui lòng nhập địa chỉ."
                else -> null
            }
        }
    }

    LaunchedEffect(userAddress) {
        if (address.isBlank()) {
            address = userAddress
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            // Only dismiss if OrderSuccessSheet is not active
            if (!isCheckoutSuccess) {
                onDismiss()
            }
        },
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        if (isCheckoutSuccess) {
            OrderSuccessSheet(
                paymentMethod = paymentMethod,
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        isCheckoutSuccess = false
                        onDismiss()
                    }
                },
                onNavigateOrders = {
                    scope.launch {
                        navController.navigate("orders") {
                            popUpTo(navController.graph.startDestinationId)
                        }
                        sheetState.hide()
                        isCheckoutSuccess = false
                        onDismiss()
                    }
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Thông tin người nhận:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { userViewModel.updateUserInfo(it, phone, address) },
                        label = { Text("Người nhận") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
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

                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            userViewModel.updateUserInfo(fullName, phone, it)
                        },
                        label = { Text("Địa chỉ nhận hàng") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Phương thức thanh toán:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

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

                        Spacer(modifier = Modifier.width(12.dp))

                        RadioButton(
                            selected = paymentMethod == "Ngân hàng",
                            onClick = { paymentMethod = "Ngân hàng" }
                        )
                        Icon(
                            Icons.Default.CreditCard,
                            contentDescription = "Ngân hàng",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Ngân hàng")
                    }

                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp, top = 12.dp))

                    if (formError != null) {
                        Text(
                            text = formError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        scope.launch {
                            cartViewModel.placeOrder(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                paymentMethod = paymentMethod,
                                onSuccess = { stockUpdates ->
                                    println("Order placed successfully, stockUpdates: $stockUpdates")
                                    orderViewModel.fetchOrders()
                                    scope.launch {
                                        println("Hiding checkout sheet")
                                        sheetState.hide()
                                        println("Setting isCheckoutSuccess = true")
                                        isCheckoutSuccess = true
                                    }
                                },
                                onFailure = { e ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đặt hàng thất bại: ${e.message ?: "Lỗi không xác định"}")
                                    }
                                }
                            )
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

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}