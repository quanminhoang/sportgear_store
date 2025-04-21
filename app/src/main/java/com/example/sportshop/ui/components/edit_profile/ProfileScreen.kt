package com.example.sportshop.ui.components.edit_profile

import AvatarEditor
import LogoutDialog
import SaveChangesButton
import SavingBottomSheet
import UserInfoFields
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import rememberImagePicker

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

    val launcher = rememberImagePicker(
        onImageSelected = { uri ->
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
                    Toast.makeText(context, "Upload thất bại: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                } finally {
                    showSheet = false
                }
            }
        }
    )

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    Btn_Back(navController = navController)
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SaveChangesButton(onSave = {
                    val uid = user?.uid ?: return@SaveChangesButton
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
                                popUpTo("profile") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            showSheet = false
                        }
                    }
                })

                Spacer(Modifier.height(8.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarEditor(avatarUrl, selectedImageUri, launcher)
            Spacer(Modifier.height(8.dp))
            Text(user?.email ?: "yourname@gmail.com", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))

            UserInfoFields(
                name = name,
                phone = phone,
                address = address,
                onNameChange = { name = it },
                onPhoneChange = { phone = it },
                onAddressChange = { address = it }
            )
        }

        if (showSheet) {
            SavingBottomSheet(sheetState = sheetState)
        }
    }
}
