package com.example.sportshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts


    // Add a new StateFlow for featured products
    private val _featuredProducts = MutableStateFlow<List<Product>>(emptyList())
    val featuredProducts: StateFlow<List<Product>> = _featuredProducts

    init {
        loadFromFirestore()
    }

    private fun loadFromFirestore() {
        viewModelScope.launch {
            Firebase.firestore.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    val fetchedProducts = result.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }
                    _products.value = fetchedProducts

                    // Cập nhật danh sách tất cả sản phẩm
                    _allProducts.value = fetchedProducts

                    // Lọc sản phẩm nổi bật
                    _featuredProducts.value = fetchedProducts.filter { it.feature }
                }
                .addOnFailureListener {
                    // Xử lý lỗi
                }
        }
    }
    fun getProductById(productId: String): Product? {
        return _products.value.find { it.id == productId }
    }
}