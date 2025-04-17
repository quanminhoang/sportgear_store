package com.example.sportshop.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportshop.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sportshop.ui.components.Btn_GoogleSignIn
import com.example.sportshop.ui.theme.SportsShopTheme


// Welcome Screen
@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_shop),
            contentDescription = "logo",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp)
                .size(240.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Chào mừng\nđến với Sport Store!",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Hãy trở thành một phần của cộng đồng Sport Shop – nơi bạn có thể tìm thấy những sản phẩm chất lượng, ưu đãi độc quyền và nhiều tiện ích chỉ dành riêng cho thành viên",
            modifier = Modifier.padding(top = 16.dp),
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.run { height(100.dp) })

        Btn_GoogleSignIn(
            onSignInSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun WelcomeScreenPreview() {
        val navController = rememberNavController()
        SportsShopTheme(theme = "Light") {
            WelcomeScreen(navController = navController)
        }
    }
