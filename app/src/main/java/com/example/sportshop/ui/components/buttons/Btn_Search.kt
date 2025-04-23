import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Btn_Search(navController: NavController) {
    IconButton(
        onClick = { navController.navigate("search_screen") },
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Tìm kiếm",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
