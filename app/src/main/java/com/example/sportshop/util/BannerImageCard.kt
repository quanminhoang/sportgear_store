import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CollectionImageCard(
    imageResId: Int, collection: String, navController: NavController, featured: Boolean
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate(
                "all_products?featured=false&category=&collection=$collection"
            )
        }
        .padding(top = 8.dp)) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = collection,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )


        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = collection, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold, color = Color.White
                )
            )
        }
    }
}