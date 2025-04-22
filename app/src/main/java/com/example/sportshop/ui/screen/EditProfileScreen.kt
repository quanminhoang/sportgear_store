package com.example.sportshop.ui.screen

import SavingBottomSheet
import UserInfoFields
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf(user?.photoUrl?.toString() ?: "") }

    var originalName by remember { mutableStateOf("") }
    var originalPhone by remember { mutableStateOf("") }
    var originalAddress by remember { mutableStateOf("") }
    var originalAvatarUrl by remember { mutableStateOf(user?.photoUrl?.toString() ?: "") }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // Check if data has changed
    val isChanged by remember {
        derivedStateOf {
            // Use stable references for comparison
            name != originalName || phone != originalPhone || address != originalAddress || avatarUrl != originalAvatarUrl
        }
    }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { doc ->
                val fetchedName = doc.getString("name") ?: ""
                val fetchedPhone = doc.getString("phone") ?: ""
                val fetchedAddress = doc.getString("address") ?: ""
                val fetchedAvatar = doc.getString("avatar") ?: user.photoUrl?.toString() ?: ""

                name = fetchedName
                phone = fetchedPhone
                address = fetchedAddress
                avatarUrl = fetchedAvatar

                originalName = fetchedName
                originalPhone = fetchedPhone
                originalAddress = fetchedAddress
                originalAvatarUrl = fetchedAvatar
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Huỷ", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary))
                    }
                },
                actions = {
                    // Save button: disabled if no changes, enabled if there are changes
                    TextButton(
                        onClick = {
                            val uid = user?.uid ?: return@TextButton
                            val data = mapOf(
                                "name" to name,
                                "phone" to phone,
                                "address" to address,
                                "avatar" to avatarUrl
                            )
                            scope.launch {
                                showSheet = true
                                try {
                                    firestore.collection("users").document(uid).update(data).await()
                                    Toast.makeText(context, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("main_profile") { inclusive = true }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    showSheet = false
                                }
                            }
                        },
                        enabled = isChanged,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (isChanged) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Lưu", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            // Avatar image with option to update
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(Modifier.height(32.dp))

            // User info fields
            UserInfoFields(
                name = name,
                phone = phone,
                address = address,
                onNameChange = { name = it },
                onPhoneChange = { phone = it },
                onAddressChange = { address = it }
            )
        }

        // Show saving progress while data is being saved
        if (showSheet) {
            SavingBottomSheet(sheetState = sheetState)
        }
    }
}