package ie.setu.bookapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(val auth: FirebaseAuth): ViewModel(){

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    private fun isValidInput(): Boolean {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.value = currentState.copy(error = "Email and password cannot be blank")
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email.trim()).matches()) {
            _uiState.update { it.copy(error = "Please enter a valid email address.") }
            return false
        }
        return true
    }


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            is LoginUIEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }
            LoginUIEvent.LoginButtonClicked -> {
                signIn()
            }
        }
    }

    class LoginViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(auth) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun signIn() {
        if (!isValidInput()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                auth.signInWithEmailAndPassword(
                    _uiState.value.email.trim(),
                    _uiState.value.password.trim()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        val errorMessage = task.exception?.message ?: "Login Failed. Unknown error"
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage,
                                isSuccess = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error",
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
            _uiState.update { LoginUiState(isSuccess = false) }
        }
    }

    fun resetLoginState() {
        _uiState.update { LoginUiState()}
    }

}