// BookViewModel.kt
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

    init {
        getAllBooks()
        getFavoriteBooks()
        getDownloadedBooks()
        getBooksByCategory(_selectedCategory.value)
    }

    private fun getAllBooks() {
        viewModelScope.launch {
            repository.allBooks
                .catch { e ->
                    Timber.e(e, "Error fetching all books")
                }
                .collect {
                    _allBooks.value = it
                    Timber.d("Updated allBooks: ${it.size} books")
                }
        }
    }

    private fun getFavoriteBooks() {
        viewModelScope.launch {
            repository.favoriteBooks
                .catch { e ->
                    Timber.e(e, "Error fetching favorite books")
                }
                .collect {
                    _favoriteBooks.value = it
                    Timber.d("Updated favoriteBooks: ${it.size} books")
                }
        }
    }

    private fun getDownloadedBooks() {
        viewModelScope.launch {
            repository.downloadedBooks
                .catch { e ->
                    Timber.e(e, "Error fetching downloaded books")
                }
                .collect {
                    _downloadedBooks.value = it
                    Timber.d("Updated downloadedBooks: ${it.size} books")
                }
        }
    }

    fun getBooksByCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            repository.getBooksByCategory(category)
                .catch { e ->
                    Timber.e(e, "Error fetching books for category: $category")
                }
                .collect {
                    _booksByCategory.value = it
                    Timber.d("Updated booksByCategory for $category: ${it.size} books")
                }
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
            Timber.d("Book added: ${book.title}")
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateBook(book)
            Timber.d("Book updated: ${book.title}")
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
            Timber.d("Book deleted: ${book.title}")
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            repository.toggleFavorite(book)
            Timber.d("Toggled favorite for book: ${book.title}")
        }
    }

    fun toggleDownload(book: Book) {
        viewModelScope.launch {
            repository.toggleDownload(book)
            Timber.d("Toggled download for book: ${book.title}")
        }
    }

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
