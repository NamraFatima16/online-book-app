package ie.setu.bookapp

import android.app.Application
import androidx.room.Room
import ie.setu.bookapp.database.AppDatabase
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.model.User
import ie.setu.bookapp.repository.BookRepository
import ie.setu.bookapp.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class BookApplication : Application() {
    // Database instance
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "book_app_database"
        )
            .fallbackToDestructiveMigration() // This will destroy and recreate the database if the schema changes
            .build()
    }

    // Repository instances
    val bookRepository by lazy { BookRepository(database.bookDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }

    override fun onCreate() {
        super.onCreate()
        // Initialize Timber for logging
        Timber.plant(Timber.DebugTree())
        Timber.d("BookApplication initialized")

        // Add sample data for testing
        addSampleData()
    }

    private fun addSampleData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Checking if we have data
                val users = userRepository.allUsers.first()
                if (users.isEmpty()) {
                    // Add test user
                    val testUser = User(
                        firstName = "Test",
                        lastName = "User",
                        email = "test@example.com",
                        password = "password"
                    )
                    userRepository.insertUser(testUser)
                    Timber.d("Added test user: ${testUser.email}")

                    // Add sample books
                    val sampleBooks = listOf(
                        Book(title = "The Great Gatsby", author = "F. Scott Fitzgerald", description = "A story about the American Dream", category = "Fiction"),
                        Book(title = "To Kill a Mockingbird", author = "Harper Lee", description = "A story of racial injustice", category = "Fiction"),
                        Book(title = "1984", author = "George Orwell", description = "A dystopian novel", category = "Science Fiction")
                    )

                    sampleBooks.forEach { book ->
                        bookRepository.insertBook(book)
                        Timber.d("Added sample book: ${book.title}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error adding sample data")
            }
        }
    }
}