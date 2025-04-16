package com.example.sportshop

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class GoogleAuthManager(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 1. Sửa kiểu trả về thành GoogleSignInClient
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun handleSignInResult(
        intent: Intent?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            // 2. Thêm kiểm tra null cho intent
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent)
                .getResult(ApiException::class.java)

            // 3. Thêm kiểm tra null cho idToken
            val idToken = account.idToken ?: run {
                onFailure("Không nhận được token từ Google")
                return
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let { user ->
                        createUserIfNotExists(user)
                        onSuccess()
                    } ?: run {
                        onFailure("Không nhận được thông tin người dùng")
                    }
                }
                .addOnFailureListener { e ->
                    onFailure("Đăng nhập thất bại: ${e.message ?: "Unknown error"}")
                }
        } catch (e: ApiException) {
            onFailure("Lỗi đăng nhập Google: ${e.message ?: "Unknown error"}")
        }
    }

    private fun createUserIfNotExists(user: FirebaseUser) {
        val userRef = firestore.collection("users").document(user.uid)
        userRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                userRef.set(hashMapOf(
                    "uid" to user.uid,
                    "email" to user.email,
                    "name" to "",
                    "dob" to "",
                    "phone" to "",
                    "address" to "",
                    "createdAt" to System.currentTimeMillis()
                )).addOnFailureListener { e ->
                    Toast.makeText(context, "Lỗi khi tạo người dùng: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Lỗi khi kiểm tra người dùng: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}