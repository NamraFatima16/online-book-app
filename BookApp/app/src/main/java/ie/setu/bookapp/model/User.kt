package ie.setu.bookapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import timber.log.Timber

@Entity(tableName = "users")
data class User(
    val firstName: String = "",
    val lastName: String = "",
    @PrimaryKey
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String? = null
) {
    init {
        Timber.d("User created: $firstName $lastName")
    }
}