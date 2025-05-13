package ie.setu.bookapp.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(val auth: FirebaseAuth) : ViewModel() {


    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }

            is SignUpUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }

            is SignUpUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.confirmPassword, error = null) }

            }
            SignUpUiEvent.SignUpButtonClicked -> {
                signUp()
            }
        }
    }

    class SignUpViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(auth) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    fun resetSignUpState() {
        _uiState.update { SignUpUiState() }
    }

    private fun isValidInput(): Boolean {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(error = "Email and password cannot be empty.") }
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email.trim()).matches()) {
            _uiState.update { it.copy(error = "Please enter a valid email address.") }
            return false
        }
        if (currentState.password.length < 6) {
            _uiState.update { it.copy(error = "Password must be at least 6 characters long.") }
            return false
        }
        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match.") }
            return false
        }
        return true
    }

    private fun signUp() {
        if (!isValidInput()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                auth.createUserWithEmailAndPassword(
                    _uiState.value.email.trim(),
                    _uiState.value.password.trim()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        val exception = task.exception
                        val errorMessage = when (exception) {
                            is FirebaseAuthUserCollisionException -> "An account already exists with this email."
                            is FirebaseAuthException -> exception.message ?: "Sign up failed."
                            else -> exception?.message ?: "Sign up failed. Please try again."
                        }
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
                        error = e.message ?: "An unexpected error occurred.",
                        isSuccess = false
                    )
                }
            }
        }
    }

}



