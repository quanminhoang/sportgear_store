package com.example.sportshop.model.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.cartDataStore by preferencesDataStore(name = "cart_store")

class CartDataStore(private val context: Context) {
    private fun cartKeyForUser(userId: String) = stringPreferencesKey("cart_items_$userId")

    fun getCartItems(userId: String): Flow<List<CartItem>> {
        return context.cartDataStore.data.map { preferences ->
            val json = preferences[cartKeyForUser(userId)] ?: return@map emptyList()
            val type = object : TypeToken<List<CartItem>>() {}.type
            Gson().fromJson(json, type)
        }
    }

    suspend fun saveCartItems(userId: String, cartItems: List<CartItem>) {
        val json = Gson().toJson(cartItems)
        context.cartDataStore.edit { preferences ->
            preferences[cartKeyForUser(userId)] = json
        }
    }

    suspend fun clearCart(userId: String) {
        context.cartDataStore.edit { preferences ->
            preferences.remove(cartKeyForUser(userId))
        }
    }
}
