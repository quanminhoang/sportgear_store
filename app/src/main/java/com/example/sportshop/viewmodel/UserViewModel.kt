import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportshop.model.data.getUserForm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val admin = document.getBoolean("isAdmin") ?: false
                val name = document.getString("name")?.trim() ?: "Người dùng"

                val last = name.split(" ").lastOrNull() ?: name
                _phone.value = document.getString("phone") ?: ""
                _address.value = document.getString("address") ?: ""
                _isAdmin.value = admin
                _fullName.value = name
                _lastName.value = last
            }
    }
    fun updateUserInfo(name: String, phone: String, address: String) {
        _fullName.value = name
        _phone.value = phone
        _address.value = address
    }

    // Kết hợp các trường để tạo thành form người dùng
    val getUserForm = combine(_fullName, _phone, _address) { full, phone, addr ->
        getUserForm(full, phone, addr)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), getUserForm("", "", ""))
    fun isValidUserForm(): Boolean {
        return getUserForm.value.isValid() // Kiểm tra tính hợp lệ của form người dùn
    }
    fun getUserFormError(): String? {
        return getUserForm.value.getErrorMessage() // Trả về lỗi đầu tiên hoặc null nếu hợp lệ
    }

}
