package com.example.sportshop.ui.components.buttons

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportshop.GoogleAuthManager
import com.example.sportshop.R

@Composable
fun Btn_GoogleSignIn(
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authManager = remember { GoogleAuthManager(context) }
    val signInClient = remember { authManager.getGoogleSignInClient() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authManager.handleSignInResult(
            intent = result.data,
            onSuccess = onSignInSuccess,
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    Button(
        onClick = { signInClient.signOut().addOnCompleteListener {
            launcher.launch(signInClient.signInIntent)}
        },
        modifier = modifier
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF1F1F1F)
        ),
        border = BorderStroke(1.dp, Color(0xFF747775)),
        shape = RoundedCornerShape(4.dp),
        elevation = null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Continue with Google",
                fontSize = 16.sp,
                letterSpacing = 0.25.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}