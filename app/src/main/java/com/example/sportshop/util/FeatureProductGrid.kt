
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.product.FeatureProductCard

@Composable
fun FeaturedProductsRow(
    products: List<Product>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        items(products.take(6)) { product ->
            FeatureProductCard(
                product = product,
                onClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }
    }
}