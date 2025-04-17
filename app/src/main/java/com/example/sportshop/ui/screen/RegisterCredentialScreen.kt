package com.example.sportshop.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sportshop.ui.theme.SportsShopTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Register Credential Screen
@Composable
fun RegisterCredentialScreen(navController: NavHostController = rememberNavController(),
    name: String,
    dob: String,
    phone: String,
    email: String,
    address: String
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tạo mật khẩu", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác nhận mật khẩu") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            if (password != confirmPassword) {
                Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return@Button
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    val data = mapOf(
                        "name" to name,
                        "dob" to dob,
                        "phone" to phone,
                        "email" to email,
                        "address" to address
                    )

                    firestore.collection("users").document(uid).set(data)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Tài khoản đã được tạo, bạn có thể bắt đầu mua sắm rồi!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("home") {
                                popUpTo("register_credential") { inclusive = true }
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Đăng ký thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Hoàn tất đăng ký")
        }
    }
}

