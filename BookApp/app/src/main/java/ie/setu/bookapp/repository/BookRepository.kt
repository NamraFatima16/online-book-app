package ie.setu.bookapp.repository

import ie.setu.bookapp.database.BookDao
import ie.setu.bookapp.model.Book
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class BookRepository(private val bookDao: BookDao) {

    // Get all books
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    // Get favorite books
    val favoriteBooks: Flow<List<Book>> = bookDao.getFavoriteBooks()

    // Get downloaded books (assuming there's a downloaded flag in the Book entity)
    val downloadedBooks: Flow<List<Book>> = bookDao.getDownloadedBooks()

    // Get books by category
    fun getBooksByCategory(category: String): Flow<List<Book>> =
        bookDao.getBooksByCategory(category)

    // Get book by ID
    fun getBookById(id: Int): Flow<Book?> = bookDao.getBookById(id)

    // Insert a new book
    suspend fun insertBook(book: Book) {
        val bookId = bookDao.insertBook(book)
        Timber.d("Book inserted with ID: $bookId")
    }

    // Update an existing book
    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
        Timber.d("Book updated: ${book.title}")
    }

    // Delete a book
    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
        Timber.d("Book deleted: ${book.title}")
    }

    // Toggle favorite status
    suspend fun toggleFavorite(book: Book) {
        val updatedBook = book.copy(isFavorite = !book.isFavorite)
        updateBook(updatedBook)
        Timber.d("Book favorite toggled: ${book.title}, new status: ${updatedBook.isFavorite}")
    }


    suspend fun toggleDownload(book: Book) {

        Timber.d("Toggle download requested for book: ${book.title}")
    }


    fun searchBooks(query: String): Flow<List<Book>> {
        val searchQuery = "%$query%"
        return bookDao.searchBooks(searchQuery)
    }
}