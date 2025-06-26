package com.example.sportshop.model.data

import android.provider.ContactsContract.CommonDataKinds.Phone

data class Order(
    val id: String = generateOrderId(),
    val uid: String = "",
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val paymentMethod: String = "",
    val totalPrice: Double = 0.0,
    val status: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val items: List<OrderItem> = emptyList()
)

fun generateOrderId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..10)
        .map { chars.random() }
        .joinToString("")
}
