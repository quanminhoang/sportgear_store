package com.example.sportshop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.CartDataStore
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.model.data.Order
import com.example.sportshop.model.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDataStore = CartDataStore(application.applicationContext)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private var currentUserId: String? = null

    fun setUser(userId: String) {
        currentUserId = userId

        viewModelScope.launch {
            _cartItems.value = cartDataStore.getCartItems(userId).first()
        }
    }

    fun addToCart(item: CartItem) {
        val userId = currentUserId ?: return

        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.id == item.id }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            currentItems[currentItems.indexOf(existingItem)] = updatedItem
        } else {
            currentItems.add(item)
        }

        _cartItems.value = currentItems

        viewModelScope.launch {
            cartDataStore.saveCartItems(userId, currentItems)
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
        address: String,
        paymentMethod: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val firestore = FirebaseFirestore.getInstance()
            val order = Order(
                address = address,
                paymentMethod = paymentMethod,
                totalPrice = cartItems.value.sumOf { it.price * it.quantity },
                items = cartItems.value
            )

            firestore.collection("orders")
                .add(order)
                .addOnSuccessListener {
                    clearCart()
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }
    }
}
