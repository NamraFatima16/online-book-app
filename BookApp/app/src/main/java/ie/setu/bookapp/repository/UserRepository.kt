package ie.setu.bookapp.repository

import ie.setu.bookapp.database.UserDao
import ie.setu.bookapp.model.User
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

/**
 * Repository class for User-related data operations.
 * Acts as a clean API for data access to the rest of the application.
 */
class UserRepository(private val userDao: UserDao) {

    // Get all users
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    // Get user by ID
    fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)

    // Get user by email
    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    // Insert a new user
    suspend fun insertUser(user: User) {
        val userId = userDao.insertUser(user)
        Timber.d("User inserted with ID: $userId")
    }

    // Update existing user
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
        Timber.d("User updated: ${user.email}")
    }

    // Delete a user
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
        Timber.d("User deleted: ${user.email}")
    }

    // Validate user login credentials
    suspend fun validateUser(email: String, password: String): User? {
        val user = userDao.getUserByEmailAndPassword(email, password)
        if (user != null) {
            Timber.d("User validated: ${user.email}")
        } else {
            Timber.d("User validation failed for email: $email")
        }
        return user
    }
}