import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun UserInfoFields(
    name: String,
    phone: String,
    address: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    val textFieldModifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp) // Padding cho các TextField

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // Field 1: Name
        Text(text = "Tên",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = name,
            onValueChange = onNameChange,
            modifier = textFieldModifier,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp), // Bo góc nhẹ
            placeholder = {
                Text(
                    "Nhập tên", style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            })

        Spacer(modifier = Modifier.height(32.dp)) // Khoảng cách giữa các TextField

        // Field 2: Phone
        Text(text = "Số điện thoại",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = phone,
            onValueChange = onPhoneChange,
            modifier = textFieldModifier,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface // Màu chữ khi người dùng nhập
            ),
            shape = RoundedCornerShape(12.dp), // Bo góc nhẹ
            placeholder = {
                Text(
                    "Nhập SĐT", style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            })

        Spacer(modifier = Modifier.height(32.dp))

        // Field 3: Address
        Text(text = "Địa chỉ",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = address,
            onValueChange = onAddressChange,
            modifier = textFieldModifier,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface // Màu chữ khi người dùng nhập
            ),
            shape = RoundedCornerShape(12.dp), // Bo góc nhẹ
            placeholder = {
                Text(
                    "Nhập địa chỉ", style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            })
    }
}
