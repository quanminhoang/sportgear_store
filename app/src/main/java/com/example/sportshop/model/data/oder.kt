package com.example.sportshop.model.data

data class Order(
    val id: String? = null,
    val address: String = "",
    val paymentMethod: String = "",
    val totalPrice: Double = 0.0,
    val items: List<CartItem> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)