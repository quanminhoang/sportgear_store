package com.example.sportshop.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileCard(
    name: String,
    email: String,
    photoUrl: String?,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit,
    onOrderHistoryClick: () -> Unit
) {
    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp).fillMaxWidth()
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                name, style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(4.dp))

            Text(
                email, style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        ProfileMenuItem(Icons.Default.Person, "Thông tin cá nhân", onProfileClick)
        ProfileMenuItem(Icons.Default.Settings, "Cài đặt", onSettingsClick)
        ProfileMenuItem(Icons.Default.History, "Lịch sử đơn hàng", onOrderHistoryClick)
        ProfileMenuItem(Icons.Default.Logout, "Đăng xuất", onLogout)

    }
}


