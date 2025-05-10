package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import ie.setu.bookapp.model.User
import ie.setu.bookapp.repository.FirebaseAuthManager
import ie.setu.bookapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


class UserViewModel(private val repository: UserRepository) : ViewModel() {


    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _signupError = MutableStateFlow<String?>(null)
    val signupError: StateFlow<String?> = _signupError

    private val _userOperationStatus = MutableStateFlow<UserOperationStatus>(UserOperationStatus.Idle)
    val userOperationStatus: StateFlow<UserOperationStatus> = _userOperationStatus

    private val firebaseAuthManager = FirebaseAuthManager()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                val firebaseUser = firebaseAuthManager.signInWithEmail(email, password)
                if (firebaseUser != null) {
                    // Get user data from Firestore
                    val userData = firebaseAuthManager.getUserData(firebaseUser.uid)

                    if (userData != null) {
                        _currentUser.value = userData
                        _isLoggedIn.value = true
                        _loginError.value = null
                        _userOperationStatus.value = UserOperationStatus.Success
                        Timber.d("User logged in: ${userData.email}")
                    } else {
                        _loginError.value = "Failed to retrieve user data"
                        _userOperationStatus.value = UserOperationStatus.Error("Failed to retrieve user data")
                    }
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

    // Adding Google sign-in function
    fun signInWithGoogle(googleAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            _userOperationStatus.value = UserOperationStatus.Loading
            try {
                val firebaseUser = firebaseAuthManager.signInWithGoogle(googleAccount)
                if (firebaseUser != null) {
                    // Get user data from Firestore
                    val userData = firebaseAuthManager.getUserData(firebaseUser.uid)

                    if (userData != null) {
                        _currentUser.value = userData
                        _isLoggedIn.value = true
                        _loginError.value = null
                        _userOperationStatus.value = UserOperationStatus.Success
                        Timber.d("User logged in with Google: ${userData.email}")
                    } else {
                        _loginError.value = "Failed to retrieve user data"
                        _userOperationStatus.value = UserOperationStatus.Error("Failed to retrieve user data")
                    }
                } else {
                    _loginError.value = "Google sign-in failed"
                    _userOperationStatus.value = UserOperationStatus.Error("Google sign-in failed")
                    Timber.d("Google sign-in failed")
                }
            } catch (e: Exception) {
                _loginError.value = "Error during Google sign-in: ${e.message}"
                _userOperationStatus.value = UserOperationStatus.Error("Google sign-in error: ${e.localizedMessage}")
                Timber.e(e, "Google sign-in error")
            }
        }
    }
    // logout function
    fun logout() {
        viewModelScope.launch {
            firebaseAuthManager.signOut()
            _currentUser.value = null
            _isLoggedIn.value = false
            _userOperationStatus.value = UserOperationStatus.Idle
            Timber.d("User logged out")
        }
    }

   //register user funtion
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
               val lastName = ""
               val firebaseUser = firebaseAuthManager.registerWithEmail(email, password, username, lastName)

               if (firebaseUser != null) {

                   val userData = firebaseAuthManager.getUserData(firebaseUser.uid)

                   if (userData != null) {
                       _currentUser.value = userData
                       _isLoggedIn.value = true
                       _userOperationStatus.value = UserOperationStatus.Success
                       onSuccess()
                       Timber.d("User registered successfully: $email")
                   } else {
                       onError("Failed to retrieve user data")
                       _userOperationStatus.value = UserOperationStatus.Error("Failed to retrieve user data")
                   }
               } else {
                   onError("Registration failed")
                   _userOperationStatus.value = UserOperationStatus.Error("Registration failed")
                   Timber.d("Registration failed")
               }
           } catch (e: Exception) {
               onError(e.message ?: "Registration failed")
               _userOperationStatus.value = UserOperationStatus.Error("Registration error: ${e.localizedMessage}")
               Timber.e(e, "Registration error")
           }
       }
   }

  //Creates new user
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

   //deletes users accounts
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