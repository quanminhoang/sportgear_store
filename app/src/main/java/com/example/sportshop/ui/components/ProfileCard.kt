package com.example.sportshop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    notificationSetting: String,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationToggle: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    Text(email, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(16.dp))
            Divider()
            ProfileMenuItem(Icons.Default.Person, "My Profile", onProfileClick)
            ProfileMenuItem(Icons.Default.Settings, "Settings", onSettingsClick)
            ProfileMenuItem(Icons.Default.Notifications, "Notification", onNotificationToggle)
            ProfileMenuItem(Icons.Default.Logout, "Log Out", onLogout)

            if (notificationSetting.isNotEmpty()) {
                Text(
                    "Notification: $notificationSetting",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
