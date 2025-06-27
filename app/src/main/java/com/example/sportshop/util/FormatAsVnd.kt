package com.example.sportshop.util

import java.text.NumberFormat
import java.util.Locale

object FormatAsVnd {
    fun format(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        formatter.maximumFractionDigits = 0
        return "₫" + formatter.format(amount)
    }
}


