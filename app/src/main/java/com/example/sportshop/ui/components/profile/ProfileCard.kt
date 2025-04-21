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
import androidx.compose.ui.text.font.FontWeight
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
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.width(16.dp))
            Column(
            ) {
                Text(
                    name, style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    email, style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        ProfileMenuItem(Icons.Default.Person, "Edit Profile", onProfileClick)
        ProfileMenuItem(Icons.Default.Settings, "Settings", onSettingsClick)
        ProfileMenuItem(Icons.Default.Notifications, "Notification", onNotificationToggle)
        ProfileMenuItem(Icons.Default.Logout, "Log Out", onLogout)

    }
}


