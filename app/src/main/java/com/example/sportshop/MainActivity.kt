package com.example.sportshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
// all import profile (firebase,...)
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.FirebaseException
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.navigation.navArgument
import com.google.android.gms.common.api.ApiException
import com.google.firebase.storage.FirebaseStorage
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.util.Log
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

class ThemeManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    private val themeState = mutableStateOf(sharedPreferences.getString("theme", "Light") ?: "Light")

    val currentTheme: String
        get() = themeState.value

    fun setTheme(theme: String) {
        themeState.value = theme
        sharedPreferences.edit().putString("theme", theme).apply()
    }
}

@Composable
fun rememberThemeManager(): ThemeManager {
    val context = LocalContext.current
    return remember { ThemeManager(context) }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cập nhật ngôn ngữ khi khởi động ứng dụng
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "Eng") ?: "Eng"
        val locale = if (language == "Eng") Locale("en") else Locale("vi")
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContent {
            val themeManager = rememberThemeManager()
            // Áp dụng theme cho toàn bộ ứng dụng
            SportsShopTheme(themeManager.currentTheme) {
                MyApp(themeManager)
            }
        }
    }
}

@Composable
fun SportsShopTheme(theme: String, content: @Composable () -> Unit) {
    val colorScheme = if (theme == "Light") lightColorScheme() else darkColorScheme()
    MaterialTheme(colorScheme = colorScheme, content = content)
}

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(themeManager: ThemeManager) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }

        composable("main_profile") { MainProfileMenu(navController, themeManager) }
        composable("profile") { ProfileScreen(navController) }
        composable("login_google") { GoogleLoginScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register_info") { RegisterInfoScreen(navController) }
        composable(
            "register_credential?name={name}&dob={dob}&phone={phone}&email={email}&address={address}",
            arguments = listOf(
                navArgument("name") { defaultValue = "" },
                navArgument("dob") { defaultValue = " " },
                navArgument("phone") { defaultValue = "" },
                navArgument("email") { defaultValue = "" },
                navArgument("address") { defaultValue = "" }
            )
        ) { backStackEntry ->
            RegisterCredentialScreen(
                navController,
                name = backStackEntry.arguments?.getString("name") ?: "",
                dob = backStackEntry.arguments?.getString("dob") ?: "",
                phone = backStackEntry.arguments?.getString("phone") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                address = backStackEntry.arguments?.getString("address") ?: ""
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(2000)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("welcome") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_shop),
                contentDescription = "Sports Shop Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sports Shop",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sports Shop") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { /* Search action */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Cart action */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Trang Chủ") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Wishlist") },
                    label = { Text("Wishlist") },
                    selected = false,
                    onClick = {
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Thông tin") },
                    selected = true,
                    onClick = {
                        navController.navigate("main_profile")
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Special Offer Section
            SpecialOfferCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Categories Section
            TopCategoriesSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Trending Products
            Text(
                text = "Sản Phẩm Nổi Bật",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProductList()
        }
    }
}

@Composable
fun SpecialOfferCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.nike_shoes),
                contentDescription = "Special Offer",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Giày Chạy Bộ",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Text(
                    text = "Thiết kế giúp người mang có cảm giác chạy êm ái nhất",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* View details */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Thông Tin Sản Phẩm")
                }
            }
        }
    }
}

@Composable
fun TopCategoriesSection() {
    val categories = listOf(
        "Giày Thể Thao" to R.drawable.nike_shoes,
        "Áo Thể Thao" to R.drawable.adidas_shirt,
        "Quần Thể Thao" to R.drawable.jogger,
        "Quả Bóng Đá" to R.drawable.puma_ball
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Danh Mục Sản Phẩm",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { (name, icon) ->
                CategoryItem(name, icon)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Nổi Bật",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Xem Tất Cả",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CategoryItem(name: String, iconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(60.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary)

            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductList() {
    val products = listOf(
        Product(
            "Giày Thể Thao",
            "5,233,470 VND",
            "5 WID (%)",
            R.drawable.nike_shoes
        ),
        Product(
            "Áo thể thao",
            "1,019,217 VND",
            "56 WID (23%)",
            R.drawable.adidas_shirt
        ),
        Product(
            "Quả Bóng Đá",
            "1,019,217 VND",
            "56 WID (23%)",
            R.drawable.puma_ball
        ),
        Product(
            "Quần Jogger",
            "500,000 VND",
            "56 WID (23%)",
            R.drawable.jogger
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        products.forEach { product ->
            ProductCard(product)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = product.discount,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Red
                    )
                )
            }

            IconButton(
                onClick = { /* Add to wishlist */ },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = "Add to wishlist"
                )
            }
        }
    }
}

