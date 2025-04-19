package com.example.sportshop.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sportshop.model.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

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

    suspend fun uploadImage(uri: Uri): String? {
        return try {
            val fileName = "product_images/${UUID.randomUUID()}.jpg"
            val ref = FirebaseStorage.getInstance().reference.child(fileName)
            ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
