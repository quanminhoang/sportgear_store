import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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

                _isAdmin.value = admin
                _fullName.value = name
                _lastName.value = last
            }
    }
}
