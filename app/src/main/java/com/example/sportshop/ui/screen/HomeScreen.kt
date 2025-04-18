package com.example.sportshop.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.MainProfileMenu
import com.example.sportshop.ThemeManager
import com.example.sportshop.ui.components.ProductListWrapper
import com.example.sportshop.ui.components.SpecialOfferCard
import com.example.sportshop.ui.components.TopCategoriesSection
import com.example.sportshop.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.sportshop.ui.viewmodel.AdminViewModel

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("Home", Icons.Default.Home, Icons.Default.Home),
    BottomNavigationItem("Favorite", Icons.Default.Favorite, Icons.Default.Favorite),
    BottomNavigationItem("Profile", Icons.Default.Person, Icons.Default.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val pagerState = rememberPagerState(pageCount = { bottomNavigationItems.size })
    val scope = rememberCoroutineScope()
    val isAdmin by userViewModel.isAdmin.collectAsState()

    val context = LocalContext.current
    val themeManager = remember { ThemeManager(context) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sports Shop") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { navController.navigate("cart_screen") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (pagerState.currentPage == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> HomeTabContent(isAdmin, navController)
                1 -> FavoriteTabContent()
                2 -> MainProfileMenu(navController = navController, themeManager = themeManager) // Truyền themeManager vào đây
            }
        }
    }
}

@Composable
fun HomeTabContent(
    isAdmin: Boolean,
    navController: NavController,
    adminViewModel: AdminViewModel = viewModel()
) {
    val isRefreshing by adminViewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { adminViewModel.fetchProducts() }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SpecialOfferCard()
            Spacer(modifier = Modifier.height(24.dp))
            TopCategoriesSection()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sản Phẩm Nổi Bật",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProductListWrapper()
            Spacer(modifier = Modifier.height(32.dp))

            if (isAdmin) {
                Button(
                    onClick = { navController.navigate("admin") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vào trang Admin")
                }
            }
        }
    }
}

@Composable
fun FavoriteTabContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Danh sách yêu thích")
    }
}



