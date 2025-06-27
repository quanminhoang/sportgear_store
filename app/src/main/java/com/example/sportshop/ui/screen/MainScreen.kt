import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

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

    // Lắng nghe sự kiện chuyển tab từ OrderDetailScreen
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val switchToCartTab =
        currentBackStackEntry?.savedStateHandle?.get<Boolean>("switch_to_cart_tab") == true
    LaunchedEffect(switchToCartTab) {
        if (switchToCartTab) {
            scope.launch {
                pagerState.scrollToPage(2) // 2 là index của CartTab
            }
            currentBackStackEntry?.savedStateHandle?.set("switch_to_cart_tab", false)
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        Column {
            when (pagerState.currentPage) {
                0 -> HomeTopBar(
                    isAdmin, navController, modifier = Modifier, userViewModel, adminViewModel
                )

                1 -> ProductTopBar(navController)
                2 -> CartTopBar()
                3 -> ProfileTopBar()
            }
        }
    }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavigationItems.forEachIndexed { index, item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(6.dp, top = 12.dp, bottom = 24.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }) {
                    Icon(
                        imageVector = if (pagerState.currentPage == index) item.selectedIcon
                        else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) // màu khi chưa chọn
                    )

                    val isSelected = pagerState.currentPage == index

                    Text(
                        text = item.title,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )

                }
            }
        }

    }) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = false
        ) { page ->

            when (page) {
                0 -> HomeTabContent(productViewModel, navController)
                1 -> ProductTabContent(productViewModel, cartViewModel, navController)
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
                        // Reload lại Activity khi đổi theme
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