import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserInfoFields(
    name: String,
    phone: String,
    address: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = { Text("Mobile number") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = address,
        onValueChange = onAddressChange,
        label = { Text("Location") },
        modifier = Modifier.fillMaxWidth()
    )
}
