package com.example.sportshop.model.data

data class getUserForm(
    val fullName: String,
    val phone: String,
    val address: String
) {
    fun isValid(): Boolean = getErrorMessage() == null
    fun getErrorMessage(): String? {
        return when {
            fullName.isBlank() -> "Tên không được để trống"
            phone.isBlank() -> "Số điện thoại không được để trống"
            address.isBlank() -> "Địa chỉ không được để trống"
            !fullName.matches(Regex("^[\\p{L} ]{2,}$")) -> "Tên chỉ được chứa chữ cái và khoảng trắng"
            !phone.matches(Regex("^0\\d{9}$")) -> "SĐT không hợp lệ. Vui lòng nhập đúng định dạng 0XXXXXXXXX"
            address.length < 10 -> "Vui lòng nhập địa chỉ đầy đủ"
            else -> null
        }
    }
}
