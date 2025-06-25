package com.example.sportshop.model.data

data class OrderFirebase(
    val id: String = "",
    val uid: String = "",
    val address: String = "",
    val paymentMethod: String = "",
    val totalPrice: Int = 0,
    val status: String = "",
    val timestamp: Long = 0L,
    val items: List<OrderItem> = emptyList()
)

data class OrderItem(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val price: Int = 0,
    val quantity: Int = 0
)
