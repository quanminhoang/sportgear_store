package com.example.sportshop.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuantityEditor(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int? = null,
    max: Int? = null
) {
    val minValue = min ?: 1
    val maxValue = max?.coerceAtLeast(minValue) ?: 99 // Ensure max >= min

    val displayQuantity = quantity.coerceIn(minValue, maxValue)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline, RectangleShape)
    ) {
        OutlinedButton(
            onClick = { if (quantity > minValue) onQuantityChange(quantity - 1) },
            enabled = quantity > minValue,
            contentPadding = PaddingValues(2.dp),
            modifier = Modifier.size(24.dp),
            shape = RectangleShape
        ) {
            Text("-")
        }
        Text(
            text = displayQuantity.toString(),
            modifier = Modifier
                .width(24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedButton(
            onClick = { if (quantity < maxValue) onQuantityChange(quantity + 1) },
            enabled = quantity < maxValue,
            contentPadding = PaddingValues(2.dp),
            modifier = Modifier.size(24.dp),
            shape = RectangleShape
        ) {
            Text("+")
        }
    }
}