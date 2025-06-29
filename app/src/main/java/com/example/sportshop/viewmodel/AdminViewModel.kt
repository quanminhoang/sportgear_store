package com.example.sportshop.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sportshop.model.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        db.collection("products").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                products = it.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
            }
        }
    }

    fun deleteProduct(productId: String) {
        db.collection("products").document(productId).delete()
    }

    fun saveProduct(product: Product, onResult: (Boolean, String) -> Unit) {
        if (product.id.isNullOrBlank()) {
            db.collection("products").add(product.copy(id = null))
                .addOnSuccessListener { docRef ->
                    val productWithId = product.copy(id = docRef.id)
                    db.collection("products").document(docRef.id).set(productWithId)
                        .addOnSuccessListener {
                            onResult(true, "Đã thêm sản phẩm mới")
                        }
                        .addOnFailureListener {
                            onResult(false, "Lỗi khi cập nhật sản phẩm mới tạo")
                        }
                }
                .addOnFailureListener {
                    onResult(false, "Lỗi khi thêm sản phẩm")
                }
        } else {
            db.collection("products").document(product.id!!).set(product)
                .addOnSuccessListener {
                    onResult(true, "Cập nhật sản phẩm thành công")
                }
                .addOnFailureListener {
                    onResult(false, "Lỗi khi cập nhật sản phẩm")
                }
        }
    }
}