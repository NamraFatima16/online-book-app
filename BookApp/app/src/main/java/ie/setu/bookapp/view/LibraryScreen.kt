package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.ui.components.BooksGrid
import ie.setu.bookapp.viewmodel.BookViewModel
import timber.log.Timber

@Composable
fun LibraryScreen(
    bookViewModel: BookViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample data
    val allBooks = remember {
        listOf(
            Book(1, "About Health", "Dr. Smith", "A comprehensive guide to healthy living...", "Health", true),
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", "Romance", true),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", "Romance", true),
            Book(4, "Body Shot", "Amy Knupp", "A steamy romance novel...", "Romance", true),
            Book(5, "Anthony Bourdain", "Biography", "The life of a chef and adventurer...", "Biography", true)
        )
    }

    val favoriteBooks = remember {
        allBooks.filter { it.isFavorite }
    }

    val downloadedBooks = remember {
        listOf(
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", "Romance", true),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", "Romance", true)
        )
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All Books", "Downloads", "Favorites")

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top App Bar
        AppTopBar(
            title = "My Library",
            showSearchButton = true
        )

        // Library Category Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        Timber.d("Library tab selected: $title")
                    },
                    text = { Text(title) }
                )
            }
        }

        // Library Books Grid based on selected tab
        val displayBooks = when (selectedTabIndex) {
            0 -> allBooks
            1 -> downloadedBooks
            2 -> favoriteBooks
            else -> allBooks
        }

        BooksGrid(
            books = displayBooks,
            onBookClick = { bookId ->
                Timber.d("Book clicked from library: $bookId")
                onBookClick(bookId)
            },
            onFavoriteClick = { book, isFavorite ->
                Timber.d("Library book favorite toggled: ${book.title}, isFavorite: $isFavorite")
                // In a real app with actual data: bookViewModel.toggleFavorite(book)
            },
            contentPadding = PaddingValues(16.dp)
        )
    }
}