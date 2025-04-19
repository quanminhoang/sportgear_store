package com.example.sportshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sportshop.model.data.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
}
