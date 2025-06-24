package com.example.sportshop.components.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportshop.R
import com.example.sportshop.ui.components.search.ProductListWrapper
import com.example.sportshop.viewmodel.CartViewModel
import com.example.sportshop.viewmodel.ProductViewModel

@Composable
fun ProductTabContent(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val allProducts = productViewModel.allProducts.collectAsState(initial = emptyList()).value
    var selectedTab by remember { mutableStateOf(0) }

    val categories = listOf("Giày", "Áo", "Quần", "Dụng Cụ Thể Thao")
    val featured = true  // Hoặc có thể là một giá trị dynamic tùy theo điều kiện

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        categories.forEach { category ->
            val imageResource = when (category) {
                "Giày" -> R.drawable.image1x1
                "Áo" -> R.drawable.image1x2
                "Quần" -> R.drawable.image1x3
                "Dụng Cụ Thể Thao" -> R.drawable.image1x4
                else -> R.drawable.image1x1
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        navController.navigate("all_products/${category}?featured=${featured}")
                    }
            ) {
                Image(
                    painter = painterResource(imageResource),
                    contentDescription = category,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Text(
                    text = category,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        shadow = androidx.compose.ui.text.TextStyle.Default.shadow?.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier
                        .align(Alignment.Center) // Căn giữa văn bản
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(8.dp))

        }
    }
}
