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

class LibraryViewModel(private val repository: BookRepository) : ViewModel() {

    // StateFlow for all books
    private val _allBooks = MutableStateFlow<List<Book>>(emptyList())
    val allBooks: StateFlow<List<Book>> = _allBooks

    // StateFlow for favorite books
    private val _favoriteBooks = MutableStateFlow<List<Book>>(emptyList())
    val favoriteBooks: StateFlow<List<Book>> = _favoriteBooks

    init {
        getAllBooks()
        getFavoriteBooks()
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

    // Add book
    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
            Timber.d("Book added via ViewModel: ${book.title}")
        }
    }

    // Update book details
    fun updateBook(updatedBook: Book) {
        viewModelScope.launch {
            repository.updateBook(updatedBook)
            Timber.d("Book updated via ViewModel: ${updatedBook.title}")
        }
    }

    // Delete book
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
            Timber.d("Book deleted via ViewModel: ${book.title}")
        }
    }

    // Toggle favorite status
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            repository.toggleFavorite(book)
            Timber.d("Book favorite toggled: ${book.title}")
        }
    }

    // Factory for creating the ViewModel with dependency injection
    class LibraryViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LibraryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}