data class Product(
    val name: String,
    val price: String,
    val discount: String,
    val imageRes: Int
)

@Composable
fun SportsShopTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF03DAC6),
            tertiary = Color(0xFFFFFAFA)
        ),
        content = content
    )
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chào mừng đến Sports Shop!",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("login_google")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text("Đăng nhập bằng Google")
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Thêm nút Đăng nhập bằng Email
        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text("Đăng nhập bằng Email")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("register_info")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text("Đăng ký bằng Email")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileMenu(navController: NavController, themeManager: ThemeManager) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "yourname@gmail.com"
    val name = user?.displayName ?: "Your name"
    val photoUrl = user?.photoUrl?.toString()

    // Lấy SharedPreferences để lưu cài đặt
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)

    // Đọc language đã lưu
    val currentLanguage = sharedPreferences.getString("language", "Eng") ?: "Eng"

    var notificationSetting by remember { mutableStateOf("Allow") }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var theme by remember { mutableStateOf(themeManager.currentTheme) }
    var language by remember { mutableStateOf(currentLanguage) }

    // Hàm lưu cài đặt
    val onSaveSettings: () -> Unit = {
        // Lưu theme qua ThemeManager
        themeManager.setTheme(theme)
        // Lưu language
        sharedPreferences.edit().apply {
            putString("language", language)
            apply()
        }
        // Thay đổi ngôn ngữ và làm mới hoạt động
        val locale = if (language == "Eng") Locale("en") else Locale("vi")
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        // Làm mới activity để áp dụng ngôn ngữ
        context.getActivity()?.recreate()
    }

    // Giao diện chính (Card hiển thị thông tin người dùng)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    Text(email, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(16.dp))
            Divider()

            ProfileMenuItem(Icons.Default.Person, "My Profile") {
                navController.navigate("profile")
            }

            ProfileMenuItem(Icons.Default.Settings, "Settings") {
                showSettingsSheet = true
            }

            ProfileMenuItem(Icons.Default.Notifications, "Notification") {
                notificationSetting = if (notificationSetting == "Allow") "Mute" else "Allow"
            }

            ProfileMenuItem(Icons.Default.Logout, "Log Out") {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }

            if (notificationSetting.isNotEmpty()) {
                Text(
                    "Notification: $notificationSetting",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

    // Modal Settings để người dùng chọn theme và ngôn ngữ
    if (showSettingsSheet) {
        ModalBottomSheet(onDismissRequest = { showSettingsSheet = false }) {
            Column(Modifier.padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Hiển thị theme hiện tại
                Text("Theme: $theme", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                // Hiển thị ngôn ngữ hiện tại
                Text(
                    "Language: ${if (language == "Eng") "English" else "Vietnamese"}",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Chọn theme
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = theme == "Light",
                        onClick = {
                            theme = "Light"
                            onSaveSettings() // Lưu và áp dụng ngay
                        }
                    )
                    Text("Light", modifier = Modifier.clickable {
                        theme = "Light"
                        onSaveSettings()
                    })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = theme == "Dark",
                        onClick = {
                            theme = "Dark"
                            onSaveSettings() // Lưu và áp dụng ngay
                        }
                    )
                    Text("Dark", modifier = Modifier.clickable {
                        theme = "Dark"
                        onSaveSettings()
                    })
                }

                Spacer(Modifier.height(16.dp))

                // Chọn ngôn ngữ
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = language == "Eng",
                        onClick = {
                            language = "Eng"
                            onSaveSettings() // Lưu và áp dụng ngay
                        }
                    )
                    Text("English", modifier = Modifier.clickable {
                        language = "Eng"
                        onSaveSettings()
                    })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = language == "Viet",
                        onClick = {
                            language = "Viet"
                            onSaveSettings() // Lưu và áp dụng ngay
                        }
                    )
                    Text("Vietnamese", modifier = Modifier.clickable {
                        language = "Viet"
                        onSaveSettings()
                    })
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf(user?.photoUrl?.toString() ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Bottom Sheet state
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // Dialog confirm sign out
    var showSignOutDialog by remember { mutableStateOf(false) }

    // Image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            val ref = storage.reference.child("avatars/${user?.uid}.jpg")
            scope.launch {
                showSheet = true
                try {
                    ref.putFile(uri).await()
                    val downloadUri = ref.downloadUrl.await()
                    avatarUrl = downloadUri.toString()

                    firestore.collection("users").document(user!!.uid)
                        .update("avatar", avatarUrl)
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    showSheet = false
                }
            }
        }
    }

    // Load Firestore data
    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { doc ->
                name = doc.getString("name") ?: ""
                phone = doc.getString("phone") ?: ""
                address = doc.getString("address") ?: ""
                avatarUrl = doc.getString("avatar") ?: user.photoUrl?.toString() ?: ""
            }
        }
    }

    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = selectedImageUri ?: avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .offset(x = (-4).dp, y = (-4).dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.Gray, CircleShape)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Avatar", tint = Color.Black, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(user?.email ?: "yourname@gmail.com", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(24.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Mobile number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        val uid = user?.uid ?: return@Button
                        val data = mapOf("name" to name, "phone" to phone, "address" to address, "avatar" to avatarUrl)

                        scope.launch {
                            showSheet = true
                            try {
                                firestore.collection("users").document(uid).update(data).await()
                                Toast.makeText(context, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                showSheet = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Change")
                }

                Spacer(Modifier.height(16.dp))

                TextButton(
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đăng xuất", color = Color.Red)
                }
            }

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Đang lưu thay đổi...")
                    }
                }
            }

            if (showSignOutDialog) {
                AlertDialog(
                    onDismissRequest = { showSignOutDialog = false },
                    title = { Text("Xác nhận") },
                    text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
                    confirmButton = {
                        TextButton(onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                            showSignOutDialog = false
                        }) {
                            Text("Đăng xuất")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSignOutDialog = false }) {
                            Text("Hủy")
                        }
                    }
                )
            }
        }
    }
}




