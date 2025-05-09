package ie.setu.bookapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,

    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,


    val phoneNumber: String? = null,
    val profileImagePath: String? = null,
    val preferences: String? = null,


    val isActive: Boolean = true,


    val dateCreated: Long = System.currentTimeMillis(),
    val lastLogin: Long? = null
)