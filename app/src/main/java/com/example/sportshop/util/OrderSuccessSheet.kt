package com.example.sportshop.ui.components.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OrderSuccessSheet(
    paymentMethod: String,
    onDismiss: () -> Unit,
    onNavigateOrders: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "🎉 Đặt hàng thành công!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Phương thức thanh toán: ${if (paymentMethod == "cash") "Tiền mặt" else "Ngân hàng"}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateOrders,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xem đơn hàng")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Đóng")
        }
    }
}