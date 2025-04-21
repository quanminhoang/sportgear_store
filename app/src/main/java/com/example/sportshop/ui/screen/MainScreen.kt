import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
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
import com.example.sportshop.components.products.ProductTabContent
import com.example.sportshop.components.profile.ProfileTabContent
import com.example.sportshop.navigation.bottomNavigationItems
import com.example.sportshop.ui.components.cart.CartTabContent
import com.example.sportshop.ui.components.cart.CartTopBar
import com.example.sportshop.ui.components.home.HomeTabContent
import com.example.sportshop.ui.components.product.ProductTopBar
import com.example.sportshop.ui.components.profile.ProfileTopBar
import com.example.sportshop.ui.theme.ThemeManager
import com.example.sportshop.viewmodel.AdminViewModel
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = { bottomNavigationItems.size })
    val scope = rememberCoroutineScope()
    val isAdmin by userViewModel.isAdmin.collectAsState()
    val context = LocalContext.current
    val themeManager = remember { ThemeManager(context) }
    val adminViewModel: AdminViewModel = viewModel()
    val featuredProducts by productViewModel.featuredProducts.collectAsState()


    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Column {
                when (pagerState.currentPage) {
                    0 -> HomeTopBar(isAdmin, navController, modifier = Modifier, userViewModel, adminViewModel)
                    1 -> ProductTopBar(navController)
                    2 -> CartTopBar()
                    3 -> ProfileTopBar()
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.dp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
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
                                imageVector = if (pagerState.currentPage == index) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title, style = MaterialTheme.typography.bodySmall)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = true
        ) { page ->

                when (page) {
                    0 -> HomeTabContent(productViewModel, cartViewModel, navController)
                    1 -> ProductTabContent(cartViewModel)
                    2 -> CartTabContent(cartViewModel)
                    3 -> ProfileTabContent(navController, themeManager)
                }
            }
        }
    }