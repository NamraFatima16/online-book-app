// UserViewModel.kt
package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ie.setu.bookapp.model.User
import ie.setu.bookapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // StateFlow for current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // StateFlow for login state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // StateFlow for login error
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    // StateFlow for signup error
    private val _signupError = MutableStateFlow<String?>(null)
    val signupError: StateFlow<String?> = _signupError

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = repository.validateUser(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _loginError.value = null
                    Timber.d("User logged in: ${user.email}")
                } else {
                    _loginError.value = "Invalid email or password"
                    Timber.d("Login failed: Invalid credentials")
                }
            } catch (e: Exception) {
                _loginError.value = "Error during login: ${e.message}"
                Timber.e(e, "Login error")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        Timber.d("User logged out")
    }

    fun signup(user: User) {
        viewModelScope.launch {
            try {
                // Check if user already exists
                val existingUser = repository.getUserByEmail(user.email).first()
                if (existingUser != null) {
                    _signupError.value = "Email already registered"
                    Timber.d("Signup failed: Email already exists")
                    return@launch
                }

                repository.insertUser(user)
                _currentUser.value = user
                _isLoggedIn.value = true
                _signupError.value = null
                Timber.d("User signed up: ${user.email}")
            } catch (e: Exception) {
                _signupError.value = "Error during signup: ${e.message}"
                Timber.e(e, "Signup error")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                repository.updateUser(user)
                _currentUser.value = user
                Timber.d("User updated: ${user.email}")
            } catch (e: Exception) {
                Timber.e(e, "Update user error")
            }
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }

    fun clearSignupError() {
        _signupError.value = null
    }

    class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}