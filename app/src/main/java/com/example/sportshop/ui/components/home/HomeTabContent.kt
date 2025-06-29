package com.example.sportshop.ui.components.home

import CollectionImageCard
import FeaturedProductsRow
import UserViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.R
import com.example.sportshop.viewmodel.ProductViewModel


@Composable
fun HomeTabContent(
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val featuredProducts by productViewModel.featuredProducts.collectAsState()
    val categories = listOf("Giày", "Áo", "Quần", "Dụng Cụ Thể Thao")
    val featured = true
    val lastName by userViewModel.lastName.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Text(
                text = "XIN CHÀO, ${lastName.uppercase()}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
        }


        item {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dành riêng cho bạn",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = {
                    navController.navigate("all_products?featured=true")
                }) {
                    Text(
                        text = "Xem tất cả",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        item {
            FeaturedProductsRow(
                products = featuredProducts, navController = navController
            )

            Spacer(Modifier.padding(16.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Text(
                    text = "Bộ sưu tập đặc biệt",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Adding a subtle divider for better visual separation
                Divider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f), // Lighter divider color
                    thickness = 1.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (!expanded) Modifier.height(900.dp) else Modifier)
                ) {
                    Column {
                        val collectionImages = mapOf(
                            "Blazer" to R.drawable.banner_pegasus,
                            "Jordan" to R.drawable.banner_jordan,
                            "Air Max" to R.drawable.banner_airmax,
                            "Vomero" to R.drawable.banner_vomero
                        )

                        val collections = listOf("Blazer", "Jordan", "Air Max", "Vomero")

                        collections.forEach { collection ->
                            CollectionImageCard(
                                imageResId = collectionImages[collection] ?: R.drawable.ic_no_picture_available,
                                collection = collection,
                                navController = navController,
                                featured = featured
                            )
                        }
                    }

                    if (!expanded) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .height(120.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f), // Softer transition
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                ), contentAlignment = Alignment.BottomCenter
                        ) {
                            Button(
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .padding(horizontal = 120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "XEM THÊM",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                if (expanded) {
                    OutlinedButton(
                        onClick = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 120.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "THU GỌN",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Bạn có thể thay đổi chiều cao tùy ý
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Cảm ơn đã lựa chọn chúng tôi",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
