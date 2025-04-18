package com.example.sportshop.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportshop.ui.theme.SportShopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    theme: String,
    language: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    SportShopTheme(theme = theme) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Theme: $theme", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = theme == "Light", onClick = { onThemeChange("Light") })
                    Text("Light", modifier = Modifier
                        .clickable { onThemeChange("Light") }
                        .padding(start = 4.dp))
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = theme == "Dark", onClick = { onThemeChange("Dark") })
                    Text("Dark", modifier = Modifier
                        .clickable { onThemeChange("Dark") }
                        .padding(start = 4.dp))
                }

                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = language == "Eng", onClick = { onLanguageChange("Eng") })
                    Text("English", modifier = Modifier
                        .clickable { onLanguageChange("Eng") }
                        .padding(start = 4.dp))
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = language == "Viet", onClick = { onLanguageChange("Viet") })
                    Text("Vietnamese", modifier = Modifier
                        .clickable { onLanguageChange("Viet") }
                        .padding(start = 4.dp))
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}
