package ie.setu.bookapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ie.setu.bookapp.database.BookDao
import ie.setu.bookapp.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class BookRepository(private val bookDao: BookDao) {
    private val firestore = FirebaseFirestore.getInstance()
    private val booksCollection = firestore.collection("books")
    private val auth = FirebaseAuth.getInstance()

    // Get all books
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    // Get favorite books
    val favoriteBooks: Flow<List<Book>> = bookDao.getFavoriteBooks()

    // Get downloaded books
    val downloadedBooks: Flow<List<Book>> = bookDao.getDownloadedBooks()

    // Get books by category
    fun getBooksByCategory(category: String): Flow<List<Book>> =
        bookDao.getBooksByCategory(category)

    // Get book by ID
    fun getBookById(id: Int): Flow<Book?> = bookDao.getBookById(id)

    // Insert a new book
    suspend fun insertBook(book: Book) {
        try {
            // 1. Insert in Room database first
            val bookId = bookDao.insertBook(book)
            Timber.d("Book inserted with ID: $bookId")

            // 2. Insert in Firestore
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                val bookData = mapOf(
                    "title" to book.title,
                    "author" to book.author,
                    "description" to book.description,
                    "category" to book.category,
                    "isFavorite" to book.isFavorite,
                    "isDownloaded" to book.isDownloaded,
                    "publisher" to book.publisher,
                    "publishedDate" to book.publishedDate,
                    "pageCount" to book.pageCount,
                    "isbn" to book.isbn,
                    "language" to book.language,
                    "rating" to book.rating,
                    "dateAdded" to book.dateAdded,
                    "lastModified" to book.lastModified,
                    "userId" to currentUserId,
                    "localId" to bookId
                )

                booksCollection.add(bookData).await()
                Timber.d("Book added to Firestore: ${book.title}")
            } else {
                Timber.w("No user signed in, book saved only locally")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error inserting book")
            throw e
        }
    }

    // Update an existing book
    suspend fun updateBook(book: Book) {
        try {
            // 1. Update in Room
            bookDao.updateBook(book)
            Timber.d("Book updated in Room: ${book.title}")

            // 2. Update in Firestore
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                val query = booksCollection
                    .whereEqualTo("localId", book.id)
                    .whereEqualTo("userId", currentUserId)
                    .get()
                    .await()

                if (!query.isEmpty) {
                    val documentId = query.documents[0].id
                    val bookData = mapOf(
                        "title" to book.title,
                        "author" to book.author,
                        "description" to book.description,
                        "category" to book.category,
                        "isFavorite" to book.isFavorite,
                        "isDownloaded" to book.isDownloaded,
                        "publisher" to book.publisher,
                        "publishedDate" to book.publishedDate,
                        "pageCount" to book.pageCount,
                        "isbn" to book.isbn,
                        "language" to book.language,
                        "rating" to book.rating,
                        "lastModified" to System.currentTimeMillis()
                    )

                    booksCollection.document(documentId).update(bookData).await()
                    Timber.d("Book updated in Firestore: ${book.title}")
                } else {
                    Timber.w("Book not found in Firestore, only updated locally")
                }
            } else {
                Timber.w("No user signed in, book updated only locally")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating book")
            throw e
        }
    }

    // Delete a book
    suspend fun deleteBook(book: Book) {
        try {
            // 1. Delete from Room
            bookDao.deleteBook(book)
            Timber.d("Book deleted from Room: ${book.title}")

            // 2. Delete from Firestore
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                val query = booksCollection
                    .whereEqualTo("localId", book.id)
                    .whereEqualTo("userId", currentUserId)
                    .get()
                    .await()

                if (!query.isEmpty) {
                    val documentId = query.documents[0].id
                    booksCollection.document(documentId).delete().await()
                    Timber.d("Book deleted from Firestore: ${book.title}")
                } else {
                    Timber.w("Book not found in Firestore, only deleted locally")
                }
            } else {
                Timber.w("No user signed in, book deleted only locally")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting book")
            throw e
        }
    }

    // Toggle favorite status
    suspend fun toggleFavorite(book: Book) {
        val updatedBook = book.copy(isFavorite = !book.isFavorite)
        updateBook(updatedBook)
        Timber.d("Book favorite toggled: ${book.title}, new status: ${updatedBook.isFavorite}")
    }

    // Toggle download status
    suspend fun toggleDownload(book: Book) {
        val updatedBook = book.copy(isDownloaded = !book.isDownloaded)
        updateBook(updatedBook)
        Timber.d("Book download toggled: ${book.title}, new status: ${updatedBook.isDownloaded}")
    }

    // Sync books from Firestore to local database
    suspend fun syncBooksFromFirestore() {
        try {
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                val snapshot = booksCollection
                    .whereEqualTo("userId", currentUserId)
                    .get()
                    .await()

                for (document in snapshot.documents) {
                    val firestoreBook = document.data
                    val localId = firestoreBook?.get("localId") as? Long

                    if (localId != null) {
                        // Check if book exists locally
                        val localBook = bookDao.getBookByIdSync(localId.toInt())

                        if (localBook == null) {
                            // Book doesn't exist locally, insert it
                            val newBook = Book(
                                id = 0, // Room will auto-generate
                                title = firestoreBook["title"] as String,
                                author = firestoreBook["author"] as String,
                                description = firestoreBook["description"] as? String,
                                category = firestoreBook["category"] as String,
                                isFavorite = firestoreBook["isFavorite"] as Boolean,
                                isDownloaded = firestoreBook["isDownloaded"] as Boolean,
                                publisher = firestoreBook["publisher"] as? String,
                                publishedDate = firestoreBook["publishedDate"] as? String,
                                pageCount = (firestoreBook["pageCount"] as? Long)?.toInt(),
                                isbn = firestoreBook["isbn"] as? String,
                                language = firestoreBook["language"] as? String,
                                rating = (firestoreBook["rating"] as? Double)?.toFloat(),
                                dateAdded = firestoreBook["dateAdded"] as Long,
                                lastModified = firestoreBook["lastModified"] as Long
                            )
                            bookDao.insertBook(newBook)
                            Timber.d("Book synced from Firestore to local: ${newBook.title}")
                        }
                    }
                }
                Timber.d("Books sync completed from Firestore to local")
            } else {
                Timber.w("No user signed in, cannot sync books from Firestore")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error syncing books from Firestore")
        }
    }

    // Search books
    fun searchBooks(query: String): Flow<List<Book>> {
        val searchQuery = "%$query%"
        return bookDao.searchBooks(searchQuery)
    }
}