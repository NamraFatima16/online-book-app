// BookRepository.kt
package ie.setu.bookapp.repository

import ie.setu.bookapp.database.BookDao
import ie.setu.bookapp.model.Book
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class BookRepository(private val bookDao: BookDao) {

    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    val favoriteBooks: Flow<List<Book>> = bookDao.getFavoriteBooks()
    val downloadedBooks: Flow<List<Book>> = bookDao.getDownloadedBooks()

    fun getBooksByCategory(category: String): Flow<List<Book>> {
        return bookDao.getBooksByCategory(category)
    }

    fun getBookById(id: Int): Flow<Book> {
        Timber.d("Fetching book with ID: $id")
        return bookDao.getBookById(id)
    }

    suspend fun insertBook(book: Book) {
        Timber.d("Book added: ${book.title}")
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book) {
        Timber.d("Book updated: ${book.title}")
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        Timber.d("Book deleted: ${book.title}")
        bookDao.deleteBook(book)
    }

    suspend fun toggleFavorite(book: Book) {
        val updatedBook = book.copy(isFavorite = !book.isFavorite)
        Timber.d("Book favorite toggled: ${book.title}, isFavorite: ${updatedBook.isFavorite}")
        bookDao.updateBook(updatedBook)
    }

    suspend fun toggleDownload(book: Book) {
        val updatedBook = book.copy(isDownloaded = !book.isDownloaded)
        Timber.d("Book download toggled: ${book.title}, isDownloaded: ${updatedBook.isDownloaded}")
        bookDao.updateBook(updatedBook)
    }
}