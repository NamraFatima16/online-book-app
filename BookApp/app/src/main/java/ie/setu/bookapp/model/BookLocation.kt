package ie.setu.bookapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookstore_locations")
data class BookstoreLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null,
    val website: String? = null,
    val phoneNumber: String? = null,

    // Relation to books
    val associatedBookId: Int? = null,

    // Metadata
    val dateAdded: Long = System.currentTimeMillis()
)