package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.repository.BookRepository
import timber.log.Timber

class LibraryViewModel : ViewModel() {
    private val bookRepository = BookRepository()

    // Add book
    fun addBook(book: Book) {
        bookRepository.addBook(book)
        Timber.d("Book added via ViewModel: ${book.title}")
    }

    // Get all books
    fun getBooks(): List<Book> {
        Timber.d("Getting books via ViewModel")
        return bookRepository.getBooks()
    }

    // Get book by ID
    fun getBookById(id: Int): Book? {
        Timber.d("Getting book by ID $id via ViewModel")
        return bookRepository.getBookById(id)
    }

    // Update book details
    fun updateBook(updatedBook: Book) {
        bookRepository.updateBook(updatedBook)
        Timber.d("Book updated via ViewModel: ${updatedBook.title}")
    }

    // Delete book by ID
    fun deleteBook(id: Int) {
        bookRepository.deleteBook(id)
        Timber.d("Book deleted via ViewModel with ID: $id")
    }
}
