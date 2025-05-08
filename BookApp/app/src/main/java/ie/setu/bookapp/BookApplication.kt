package ie.setu.bookapp

import android.app.Application
import androidx.room.Room
import ie.setu.bookapp.database.AppDatabase
import ie.setu.bookapp.repository.BookRepository
import ie.setu.bookapp.repository.UserRepository
import timber.log.Timber

class BookApplication : Application() {
    // Database instance using lazy initialization
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "book_database"
        ).build()
    }

    // Repository instances
    val bookRepository by lazy { BookRepository(database.bookDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }

    override fun onCreate() {
        super.onCreate()
        // Initialize Timber for logging
        Timber.plant(Timber.DebugTree())
        Timber.d("BookApplication initialized")
    }
}