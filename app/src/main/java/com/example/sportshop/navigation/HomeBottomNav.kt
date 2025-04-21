package com.example.sportshop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("Trang Chủ", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavigationItem("Cửa Hàng", Icons.Filled.Store, Icons.Outlined.Store),
    BottomNavigationItem("Giỏ Hàng", Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
    BottomNavigationItem("Thông Tin", Icons.Filled.Person, Icons.Outlined.Person)
)