package ie.setu.bookapp.model

import timber.log.Timber
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val description: String,
    val isFavorite: Boolean = false,
    val coverUrl: String? = null,
    val isDownloaded: Boolean = false,
    val category: String = "Fiction"
) {
    init {
        Timber.d("Book created: $title by $author")
    }
}
