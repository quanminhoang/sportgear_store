import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.DrawerDefaults.shape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.ui.screen.ProductsGrid
import com.example.sportshop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ShopTabContent(
    productViewModel: ProductViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val tabTitles = listOf("Tất cả", "Chạy", "Áo", "Quần")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()
    val indicatorColor = MaterialTheme.colorScheme.primary
    Column(modifier = modifier.fillMaxSize()) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 4.dp),
                    color = indicatorColor,
                    height = 2.dp
                )
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(shape),
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }

        // Nội dung tab
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) { page ->
            val products by productViewModel.allProducts.collectAsState()

            val category = when (page) {
                0 -> null
                1 -> "giày"
                2 -> "áo"
                3 -> "quần"
                else -> null
            }

            val validCategory = category?.takeIf { it.isNotBlank() }

            val displayProducts = products.filter { product ->
                validCategory == null || product.category.equals(validCategory, ignoreCase = true)
            }

            ProductsGrid(
                displayProducts = displayProducts,
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                innerPadding = PaddingValues(16.dp)
            )
        }
    }
}