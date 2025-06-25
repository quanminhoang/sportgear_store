package com.example.sportshop.ui.screen

import Btn_Search
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController, orderViewModel: OrderViewModel) {
    val orders by orderViewModel.orders.collectAsState()
    var selectedStatus by remember { mutableStateOf("Tất cả") }

    LaunchedEffect(Unit) {
        orderViewModel.fetchOrders()
    }

    val filteredOrders = orders.filter {
        selectedStatus == "Tất cả" || it.status == selectedStatus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lịch sử đơn hàng",
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
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Tất cả", "Đã giao", "Đang xử lý", "Chờ xác nhận", "Đã hủy").forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(status) }
                    )
                }
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(filteredOrders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("order_detail/${order.id}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Đơn hàng #${order.id}", style = MaterialTheme.typography.titleMedium)
                            Text("Địa chỉ: ${order.address}")
                            Text("Phương thức: ${order.paymentMethod}")
                            Text("Tổng tiền: ${order.totalPrice}")
                            Text("Trạng thái: ${order.status}")
                            Text("Thời gian: ${order.timestamp}")
                        }
                    }
                }
            }
        }
    }
}
