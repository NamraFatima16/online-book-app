package ie.setu.bookapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val author: String,
    val description: String? = null,
    val category: String,

    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,

    // Additional fields for more book information
    val imageUrl: String? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val pageCount: Int? = null,
    val isbn: String? = null,
    val language: String? = null,
    val rating: Float? = null,

    // Timestamps for tracking
    val dateAdded: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)