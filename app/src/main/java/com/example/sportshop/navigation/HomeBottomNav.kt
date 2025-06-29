package com.example.sportshop.navigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("Home", Icons.Outlined.Home, Icons.Outlined.Home),
    BottomNavigationItem("Shop", Icons.Outlined.ShoppingBasket, Icons.Outlined.ShoppingBasket),
    BottomNavigationItem("Basket", Icons.Outlined.ShoppingCart, Icons.Outlined.ShoppingCart),
    BottomNavigationItem("Profile", Icons.Outlined.Person, Icons.Outlined.Person),
)