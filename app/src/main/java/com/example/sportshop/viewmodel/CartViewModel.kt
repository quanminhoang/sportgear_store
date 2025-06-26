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

class CartViewModel(application: Application) : AndroidViewModel(application) {

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
        fullName: String,
        phone: String,
        address: String,
        paymentMethod: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val firestore = FirebaseFirestore.getInstance()
            Log.i("CartViewModel", "Placing order for user: ${cartItems.value}")
            val order = Order(
                uid = userId,
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
                }
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
