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
        listenerRegistration?.remove()
        listenerRegistration = db.collection("products").addSnapshotListener { snapshot, _ ->
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

    fun saveProduct(product: Product) {
        val data = product.copy(id = null) // Nếu id là null, thêm mới
        if (product.id == null) {
            db.collection("products").add(data)
                .addOnSuccessListener { docRef ->
                    // Sau khi thêm mới, cập nhật lại document với id vừa tạo
                    val productWithId = product.copy(id = docRef.id)
                    db.collection("products").document(docRef.id).set(productWithId)
                }
        } else {
            db.collection("products").document(product.id).set(product)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
