package com.example.sportshop.model.data

data class OrderFirebase(
    val orderId: String = "",
    val date: String = "",
    val total: String = "",
    val status: String = "",
    val items: List<CartItem> = emptyList()
)
