package com.example.sportshop.ui.screen

import Btn_Search
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.sportshop.model.data.Product
import com.example.sportshop.ui.components.buttons.Btn_Back
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import com.example.sportshop.ui.components.ult.ProductImagePager
import com.example.sportshop.viewmodel.ProductViewModel
import com.example.sportshop.ui.components.ult.LoadingRecipeShimmer
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.util.QuantityEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    navController: NavController,
    onAddToCart: (Product, Int) -> Unit,
    productViewModel: ProductViewModel
) {
    val firstImage = product.imageUrls.firstOrNull()
    var imageError by remember(firstImage) { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var screenLoading by remember { mutableStateOf(true) }
    var selectedQuantity by remember { mutableStateOf(1) }
    var showOverlay by remember { mutableStateOf(false) }


    LaunchedEffect(product.id, isRefreshing) {
        screenLoading = true
        kotlinx.coroutines.delay(1200)
        screenLoading = false
    }

    fun reloadProduct() {
        isRefreshing = true
        productViewModel.reloadProductById(product.id ?: "") {
            isRefreshing = false
        }
    }

    LaunchedEffect(showOverlay) {
        if (showOverlay) {
            kotlinx.coroutines.delay(1500)
            showOverlay = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            navigationIcon = { Btn_Back(navController) },
            actions = { Btn_Search(navController) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            if (screenLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LoadingRecipeShimmer(imageHeight = 450.dp)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductImagePager(imageUrls = product.imageUrls)

                        if (imageError) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = "Image Error",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 16.dp, top = 8.dp)
                            )

                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = product.collection,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Danh mục: ${product.category}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = FormatAsVnd.format(product.price),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            QuantityEditor(
                                quantity = selectedQuantity,
                                onQuantityChange = { newQty ->
                                    selectedQuantity = newQty
                                },
                                min = 1,
                                max = product.quantity,
                            )
                        }

                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Text(
                            text = "Mô tả:",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = {
                            onAddToCart(product, selectedQuantity)
                            showOverlay = true
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Thêm vào giỏ ",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                AnimatedVisibility(
                    visible = showOverlay,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Thêm vào giỏ hàng!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}

