// UserRepository.kt
package ie.setu.bookapp.repository

import ie.setu.bookapp.database.UserDao
import ie.setu.bookapp.model.User
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    fun getUserByEmail(email: String): Flow<User?> {
        Timber.d("Fetching user with email: $email")
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        Timber.d("User added: ${user.firstName} ${user.lastName}")
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        Timber.d("User updated: ${user.firstName} ${user.lastName}")
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        Timber.d("User deleted: ${user.firstName} ${user.lastName}")
        userDao.deleteUser(user)
    }

    suspend fun validateUser(email: String, password: String): User? {
        Timber.d("Validating user: $email")
        return userDao.validateUser(email, password)
    }
}