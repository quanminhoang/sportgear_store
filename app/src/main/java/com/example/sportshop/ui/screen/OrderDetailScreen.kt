package com.example.sportshop.ui.screen

import Btn_Search
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonPinCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sportshop.viewmodel.OrderViewModel
import com.example.sportshop.ui.components.buttons.Btn_Back
import com.example.sportshop.viewmodel.CartViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel
) {
    val orderId = backStackEntry.arguments?.getString("id") ?: ""
    val orders by orderViewModel.orders.collectAsState()
    val order = orders.find { it.id == orderId }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (order == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Không tìm thấy thông tin đơn hàng. Vui lòng quay lại.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    BackHandler {
        order?.status?.let { status ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("order_status_restore", status)
        }
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Thông tin đơn hàng",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }, navigationIcon = {
                Btn_Back(navController)
            }, actions = {
                Btn_Search(navController)
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
            )
            )
        }, bottomBar = {
            if (order.status != "Đã hủy") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(top = 8.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Hủy đơn hàng")
                    }
                }
            }

            if (order.status == "Đã hủy") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(top = 8.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            // Thêm lại các sản phẩm vào giỏ hàng thay vì mua lại ngay
                            order.items.forEach { item ->
                                cartViewModel.addToCart(
                                    com.example.sportshop.model.data.CartItem(
                                        id = item.id,
                                        name = item.name,
                                        price = item.price,
                                        imageUrl = item.imageUrl,
                                        quantity = item.quantity
                                    )
                                )
                            }
                            Toast.makeText(
                                context,
                                "Đã thêm sản phẩm vào giỏ hàng!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Chuyển sang tab giỏ hàng (CartTab)
                            navController.popBackStack("home", inclusive = false)
                            navController.navigate("home")
                            // Gửi sự kiện sang MainScreen để chuyển sang tab Cart
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("switch_to_cart_tab", true)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Mua lại")
                    }
                }
            }

            if (showDialog && order != null) {
                AlertDialog(onDismissRequest = { showDialog = false },
                    title = { Text("Xác nhận hủy đơn") },
                    text = { Text("Bạn có chắc muốn hủy đơn hàng này không?") },
                    confirmButton = {
                        TextButton(onClick = {
                            orderViewModel.updateOrderStatus(orderId, "Đã hủy")
                            navController.popBackStack()
                            showDialog = false
                        }) {
                            Text("Đồng ý")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Hủy")
                        }
                    })
            }
        }
    ) { padding ->
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {

                val statusBgColor = when (order?.status) {
                    "Chờ xác nhận" -> Color.Yellow.copy(alpha = 0.2f)
                    "Đang giao" -> Color.Cyan.copy(alpha = 0.2f)
                    "Đã giao" -> Color.Green.copy(alpha = 0.2f)
                    "Đã hủy" -> Color.Red.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val statusText = when (order?.status) {
                    "Chờ xác nhận" -> "Cảm ơn bạn đã đặt hàng! Đơn hàng của bạn hiện đang chờ xác nhận."
                    "Đang giao" -> "Đơn hàng đang được giao đến bạn. Vui lòng chú ý điện thoại"
                    "Đã giao" -> "Đơn hàng đã hoàn thành"
                    "Đã hủy" -> "Đã huỷ đơn hàng"
                    null, "" -> "Trạng thái không xác định"
                    else -> "Trạng thái: ${order?.status}"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = statusBgColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Thông tin người nhận",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PersonPinCircle,
                            contentDescription = "Location Icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = order?.fullName ?: "Không xác định",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = " " + (order?.phone ?: "Không xác định"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = order?.address ?: "Không xác định",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sản phẩm trong đơn hàng",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    order?.items?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(bottom = 12.dp),
                        ) {
                            val imageUrl = item.imageUrl
                            var imageState by remember(item.id + imageUrl) {
                                mutableStateOf<coil.compose.AsyncImagePainter.State>(
                                    coil.compose.AsyncImagePainter.State.Empty
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(84.dp)
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
                                    if (imageState is coil.compose.AsyncImagePainter.State.Loading) {
                                        CircularProgressIndicator()
                                    }
                                    if (imageState is coil.compose.AsyncImagePainter.State.Error) {
                                        Image(
                                            painter = painterResource(id = com.example.sportshop.R.drawable.ic_no_picture_available),
                                            contentDescription = "No Image",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else {
                                    Image(
                                        painter = painterResource(id = com.example.sportshop.R.drawable.ic_no_picture_available),
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
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    text = "x${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "₫${
                                        NumberFormat.getNumberInstance(Locale("vi", "VN"))
                                            .format(item.price)
                                    }",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    val formattedTotal = NumberFormat.getNumberInstance(Locale("vi", "VN"))
                        .format(order?.totalPrice ?: 0.0)

                    Text(
                        text = buildAnnotatedString {
                            append("Thành tiền: ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("₫$formattedTotal")
                            }
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mã đơn hàng",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = order?.id ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Phương thức thanh toán",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = order?.paymentMethod ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Thời gian đặt hàng",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = if (order?.timestamp ?: 0 > 0) dateFormat.format(
                                Date(order?.timestamp ?: 0)
                            ) else "Không xác định",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}
