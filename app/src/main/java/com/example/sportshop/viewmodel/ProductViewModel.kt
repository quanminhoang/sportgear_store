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
                    _allProducts.value = fetchedProducts
                    _featuredProducts.value = fetchedProducts.filter { it.feature }

                    val grouped = fetchedProducts.groupBy { it.category.orEmpty() }
                    grouped.forEach { (category, productList) ->
                        _productsByCategory.getOrPut(category) { MutableStateFlow(emptyList()) }.value = productList
                    }
                }
                .addOnFailureListener {
                    // Xử lý lỗi
                }
        }
    }


    // Phương thức để thêm sản phẩm vào Firestore
    fun addProduct(product: Product) {
        viewModelScope.launch {
            // Add the product without the ID first
            Firebase.firestore.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    // Get the auto-generated ID from Firestore
                    val productWithId = product.copy(id = documentReference.id)

                    // Now update the product in Firestore with the new ID
                    Firebase.firestore.collection("products")
                        .document(documentReference.id)
                        .set(productWithId)
                        .addOnSuccessListener {
                            // Successfully updated product with the ID
                            loadFromFirestore()  // Reload the product list
                        }
                        .addOnFailureListener { e ->
                            // Handle failure to set the product ID
                        }
                }
                .addOnFailureListener { e ->
                    // Handle failure to add product
                }
        }
    }

    private val _productsByCategory = mutableMapOf<String, MutableStateFlow<List<Product>>>()
    fun getProductsByCategory(category: String): StateFlow<List<Product>> {
        return _productsByCategory.getOrPut(category) { MutableStateFlow(emptyList()) }
    }


    fun getProductById(productId: String): Product? {
        return _products.value.find { it.id == productId }
    }
}
