package com.example.sportshop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sportshop.model.data.OrderFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<OrderFirebase>>(emptyList())
    val orders: StateFlow<List<OrderFirebase>> = _orders

    fun fetchOrders() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(OrderFirebase::class.java) }
                _orders.value = list
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        db.collection("orders")
            .whereEqualTo("orderId", orderId)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    doc.reference.update("status", newStatus)
                }
            }
    }
}
