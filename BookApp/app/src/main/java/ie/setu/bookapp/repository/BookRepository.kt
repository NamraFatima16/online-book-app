package ie.setu.bookapp.repository

import ie.setu.bookapp.model.Book
import timber.log.Timber

class BookRepository {
    private val books = mutableListOf<Book>()

    init {
        Timber.d("BookRepository initialized")
    }

    // Create: Add a book
    fun addBook(book: Book) {
        books.add(book)
        Timber.d("Book added: ${book.title}")
    }

    // Read: Get all books
    fun getBooks(): List<Book> {
        Timber.d("Fetching all books")
        return books
    }

    // Read: Get a book by ID
    fun getBookById(id: Int): Book? {
        Timber.d("Fetching book with ID: $id")
        return books.find { it.id == id }
    }

    // Update: Update book details
    fun updateBook(updatedBook: Book) {
        val index = books.indexOfFirst { it.id == updatedBook.id }
        if (index != -1) {
            books[index] = updatedBook
            Timber.d("Book updated: ${updatedBook.title}")
        } else {
            Timber.d("Book not found to update: ${updatedBook.title}")
        }
    }

    // Delete: Remove a book by ID
    fun deleteBook(id: Int) {
        val book = getBookById(id)
        if (book != null) {
            books.remove(book)
            Timber.d("Book deleted: ${book.title}")
        } else {
            Timber.d("Book not found to delete with ID: $id")
        }
    }
}
