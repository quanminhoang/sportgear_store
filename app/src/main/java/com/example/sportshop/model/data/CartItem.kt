package com.example.sportshop.model.data

data class CartItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    var quantity: Int
)