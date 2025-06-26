package com.example.sportshop.ui.screen

import Btn_Search
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.ui.components.buttons.Btn_Back
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val orderId = backStackEntry.arguments?.getString("id") ?: ""
    val orders by orderViewModel.orders.collectAsState()
    val order = orders.find { it.id == orderId }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết đơn hàng",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    Btn_Back(navController)
                },
                actions = {
                    Btn_Search(navController)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (order != null) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (order.status == "Đã hủy") {
                        Button(
                            onClick = {
                                cartViewModel.reorder(
                                    order = order,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Mua lại thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    },
                                    onFailure = {
                                        Toast.makeText(
                                            context,
                                            "Lỗi khi mua lại: ${it.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Mua lại")
                        }
                    }

                    if (order.status != "Đã hủy") {
                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Hủy đơn hàng")
                        }
                    }
                }
            }

            if (showDialog && order != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Xác nhận hủy đơn") },
                    text = { Text("Bạn có chắc muốn hủy đơn hàng này không?") },
                    confirmButton = {
                        TextButton(onClick = {
                            orderViewModel.updateOrderStatus(orderId, "Đã hủy")
                            navController.popBackStack()
                            showDialog = false
                        }) {
                            Text("Đồng ý")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Hủy")
                        }
                    }
                )
            }
        }
    ) { padding ->
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (order == null) {
                Text(
                    "Không tìm thấy thông tin đơn hàng. Vui lòng quay lại.",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text("Mã đơn hàng: ${order.id}")
                Text("Địa chỉ: ${order.address}")
                Text("Người nhận: ${order.fullName}")
                Text("Số điện thoại: ${order.phone}")
                Text("Phương thức thanh toán: ${order.paymentMethod}")
                Text("Tổng tiền: ${numberFormat.format(order.totalPrice)}")
                Text("Trạng thái: ${order.status}")
                Text(
                    "Thời gian: ${
                        if (order.timestamp > 0)
                            dateFormat.format(Date(order.timestamp))
                        else
                            "Không xác định"
                    }"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Danh sách sản phẩm:", style = MaterialTheme.typography.titleMedium)
                order.items.forEach { item ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("• ${item.name} x${item.quantity} - ${numberFormat.format(item.price)}")
                    }
                }
            }
        }
    }
}
