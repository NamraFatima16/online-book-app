package ie.setu.bookapp.model

import timber.log.Timber

data class Book(
    var id: Int,
    var title: String,
    var author: String,
    var description: String,
    var isFavorite: Boolean = false
) {
    init {
        Timber.d("Book created: $title by $author")
    }

    // Update the favorite status
    fun toggleFavorite() {
        isFavorite = !isFavorite
        Timber.d("Favorite status of '$title' changed to: $isFavorite")
    }
}
