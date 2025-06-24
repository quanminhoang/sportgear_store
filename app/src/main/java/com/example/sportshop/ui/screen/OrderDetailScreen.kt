package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.sportshop.viewmodel.OrderViewModel

@Composable
fun OrderDetailScreen(
    backStackEntry: NavBackStackEntry,
    orderViewModel: OrderViewModel
) {
    val orderId = backStackEntry.arguments?.getString("id") ?: "?"
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chi tiết đơn hàng #$orderId") })
        },
        bottomBar = {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Hủy đơn hàng")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Xác nhận hủy đơn") },
                    text = { Text("Bạn có chắc muốn hủy đơn hàng này không?") },
                    confirmButton = {
                        TextButton(onClick = {
                            orderViewModel.updateOrderStatus(orderId, "Đã hủy")
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Thông tin đơn hàng đang được tải từ Firebase...")
        }
    }
}
