import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(
    padding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit, // Fixed imageUrl handling
    feature: Boolean,
    onFeatureChange: (Boolean) -> Unit
) {
    val categories = listOf("Giày", "Quần", "Áo", "Phụ kiện")
    var expandedCategoryMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Input for Name
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Tên Sản Phẩm") },
            modifier = Modifier.fillMaxWidth(),
            isError = name.isBlank()
        )

        // Input for Price
        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("Giá") },
            modifier = Modifier.fillMaxWidth(),
            isError = price.replace(",", "").toDoubleOrNull() == null
        )

        // Input for Description
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth(),
            isError = description.isBlank()
        )

        // Input for Quantity
        OutlinedTextField(
            value = quantity,
            onValueChange = onQuantityChange,
            label = { Text("Số lượng") },
            modifier = Modifier.fillMaxWidth(),
            isError = quantity.toIntOrNull() == null
        )

        // Input for Image URL
        OutlinedTextField(
            value = imageUrl,
            onValueChange = onImageUrlChange, // Fix: Use onImageUrlChange to handle input change
            label = { Text("Ảnh URL") },
            modifier = Modifier.fillMaxWidth(),
            isError = imageUrl.isBlank()
        )

        // Category Dropdown Menu
        ExposedDropdownMenuBox(
            expanded = expandedCategoryMenu,
            onExpandedChange = { expandedCategoryMenu = it }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Danh mục") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = category.isBlank()
            )

            ExposedDropdownMenu(
                expanded = expandedCategoryMenu,
                onDismissRequest = { expandedCategoryMenu = false }
            ) {
                categories.forEach { categoryOption ->
                    DropdownMenuItem(
                        text = { Text(categoryOption) },
                        onClick = {
                            onCategoryChange(categoryOption)
                            expandedCategoryMenu = false
                        }
                    )
                }
            }
        }

        // Feature Checkbox
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(
                checked = feature,
                onCheckedChange = onFeatureChange
            )
            Text("Sản phẩm nổi bật")
        }
    }
}
