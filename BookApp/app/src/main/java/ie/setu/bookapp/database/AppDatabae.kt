package ie.setu.bookapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.model.User


@Database(
    entities = [Book::class, User::class],
    version = 2, // Increment version number to handle schema changes
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    abstract fun userDao(): UserDao
}