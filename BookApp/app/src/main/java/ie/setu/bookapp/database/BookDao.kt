package ie.setu.bookapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ie.setu.bookapp.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY title ASC")
    fun getAllBooks(): Flow<List<Book>>


    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): Flow<Book?>


    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteBooks(): Flow<List<Book>>


    @Query("SELECT * FROM books WHERE category = :category ORDER BY title ASC")
    fun getBooksByCategory(category: String): Flow<List<Book>>


    @Query("SELECT * FROM books WHERE isDownloaded = 1 ORDER BY title ASC")
    fun getDownloadedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    suspend fun getBookByIdSync(bookId: Int): Book?

    @Query("""
        SELECT * FROM books 
        WHERE title LIKE :query 
        OR author LIKE :query 
        OR description LIKE :query 
        OR category LIKE :query 
        ORDER BY title ASC
    """)
    fun searchBooks(query: String): Flow<List<Book>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long


    @Update
    suspend fun updateBook(book: Book)


    @Delete
    suspend fun deleteBook(book: Book)


    @Query("DELETE FROM books WHERE category = :category")
    suspend fun deleteBooksByCategory(category: String)


    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
}