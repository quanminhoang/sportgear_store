package com.example.sportshop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sportshop.model.data.Order
import com.example.sportshop.model.data.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun fetchOrders() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("orders")
            .whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { doc ->
                    val data = doc.data
                    Order(
                        id = data["id"] as? String ?: doc.id,
                        uid = data["uid"] as? String ?: "",
                        fullName = data["fullName"] as? String ?: "",
                        phone = data["phone"] as? String ?: "",
                        address = data["address"] as? String ?: "",
                        paymentMethod = data["paymentMethod"] as? String ?: "",
                        totalPrice = when (val tp = data["totalPrice"]) {
                            is Long -> tp.toDouble()
                            is Int -> tp.toDouble()
                            is Double -> tp
                            else -> 0.0
                        },
                        status = data["status"] as? String ?: "",
                        timestamp = data["timestamp"] as? Long ?: 0L,
                        items = (data["items"] as? List<Map<String, Any>>)?.map { item ->
                            OrderItem(
                                id = item["id"] as? String ?: "",
                                name = item["name"] as? String ?: "",
                                imageUrl = item["imageUrl"] as? String ?: "",
                                price = when (val p = item["price"]) {
                                    is Long -> p.toDouble()
                                    is Int -> p.toDouble()
                                    is Double -> p
                                    else -> 0.0
                                },
                                quantity = when (val q = item["quantity"]) {
                                    is Long -> q.toInt()
                                    is Int -> q
                                    is Double -> q.toInt()
                                    else -> 0
                                }
                            )
                        } ?: emptyList()
                    )
                }
                _orders.value = list
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        db.collection("orders")
            .whereEqualTo("id", orderId)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    doc.reference.update("status", newStatus)
                }
            }
    }
}
