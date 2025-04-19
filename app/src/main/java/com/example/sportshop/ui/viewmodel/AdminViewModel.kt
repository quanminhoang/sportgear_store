package com.example.sportshop.ui.viewmodel

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

    var showDialog by mutableStateOf(false)
    var editingProduct: Product? by mutableStateOf(null)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        listenerRegistration?.remove() // tránh bị add nhiều lần
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
        val data = product.copy(id = null)
        if (product.id == null) {
            db.collection("products").add(data)
        } else {
            db.collection("products").document(product.id).set(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
