package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for handling book-related operations following MVVM pattern
 */
class BookViewModel(private val repository: BookRepository) : ViewModel() {

    // StateFlow for all books
    private val _allBooks = MutableStateFlow<List<Book>>(emptyList())
    val allBooks: StateFlow<List<Book>> = _allBooks

    // StateFlow for favorite books
    private val _favoriteBooks = MutableStateFlow<List<Book>>(emptyList())
    val favoriteBooks: StateFlow<List<Book>> = _favoriteBooks

    // StateFlow for downloaded books
    private val _downloadedBooks = MutableStateFlow<List<Book>>(emptyList())
    val downloadedBooks: StateFlow<List<Book>> = _downloadedBooks

    // StateFlow for books by category
    private val _booksByCategory = MutableStateFlow<List<Book>>(emptyList())
    val booksByCategory: StateFlow<List<Book>> = _booksByCategory

    // State for selected category
    private val _selectedCategory = MutableStateFlow("Fiction")
    val selectedCategory: StateFlow<String> = _selectedCategory

    // State for operation status
    private val _operationStatus = MutableStateFlow<OperationStatus>(OperationStatus.Idle)
    val operationStatus: StateFlow<OperationStatus> = _operationStatus

    init {
        fetchAllBooks()
        fetchFavoriteBooks()
        fetchDownloadedBooks()
        getBooksByCategory(_selectedCategory.value)
    }

    /**
     * Get a specific book by ID
     */
    fun getBookById(id: Int) = repository.getBookById(id)

    /**
     * Fetch all books from repository
     */
    fun fetchAllBooks() {
        viewModelScope.launch {
            _operationStatus.value = OperationStatus.Loading
            repository.allBooks
                .catch { e ->
                    Timber.e(e, "Error fetching all books")
                    _operationStatus.value = OperationStatus.Error("Failed to fetch books: ${e.localizedMessage}")
                }
                .collect {
                    _allBooks.value = it
                    _operationStatus.value = OperationStatus.Success
                    Timber.d("Fetched ${it.size} books")
                }
        }
    }

    /**
     * Fetch favorite books from repository
     */
    private fun fetchFavoriteBooks() {
        viewModelScope.launch {
            repository.favoriteBooks
                .catch { e ->
                    Timber.e(e, "Error fetching favorite books")
                }
                .collect {
                    _favoriteBooks.value = it
                    Timber.d("Fetched ${it.size} favorite books")
                }
        }
    }

    /**
     * Fetch downloaded books from repository
     */
    private fun fetchDownloadedBooks() {
        viewModelScope.launch {
            repository.downloadedBooks
                .catch { e ->
                    Timber.e(e, "Error fetching downloaded books")
                }
                .collect {
                    _downloadedBooks.value = it
                    Timber.d("Fetched ${it.size} downloaded books")
                }
        }
    }

    /**
     * Get books by category
     */
    fun getBooksByCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _operationStatus.value = OperationStatus.Loading
            repository.getBooksByCategory(category)
                .catch { e ->
                    Timber.e(e, "Error fetching books for category: $category")
                    _operationStatus.value = OperationStatus.Error("Failed to fetch books by category: ${e.localizedMessage}")
                }
                .collect {
                    _booksByCategory.value = it
                    _operationStatus.value = OperationStatus.Success
                    Timber.d("Fetched ${it.size} books for category: $category")
                }
        }
    }

    /**
     * Search books by query
     */
    fun searchBooks(query: String): List<Book> {
        val lowerCaseQuery = query.trim().lowercase()
        return if (lowerCaseQuery.isEmpty()) {
            emptyList()
        } else {
            _allBooks.value.filter { book ->
                book.title.lowercase().contains(lowerCaseQuery) ||
                        book.author.lowercase().contains(lowerCaseQuery) ||
                        (book.description?.lowercase()?.contains(lowerCaseQuery) ?: false) ||
                        book.category.lowercase().contains(lowerCaseQuery)
            }
        }
    }

    /**
     * Add a new book
     */
    fun addBook(book: Book) {
        viewModelScope.launch {
            _operationStatus.value = OperationStatus.Loading
            try {
                repository.insertBook(book)
                _operationStatus.value = OperationStatus.Success
                fetchAllBooks() // Refresh the book list
                Timber.d("Book added: ${book.title}")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to add book: ${e.localizedMessage}")
                Timber.e(e, "Error adding book")
            }
        }
    }

    /**
     * Update an existing book
     */
    fun updateBook(book: Book) {
        viewModelScope.launch {
            _operationStatus.value = OperationStatus.Loading
            try {
                repository.updateBook(book)
                _operationStatus.value = OperationStatus.Success
                // Refresh relevant data
                fetchAllBooks()
                fetchFavoriteBooks()
                getBooksByCategory(_selectedCategory.value)
                Timber.d("Book updated: ${book.title}")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to update book: ${e.localizedMessage}")
                Timber.e(e, "Error updating book")
            }
        }
    }

    /**
     * Delete a book
     */
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            _operationStatus.value = OperationStatus.Loading
            try {
                repository.deleteBook(book)
                _operationStatus.value = OperationStatus.Success
                // Refresh all data
                fetchAllBooks()
                fetchFavoriteBooks()
                fetchDownloadedBooks()
                getBooksByCategory(_selectedCategory.value)
                Timber.d("Book deleted: ${book.title}")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to delete book: ${e.localizedMessage}")
                Timber.e(e, "Error deleting book")
            }
        }
    }

    /**
     * Toggle favorite status for a book
     */
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            try {
                // Create a copy with toggled favorite status
                val updatedBook = book.copy(isFavorite = !book.isFavorite)
                repository.updateBook(updatedBook)

                // Refresh data to show the updated status
                fetchAllBooks()
                fetchFavoriteBooks()
                getBooksByCategory(_selectedCategory.value)

                Timber.d("Toggled favorite for book: ${book.title}, new status: ${updatedBook.isFavorite}")
            } catch (e: Exception) {
                Timber.e(e, "Error toggling favorite")
                _operationStatus.value = OperationStatus.Error("Failed to toggle favorite: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Toggle download status for a book
     */
    fun toggleDownload(book: Book) {
        viewModelScope.launch {
            try {
                // Currently this is just a log message, you'd need to implement the actual logic
                // For example, update a 'isDownloaded' property in the Book class
                repository.toggleDownload(book)

                // Refresh data
                fetchAllBooks()
                fetchDownloadedBooks()

                Timber.d("Toggled download for book: ${book.title}")
            } catch (e: Exception) {
                Timber.e(e, "Error toggling download")
                _operationStatus.value = OperationStatus.Error("Failed to toggle download: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Factory for creating BookViewModel with dependency injection
     */
    class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * Sealed class for representing operation status
 */
sealed class OperationStatus {
    object Idle : OperationStatus()
    object Loading : OperationStatus()
    object Success : OperationStatus()
    data class Error(val message: String) : OperationStatus()
}