@Composable
fun RegisterInfoScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thông tin cá nhân",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Họ tên") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("Ngày sinh") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Địa chỉ nhận hàng") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(
                    "register_credential?name=${name}&dob=${dob}&phone=${phone}&email=${email}&address=${address}"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Tiếp tục")
        }
    }
}

@Composable
fun RegisterCredentialScreen(
    navController: NavController,
    name: String,
    dob: String,
    phone: String,
    email: String,
    address: String
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tạo mật khẩu",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Thêm trường Tên đăng nhập
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tên đăng nhập") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Trường Mật khẩu với biểu tượng con mắt
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Trường Xác nhận mật khẩu với biểu tượng con mắt
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác nhận mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isLoading) return@Button
                if (username.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password != confirmPassword) {
                    Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password.length < 6) {
                    Toast.makeText(context, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        Log.d("Register", "User created: ${result.user?.uid}")
                        val uid = result.user?.uid ?: run {
                            isLoading = false
                            Toast.makeText(context, "Lỗi: Không lấy được UID", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        val data = mapOf(
                            "name" to name,
                            "dob" to dob,
                            "phone" to phone,
                            "email" to email,
                            "address" to address,
                            "username" to username // Lưu tên đăng nhập vào Firestore
                        )
                        firestore.collection("users").document(uid).set(data)
                            .addOnSuccessListener {
                                Log.d("Register", "User data saved, navigating to home")
                                Toast.makeText(
                                    context,
                                    "Tài khoản đã được tạo, bạn có thể bắt đầu mua sắm rồi!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home") {
                                    popUpTo("register_credential") { inclusive = true }
                                    Log.d("Navigation", "Navigating to home from RegisterCredentialScreen")
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                Log.e("Register", "Failed to save user data: ${e.message}")
                                Toast.makeText(context, "Lỗi lưu dữ liệu: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        Log.e("Register", "Failed to create user: ${e.message}")
                        Toast.makeText(context, "Đăng ký thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isLoading) "Đang xử lý..." else "Hoàn tất đăng ký")
        }
    }
}

@Composable
fun GoogleLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isLoading = true
        Log.d("GoogleLogin", "Received result from Google Sign-In")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleLogin", "Google Sign-In successful, account: ${account.email}")
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    Log.d("GoogleLogin", "Sign-in successful: ${result.user?.uid}")
                    val user = result.user ?: run {
                        isLoading = false
                        Toast.makeText(context, "Lỗi: Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val uid = user.uid
                    val userRef = firestore.collection("users").document(uid)

                    // Điều hướng ngay sau khi đăng nhập thành công
                    Log.d("GoogleLogin", "Navigating to home")
                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("login_google") { inclusive = true }
                        Log.d("Navigation", "Navigating to home from GoogleLoginScreen")
                    }

                    // Kiểm tra và ghi dữ liệu Firestore
                    userRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            Log.d("GoogleLogin", "Creating new user data")
                            val newUser = mapOf(
                                "email" to user.email,
                                "name" to (user.displayName ?: ""),
                                "dob" to "",
                                "phone" to "",
                                "address" to ""
                            )
                            userRef.set(newUser)
                                .addOnSuccessListener {
                                    Log.d("GoogleLogin", "User data saved")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("GoogleLogin", "Failed to save user data: ${e.message}")
                                    Toast.makeText(context, "Không thể lưu dữ liệu do mất kết nối mạng", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }.addOnFailureListener { e ->
                        Log.e("GoogleLogin", "Failed to check user data: ${e.message}")
                        Toast.makeText(context, "Không thể kiểm tra dữ liệu do mất kết nối mạng", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    Log.e("GoogleLogin", "Sign-in failed: ${e.message}")
                    Toast.makeText(context, "Đăng nhập thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: ApiException) {
            isLoading = false
            Log.e("GoogleLogin", "Google Sign-In error: ${e.statusCode}, ${e.message}")
            Toast.makeText(context, "Lỗi đăng nhập Google: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val signInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (isLoading) return@Button
                isLoading = true
                Log.d("GoogleLogin", "Starting Google Sign-In")
                signInClient.signOut().addOnCompleteListener {
                    val signInIntent = signInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isLoading) "Đang xử lý..." else "Đăng nhập bằng Google")
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var loginInput by remember { mutableStateOf("") } // Tên đăng nhập hoặc email
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Đăng nhập",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = loginInput,
            onValueChange = { loginInput = it },
            label = { Text("Tên đăng nhập hoặc Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isLoading) return@Button
                if (loginInput.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                // Kiểm tra xem loginInput là email hay tên đăng nhập
                if (loginInput.contains("@")) {
                    // Nếu là email, đăng nhập trực tiếp
                    signInWithEmail(auth, loginInput, password, context, navController) { isLoading = false }
                } else {
                    // Nếu là tên đăng nhập, tìm email tương ứng trong Firestore
                    firestore.collection("users")
                        .whereEqualTo("username", loginInput)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                isLoading = false
                                Toast.makeText(context, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show()
                            } else {
                                val userDoc = documents.first()
                                val email = userDoc.getString("email") ?: ""
                                if (email.isEmpty()) {
                                    isLoading = false
                                    Toast.makeText(context, "Lỗi: Không tìm thấy email", Toast.LENGTH_SHORT).show()
                                } else {
                                    signInWithEmail(auth, email, password, context, navController) { isLoading = false }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            Log.e("Login", "Failed to find username: ${e.message}")
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isLoading) "Đang xử lý..." else "Đăng nhập")
        }
    }
}

private fun signInWithEmail(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: android.content.Context,
    navController: NavController,
    onComplete: () -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener { result ->
            Log.d("Login", "Sign-in successful: ${result.user?.uid}")
            Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
                Log.d("Navigation", "Navigating to home from LoginScreen")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Login", "Sign-in failed: ${e.message}")
            Toast.makeText(context, "Đăng nhập thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        .addOnCompleteListener {
            onComplete()
        }
}