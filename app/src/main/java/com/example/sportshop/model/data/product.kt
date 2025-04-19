package com.example.sportshop.model.data

data class Product(
    val id: String? = null,
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val category: String = "",
    val discount: Double = 0.0
)