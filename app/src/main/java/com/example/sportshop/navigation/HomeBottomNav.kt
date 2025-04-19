package com.example.sportshop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("Trang Chủ", Icons.Default.Home, Icons.Default.Home),
    BottomNavigationItem("Sản Phẩm", Icons.Default.Store, Icons.Default.Store),
    BottomNavigationItem("Thông tin", Icons.Default.Person, Icons.Default.Person)
)