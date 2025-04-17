package com.example.sportshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        loadFromFirestore() // Thay bằng loadFromFirestore() khi bạn kết nối Firestore
    }


    // Nếu dùng Firestore thì bạn sẽ có một hàm như sau:
    private fun loadFromFirestore() {
        viewModelScope.launch {
            Firebase.firestore.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    val fetchedProducts = result.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)
                    }
                    _products.value = fetchedProducts
                }
                .addOnFailureListener {
                    // Xử lý lỗi
                }
        }
    }
}
