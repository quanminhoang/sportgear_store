import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sportshop.R
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel
import com.google.accompanist.pager.*
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductTabContent(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Giày", "Áo", "Quần", "Dụng Cụ Thể Thao")
    val featured = true

    val pagerState = rememberPagerState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    VerticalPager(
        count = categories.size,
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val category = categories[page]
        val imageRes = when (category) {
            "Giày" -> R.drawable.image1x1
            "Áo" -> R.drawable.image1x2
            "Quần" -> R.drawable.image1x3
            "Dụng Cụ Thể Thao" -> R.drawable.image1x4
            else -> R.drawable.image1x1
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate("all_products/${category}?featured=$featured")
                }
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = category,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
