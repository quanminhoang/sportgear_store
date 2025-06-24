package com.example.sportshop.model.data

data class CartItem(
    val id: String? = null,
    val name: String = "",
    val imageUrl: String = "",  // ✅ Hình ảnh phải có URL đúng
    val price: Double = 0.0,
    val quantity: Int = 1
)