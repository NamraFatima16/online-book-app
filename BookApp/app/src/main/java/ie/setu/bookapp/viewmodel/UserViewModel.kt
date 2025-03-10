package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import ie.setu.bookapp.model.User
import ie.setu.bookapp.repository.UserRepository
import timber.log.Timber

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    //Add user
    fun addUser(user: User) {
        userRepository.addUser(user)
        Timber.d("User added via ViewModel: ${user.firstName} ${user.lastName}")
    }

    //Get all users
    fun getUsers(): List<User> {
        Timber.d("Getting users via ViewModel")
        return userRepository.getUsers()
    }

    //Get user by email
    fun getUserByEmail(email: String): User? {
        Timber.d("Getting user by email: $email via ViewModel")
        return userRepository.getUserByEmail(email)
    }

    //Update user details
    fun updateUser(updatedUser: User) {
        userRepository.updateUser(updatedUser)
        Timber.d("User updated via ViewModel: ${updatedUser.firstName} ${updatedUser.lastName}")
    }

    // Delete user by email
    fun deleteUser(email: String) {
        userRepository.deleteUser(email)
        Timber.d("User deleted via ViewModel with email: $email")
    }
}
