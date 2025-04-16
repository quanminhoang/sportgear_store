package com.example.sportshop.ui.theme.project

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

// Main Profile Menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileMenu(navController: NavController, themeManager: ThemeManager) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "yourname@gmail.com"
    val name = user?.displayName ?: "Your name"
    val photoUrl = user?.photoUrl?.toString()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    val currentLanguage = sharedPreferences.getString("language", "Eng") ?: "Eng"

    var notificationSetting by remember { mutableStateOf("Allow") }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var theme by remember { mutableStateOf(themeManager.currentTheme) }
    var language by remember { mutableStateOf(currentLanguage) }

    val onSaveSettings: () -> Unit = {
        themeManager.setTheme(theme)
        sharedPreferences.edit().apply {
            putString("language", language)
            apply()
        }
        // Chỉ recreate khi thay đổi ngôn ngữ
        if (language != currentLanguage) {
            val locale = if (language == "Eng") Locale("en") else Locale("vi")
            val config = context.resources.configuration
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context.getActivity()?.recreate()
        }
    }

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
            ProfileMenuItem(Icons.Default.Person, "My Profile") {
                navController.navigate("profile")
            }
            ProfileMenuItem(Icons.Default.Settings, "Settings") {
                showSettingsSheet = true
            }
            ProfileMenuItem(Icons.Default.Notifications, "Notification") {
                notificationSetting = if (notificationSetting == "Allow") "Mute" else "Allow"
            }
            ProfileMenuItem(Icons.Default.Logout, "Log Out") {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("welcome") {
                    popUpTo("home") { inclusive = true }
                }
            }
            if (notificationSetting.isNotEmpty()) {
                Text(
                    "Notification: $notificationSetting",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

    if (showSettingsSheet) {
        ModalBottomSheet(onDismissRequest = { showSettingsSheet = false }) {
            Column(Modifier.padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Theme: $theme", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Text(
                    "Language: ${if (language == "Eng") "English" else "Vietnamese"}",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = theme == "Light",
                        onClick = { theme = "Light" }
                    )
                    Text("Light", modifier = Modifier.clickable { theme = "Light" })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = theme == "Dark",
                        onClick = { theme = "Dark" }
                    )
                    Text("Dark", modifier = Modifier.clickable { theme = "Dark" })
                }
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = language == "Eng",
                        onClick = { language = "Eng" }
                    )
                    Text("English", modifier = Modifier.clickable { language = "Eng" })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = language == "Viet",
                        onClick = { language = "Viet" }
                    )
                    Text("Vietnamese", modifier = Modifier.clickable { language = "Viet" })
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        onSaveSettings()
                        showSettingsSheet = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}

// Profile Menu Item
@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
    }
}

// Profile Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf(user?.photoUrl?.toString() ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            val ref = storage.reference.child("avatars/${user?.uid}.jpg")
            scope.launch {
                showSheet = true
                try {
                    ref.putFile(uri).await()
                    val downloadUri = ref.downloadUrl.await()
                    avatarUrl = downloadUri.toString()
                    firestore.collection("users").document(user!!.uid)
                        .update("avatar", avatarUrl)
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    showSheet = false
                }
            }
        }
    }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { doc ->
                name = doc.getString("name") ?: ""
                phone = doc.getString("phone") ?: ""
                address = doc.getString("address") ?: ""
                avatarUrl = doc.getString("avatar") ?: user.photoUrl?.toString() ?: ""
            }
        }
    }

    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = selectedImageUri ?: avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .offset(x = (-4).dp, y = (-4).dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.Gray, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Avatar",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(user?.email ?: "yourname@gmail.com", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        val uid = user?.uid ?: return@Button
                        val data = mapOf("name" to name, "phone" to phone, "address" to address, "avatar" to avatarUrl)
                        scope.launch {
                            showSheet = true
                            try {
                                firestore.collection("users").document(uid).update(data).await()
                                Toast.makeText(context, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                showSheet = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Change")
                }
                Spacer(Modifier.height(16.dp))
                TextButton(
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đăng xuất", color = Color.Red)
                }
            }

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Đang lưu thay đổi...")
                    }
                }
            }

            if (showSignOutDialog) {
                AlertDialog(
                    onDismissRequest = { showSignOutDialog = false },
                    title = { Text("Xác nhận") },
                    text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
                    confirmButton = {
                        TextButton(onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("welcome") {
                                popUpTo("home") { inclusive = true }
                            }
                            showSignOutDialog = false
                        }) {
                            Text("Đăng xuất")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSignOutDialog = false }) {
                            Text("Hủy")
                        }
                    }
                )
            }
        }
    }
}