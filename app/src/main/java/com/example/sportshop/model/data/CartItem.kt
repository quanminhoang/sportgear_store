package com.example.sportshop.model.data

data class CartItem(
    val id: String? = null,
    val name: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val stock: Int = 0,
)