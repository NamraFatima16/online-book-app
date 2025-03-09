package ie.setu.bookapp.repository

import ie.setu.bookapp.model.User
import timber.log.Timber

class UserRepository {
    private val users = mutableListOf<User>()

    init {
        Timber.d("UserRepository initialized")
    }

    // Create: Add a user
    fun addUser(user: User) {
        users.add(user)
        Timber.d("User added: ${user.firstName} ${user.lastName}")
    }

    // Read: Get all users
    fun getUsers(): List<User> {
        Timber.d("Fetching all users")
        return users
    }

    // Read: Get a user by email
    fun getUserByEmail(email: String): User? {
        Timber.d("Fetching user with email: $email")
        return users.find { it.email == email }
    }

    // Update: Update user details
    fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.email == updatedUser.email }
        if (index != -1) {
            users[index] = updatedUser
            Timber.d("User updated: ${updatedUser.firstName} ${updatedUser.lastName}")
        } else {
            Timber.d("User not found to update: ${updatedUser.firstName} ${updatedUser.lastName}")
        }
    }

    // Delete: Remove a user by email
    fun deleteUser(email: String) {
        val user = getUserByEmail(email)
        if (user != null) {
            users.remove(user)
            Timber.d("User deleted: ${user.firstName} ${user.lastName}")
        } else {
            Timber.d("User not found to delete with email: $email")
        }
    }
}
