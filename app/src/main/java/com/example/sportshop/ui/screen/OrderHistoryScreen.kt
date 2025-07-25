package com.example.sportshop.ui.screen

import Btn_Search
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.viewmodel.OrderViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.sportshop.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sportshop.util.FormatAsVnd
import com.example.sportshop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController, orderViewModel: OrderViewModel, productViewModel: ProductViewModel) {
    val orders by orderViewModel.orders.collectAsState()
    var selectedStatus by rememberSaveable { mutableStateOf("Chờ xác nhận") }

    LaunchedEffect(Unit) {
        orderViewModel.fetchOrders()
    }

    val statusMap = mapOf(
        "Chờ xác nhận" to "Chờ xác nhận",
        "Đang giao" to "Đang giao",
        "Đã giao" to "Đã giao",
        "Đã hủy" to "Đã hủy"
    )

    val filteredOrders = orders
        .filter {
            selectedStatus == "Tất cả" ||
            (
                it.status.trim().lowercase() == statusMap[selectedStatus]?.trim()?.lowercase()
            )
        }
        .sortedByDescending { it.timestamp }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val restoredStatus = currentBackStackEntry?.savedStateHandle?.get<String>("order_status_restore")
    var restoredFlag by rememberSaveable { mutableStateOf(false) }
    // Sửa logic: chỉ set filter khi quay lại từ detail, không tự động set lại filter khi user đã chọn filter mới
    LaunchedEffect(restoredStatus) {
        if (
            restoredStatus != null &&
            restoredStatus != selectedStatus &&
            listOf("Chờ xác nhận", "Đang giao", "Đã giao", "Đã hủy").contains(restoredStatus) &&
            !restoredFlag
        ) {
            selectedStatus = restoredStatus
            restoredFlag = true
            currentBackStackEntry.savedStateHandle.remove<String>("order_status_restore")
        }
    }
    fun onFilterChipClick(status: String) {
        selectedStatus = status
        restoredFlag = false
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Lịch sử đơn hàng",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },

            navigationIcon = {
                Btn_Back(navController)
            },

            actions = {
                Btn_Search(navController)
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Chờ xác nhận", "Đang giao", "Đã giao", "Đã hủy").forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { onFilterChipClick(status) },
                        label = { Text(status) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurface

                        )
                    )
                }
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(filteredOrders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                // Khi vào chi tiết, truyền trạng thái hiện tại
                                navController.currentBackStackEntry?.savedStateHandle?.set("order_status_restore", selectedStatus)
                                navController.navigate("order_detail/${order.id}")
                            }, colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = order.status,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            val firstItem = order.items.firstOrNull()
                            if (firstItem != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min),
                                ) {
                                    val imageUrl = firstItem.imageUrl
                                    var imageState by remember(firstItem.id + imageUrl) {
                                        mutableStateOf<AsyncImagePainter.State>(
                                            AsyncImagePainter.State.Empty
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (!imageUrl.isNullOrBlank() && imageUrl != "null") {
                                            AsyncImage(
                                                model = imageUrl,
                                                contentDescription = "Order Image",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop,
                                                onState = { state -> imageState = state }
                                            )
                                            if (imageState is AsyncImagePainter.State.Loading) {
                                                CircularProgressIndicator()
                                            }
                                            if (imageState is AsyncImagePainter.State.Error) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_no_picture_available),
                                                    contentDescription = "No Image",
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(12.dp)),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_no_picture_available),
                                                contentDescription = "No Image",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        Text(
                                            text = firstItem.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Text(
                                            text = "x${firstItem.quantity}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "${FormatAsVnd.format(firstItem.price)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (order.items.size > 1) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = ".......",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val totalQuantity = order.items.sumOf { it.quantity }
                            val formattedTotal = NumberFormat.getNumberInstance(Locale("vi", "VN"))
                                .format(order.totalPrice)
                            Text(
                                text = buildAnnotatedString {
                                    append("Tổng số tiền ($totalQuantity sản phẩm): ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("${FormatAsVnd.format(order.totalPrice)}")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
