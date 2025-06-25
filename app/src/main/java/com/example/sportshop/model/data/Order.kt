package com.example.sportshop.model.data

import android.provider.ContactsContract.CommonDataKinds.Phone

data class Order(
    val id: String = "",
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