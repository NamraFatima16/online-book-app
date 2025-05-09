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

    // StateFlow for user operation status
    private val _userOperationStatus = MutableStateFlow<UserOperationStatus>(UserOperationStatus.Idle)
    val userOperationStatus: StateFlow<UserOperationStatus> = _userOperationStatus


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                val user = repository.validateUser(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _loginError.value = null
                    _userOperationStatus.value = UserOperationStatus.Success
                    Timber.d("User logged in: ${user.email}")
                } else {
                    _loginError.value = "Invalid email or password"
                    _userOperationStatus.value = UserOperationStatus.Error("Invalid email or password")
                    Timber.d("Login failed: Invalid credentials")
                }
            } catch (e: Exception) {
                _loginError.value = "Error during login: ${e.message}"
                _userOperationStatus.value = UserOperationStatus.Error("Login error: ${e.localizedMessage}")
                Timber.e(e, "Login error")
            }
        }
    }

    /**
     * Log out current user
     */
    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        _userOperationStatus.value = UserOperationStatus.Idle
        Timber.d("User logged out")
    }

    /**
     * Register a new user
     */
    fun registerUser(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                // Check if email already exists
                val existingUser = repository.getUserByEmail(email).first()
                if (existingUser != null) {
                    onError("Email already registered")
                    _userOperationStatus.value = UserOperationStatus.Error("Email already registered")
                    Timber.d("Registration failed: Email already exists")
                    return@launch
                }

                // Create a new user
                val newUser = User(
                    firstName = username,
                    lastName = "",  // This could be expanded to capture last name separately
                    email = email,
                    password = password
                )

                // Insert the user
                repository.insertUser(newUser)

                // Update state
                _currentUser.value = newUser
                _isLoggedIn.value = true
                _userOperationStatus.value = UserOperationStatus.Success

                // Trigger success callback
                onSuccess()
                Timber.d("User registered successfully: $email")
            } catch (e: Exception) {
                onError(e.message ?: "Registration failed")
                _userOperationStatus.value = UserOperationStatus.Error("Registration error: ${e.localizedMessage}")
                Timber.e(e, "Registration error")
            }
        }
    }

    /**
     * Create new user account
     */
    fun createUser(user: User) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                // Check if email already exists
                val existingUser = repository.getUserByEmail(user.email).first()
                if (existingUser != null) {
                    _signupError.value = "Email already registered"
                    _userOperationStatus.value = UserOperationStatus.Error("Email already registered")
                    Timber.d("Signup failed: Email already exists")
                    return@launch
                }

                repository.insertUser(user)
                _currentUser.value = user
                _isLoggedIn.value = true
                _signupError.value = null
                _userOperationStatus.value = UserOperationStatus.Success
                Timber.d("User created: ${user.email}")
            } catch (e: Exception) {
                _signupError.value = "Error during signup: ${e.message}"
                _userOperationStatus.value = UserOperationStatus.Error("Signup error: ${e.localizedMessage}")
                Timber.e(e, "Signup error")
            }
        }
    }


    fun updateUserProfile(
        userId: Int? = null,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        password: String? = null
    ) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading

            try {
                // Get current user if it exists
                val currentUserValue = _currentUser.value ?: run {
                    _userOperationStatus.value = UserOperationStatus.Error("No user logged in")
                    Timber.e("Cannot update profile: No user logged in")
                    return@launch
                }


                val updatedUser = currentUserValue.copy(
                    userId = userId ?: currentUserValue.userId,
                    firstName = firstName ?: currentUserValue.firstName,
                    lastName = lastName ?: currentUserValue.lastName,
                    email = email ?: currentUserValue.email,
                    password = password ?: currentUserValue.password
                )

                // Update the user in the repository
                repository.updateUser(updatedUser)

                // Update current user
                _currentUser.value = updatedUser
                _userOperationStatus.value = UserOperationStatus.Success

                Timber.d("User profile updated: ${updatedUser.email}")
            } catch (e: Exception) {
                _userOperationStatus.value = UserOperationStatus.Error("Update profile error: ${e.localizedMessage}")
                Timber.e(e, "Error updating user profile")
            }
        }
    }


    fun updateUser(user: User) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                repository.updateUser(user)
                _currentUser.value = user
                _userOperationStatus.value = UserOperationStatus.Success
                Timber.d("User updated: ${user.email}")
            } catch (e: Exception) {
                _userOperationStatus.value = UserOperationStatus.Error("Update user error: ${e.localizedMessage}")
                Timber.e(e, "Error updating user")
            }
        }
    }

    /**
     * Delete user account
     */
    fun deleteUserAccount() {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading

            try {
                val currentUserValue = _currentUser.value ?: run {
                    _userOperationStatus.value = UserOperationStatus.Error("No user logged in")
                    return@launch
                }

                repository.deleteUser(currentUserValue)

                // Clear user state
                _currentUser.value = null
                _isLoggedIn.value = false
                _userOperationStatus.value = UserOperationStatus.Success

                Timber.d("User account deleted: ${currentUserValue.email}")
            } catch (e: Exception) {
                _userOperationStatus.value = UserOperationStatus.Error("Delete account error: ${e.localizedMessage}")
                Timber.e(e, "Error deleting user account")
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


sealed class UserOperationStatus {
    object Idle : UserOperationStatus()
    object Loading : UserOperationStatus()
    object Success : UserOperationStatus()
    data class Error(val message: String) : UserOperationStatus()
}