import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sportshop.viewmodel.AdminViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isAdmin: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(),
    adminViewModel: AdminViewModel
) {
    val lastName by userViewModel.lastName.collectAsState()
    val isRefreshing by adminViewModel.isRefreshing.collectAsState()

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { adminViewModel.fetchProducts() }) {
        TopAppBar(modifier = modifier, title = {
            Text(
                text = "XIN CHÀO, ${lastName.uppercase()}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }, actions = {
            IconButton(
                onClick = { navController.navigate("search_screen") },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            if (isAdmin) {
                IconButton(
                    onClick = { navController.navigate("admin_screen") },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = "Tìm kiếm",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
        )
    }
}