import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.components.profile.ProfileTabContent
import com.example.sportshop.navigation.bottomNavigationItems
import com.example.sportshop.ui.components.cart.CartTabContent
import com.example.sportshop.ui.components.cart.CartTopBar
import com.example.sportshop.ui.components.home.HomeTabContent
import com.example.sportshop.ui.components.profile.ProfileTopBar
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel(),

    ) {


    val pagerState = rememberPagerState(pageCount = { bottomNavigationItems.size })
    val scope = rememberCoroutineScope()
    val isAdmin by userViewModel.isAdmin.collectAsState()
    val context = LocalContext.current
    val themeManager = remember { ThemeManager(context) }
    val adminViewModel: AdminViewModel = viewModel()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                when (pagerState.currentPage) {
                    0 -> HomeTopBar(
                        isAdmin, navController, modifier = Modifier, userViewModel, adminViewModel
                    )

                    1 -> ShopTopBar(
                        isAdmin, navController, modifier = Modifier, userViewModel, adminViewModel
                    )
                    3 -> CartTopBar()
                    2 -> ProfileTopBar()
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                                imageVector = if (pagerState.currentPage == index)
                                    item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent), // Ensure transparency
            userScrollEnabled = false
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    ) // Apply padding only where needed
            ) {
                when (page) {
                    0 -> HomeTabContent(userViewModel,productViewModel, navController)
                    1 -> ShopTabContent(productViewModel, navController)
                    2 -> CartTabContent(
                        navController,
                        cartViewModel,
                        userViewModel,
                        productViewModel,
                        orderViewModel
                    )

                    3 -> ProfileTabContent(
                        navController = navController,
                        themeManager = themeManager,
                        userViewModel = userViewModel,
                        reload = {
                            (context as? android.app.Activity)?.let {
                                it.finish()
                                it.startActivity(it.intent)
                            }
                        }
                    )
                }
            }
        }
    }
}