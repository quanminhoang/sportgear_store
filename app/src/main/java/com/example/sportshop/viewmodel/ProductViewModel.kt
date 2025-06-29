package com.example.sportshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
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

    private val _productsByCollection = mutableMapOf<String, MutableStateFlow<List<Product>>>()
    fun getProductsByCollection(collection: String): StateFlow<List<Product>> {
        return _productsByCollection.getOrPut(collection) { MutableStateFlow(emptyList()) }
    }
    private val _productsByCategory = mutableMapOf<String, MutableStateFlow<List<Product>>>()
    fun getProductsByCategory(category: String): StateFlow<List<Product>> {
        return _productsByCategory.getOrPut(category) { MutableStateFlow(emptyList()) }
    }

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

                    val groupedByCollection = fetchedProducts.groupBy { it.collection.orEmpty() }
                    groupedByCollection.forEach { (collection, productList) ->
                        _productsByCollection.getOrPut(collection) { MutableStateFlow(emptyList()) }.value = productList
                    }
                }
                .addOnFailureListener {
                    // Xử lý lỗi
                }
        }
    }

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenerRegistration?.remove()
        listenerRegistration = Firebase.firestore.collection("products")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val fetchedProducts = it.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }

                    _products.value = fetchedProducts
                    _allProducts.value = fetchedProducts
                    _featuredProducts.value = fetchedProducts.filter { it.feature }

                    val grouped = fetchedProducts.groupBy { it.category.orEmpty() }
                    grouped.forEach { (category, productList) ->
                        _productsByCategory.getOrPut(category) { MutableStateFlow(emptyList()) }.value = productList
                    }

                    val groupedByCollection = fetchedProducts.groupBy { it.collection.orEmpty() }
                    groupedByCollection.forEach { (collection, productList) ->
                        _productsByCollection.getOrPut(collection) { MutableStateFlow(emptyList()) }.value = productList
                    }
                }
            }
    }


    fun addProduct(product: Product) {
        viewModelScope.launch {
            Firebase.firestore.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    val productWithId = product.copy(id = documentReference.id)
                    Firebase.firestore.collection("products")
                        .document(documentReference.id)
                        .set(productWithId)
                        .addOnSuccessListener {
                            loadFromFirestore()
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

    fun getProductById(productId: String): Product? {
        return _products.value.find { it.id == productId }
    }

    fun reloadProductById(productId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            Firebase.firestore.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener { doc ->
                    val updatedProduct = doc.toObject(Product::class.java)?.copy(id = doc.id)
                    if (updatedProduct != null) {
                        val updatedList = _products.value.map {
                            if (it.id == productId) updatedProduct else it
                        }
                        _products.value = updatedList
                        _allProducts.value = updatedList

                        // Cập nhật lại các nhóm category
                        val grouped = updatedList.groupBy { it.category.orEmpty() }
                        grouped.forEach { (category, productList) ->
                            _productsByCategory.getOrPut(category) { MutableStateFlow(emptyList()) }.value = productList
                        }

                        // Cập nhật lại các nhóm collection
                        val groupedByCollection = updatedList.groupBy { it.collection.orEmpty() }
                        groupedByCollection.forEach { (collection, productList) ->
                            _productsByCollection.getOrPut(collection) { MutableStateFlow(emptyList()) }.value = productList
                        }
                    }
                    onDone()
                }
                .addOnFailureListener {
                    onDone()
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

}
