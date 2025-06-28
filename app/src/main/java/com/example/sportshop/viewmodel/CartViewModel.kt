package com.example.sportshop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel(application: Application, private val productViewModel: ProductViewModel) :
    AndroidViewModel(application) {

    private val cartDataStore = CartDataStore(application.applicationContext)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private var currentUserId: String? = null

    fun setUser(userId: String) {
        currentUserId = userId

        viewModelScope.launch {
            _cartItems.value = cartDataStore.getCartItems(userId).first()
        }
    }

    fun addToCart(item: CartItem) {
        val userId = currentUserId ?: return
        val product = productViewModel.getProductById(item.id ?: return) ?: return
        val maxQuantity = product.quantity

        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.id == item.id }

        if (existingItem != null) {
            val updatedQuantity = (existingItem.quantity + item.quantity).coerceIn(1, maxQuantity)
            currentItems[currentItems.indexOf(existingItem)] =
                existingItem.copy(quantity = updatedQuantity)
        } else {
            val clampedQuantity = item.quantity.coerceIn(1, maxQuantity)
            currentItems.add(item.copy(quantity = clampedQuantity))
        }

        _cartItems.value = currentItems

        viewModelScope.launch {
            cartDataStore.saveCartItems(userId, currentItems)
        }
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        val userId = currentUserId ?: return
        val product = productViewModel.getProductById(itemId) ?: return // Exit if product not found
        val maxQuantity = product.quantity // Get product stock
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.id == itemId }

        if (index != -1) {
            val item = currentItems[index]
            val updatedQuantity = newQuantity.coerceIn(1, maxQuantity)

            currentItems[index] = item.copy(quantity = updatedQuantity)
            _cartItems.value = currentItems

            viewModelScope.launch {
                cartDataStore.saveCartItems(userId, currentItems)
            }
        }
    }

    fun removeItem(itemId: String) {
        val userId = currentUserId ?: return

        val updatedList = _cartItems.value.filterNot { it.id == itemId }
        _cartItems.value = updatedList

        viewModelScope.launch {
            cartDataStore.saveCartItems(userId, updatedList)
        }
    }

    fun clearCart() {
        val userId = currentUserId ?: return

        _cartItems.value = emptyList()

        viewModelScope.launch {
            cartDataStore.clearCart(userId)
        }
    }

    fun placeOrder(
        fullName: String,
        phone: String,
        address: String,
        paymentMethod: String,
        onSuccess: (List<Pair<String, Int>>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val firestore = FirebaseFirestore.getInstance()
            Log.i("CartViewModel", "Placing order for user: ${cartItems.value}")
            val order = Order(uid = userId,
                fullName = fullName,
                phone = phone,
                address = address,
                paymentMethod = paymentMethod,
                totalPrice = cartItems.value.sumOf { it.price * it.quantity },
                status = "Chờ xác nhận",
                items = cartItems.value.map {
                    OrderItem(
                        id = it.id ?: "",
                        name = it.name,
                        imageUrl = it.imageUrl,
                        price = it.price,
                        quantity = it.quantity
                    )
                })

            // Kiểm tra số lượng hàng trong kho
            val stockUpdates = mutableListOf<Pair<String, Int>>()
            for (cartItem in cartItems.value) {
                val product = productViewModel.getProductById(cartItem.id ?: continue) ?: continue
                val newStock = product.quantity - cartItem.quantity
                if (newStock < 0) {
                    onFailure(Exception("Insufficient stock for ${cartItem.name}: ${product.quantity} available"))
                    return@launch
                }
                stockUpdates.add(Pair(cartItem.id, newStock))
            }

            val updateErrors = mutableListOf<Throwable>()
            stockUpdates.forEach { (productId, newStock) ->
                firestore.collection("products").document(productId).update("quantity", newStock)
                    .addOnSuccessListener {
                        productViewModel.reloadProductById(productId)
                    }.addOnFailureListener { e ->
                        updateErrors.add(e)
                        return@addOnFailureListener // Fixed: Only exit the failure listener
                    }
            }

            // Update stock in Firestore
            stockUpdates.forEach { (productId, newStock) ->
                firestore.collection("products").document(productId).update("quantity", newStock)
                    .addOnSuccessListener {
                        productViewModel.reloadProductById(productId)
                    }.addOnFailureListener { e ->
                        onFailure(e)
                        return@addOnFailureListener // Fixed: Only exit the failure listener                    }
                    }

                // Place order
                firestore.collection("orders").add(order).addOnSuccessListener { docRef ->
                    // Set the order ID
                    docRef.update("id", docRef.id)
                    clearCart()
                    onSuccess(stockUpdates) // Return stock updates
                }.addOnFailureListener { e ->
                    onFailure(e)
                }
            }
        }
    }
}