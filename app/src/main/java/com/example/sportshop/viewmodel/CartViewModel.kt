package com.example.sportshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.CartItem
import com.example.sportshop.model.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.id == item.id }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            currentItems[currentItems.indexOf(existingItem)] = updatedItem
        } else {
            currentItems.add(item)
        }

        _cartItems.value = currentItems
    }

    fun removeItem(itemId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.id == itemId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(
        address: String,
        paymentMethod: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
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
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }
    }
}
