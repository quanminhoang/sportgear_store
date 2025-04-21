package com.example.sportshop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavigationItem("Shop", Icons.Filled.Store, Icons.Outlined.Storefront),
    BottomNavigationItem("Basket", Icons.Filled.ShoppingBasket, Icons.Outlined.ShoppingBasket),
    BottomNavigationItem("Profile", Icons.Filled.Person, Icons.Outlined.Person),

)