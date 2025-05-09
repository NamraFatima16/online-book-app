package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.ui.components.BooksGrid
import ie.setu.bookapp.ui.components.EmptyState
import ie.setu.bookapp.ui.components.LoadingState
import ie.setu.bookapp.viewmodel.BookViewModel
import kotlinx.coroutines.delay
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    bookViewModel: BookViewModel,
    onBackClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Sample data for testing
    val allBooks = remember {
        listOf(
            Book(1, "Atomic Habits", "James Clear", "Small changes, remarkable results...", "Self-Help"),
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", "Romance"),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", "Romance"),
            Book(4, "Body Shot", "Amy Knupp", "A steamy romance novel...", "Romance"),
            Book(5, "Milk and Honey", "Rupi Kaur", "A collection of poetry and prose...", "Poetry"),
            Book(6, "Harry Potter", "J.K. Rowling", "The boy who lived comes to Hogwarts...", "Fantasy"),
            Book(7, "The Great Gatsby", "F. Scott Fitzgerald", "The story of the mysteriously wealthy...", "Fiction"),
            Book(8, "The Catcher in the Rye", "J.D. Salinger", "The story of Holden Caulfield...", "Fiction")
        )
    }

    LaunchedEffect(Unit) {
        // Focus the search field when the screen is shown
        focusRequester.requestFocus()
    }

    LaunchedEffect(searchQuery) {
        isSearching = true
        // Add a small delay to avoid too many searches while typing
        delay(300)

        // Perform the search
        if (searchQuery.isBlank()) {
            searchResults = emptyList()
        } else {
            // Filter books using proper null safety
            searchResults = allBooks.filter { book ->
                book.title.contains(searchQuery, ignoreCase = true) ||
                        book.author.contains(searchQuery, ignoreCase = true) ||
                        // Use safe call with description since it's nullable
                        book.description?.contains(searchQuery, ignoreCase = true) ?: false ||
                        book.category.contains(searchQuery, ignoreCase = true)
            }
            Timber.d("Search query: '$searchQuery', found ${searchResults.size} results")
        }
        isSearching = false
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search AppBar
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search books...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isSearching -> {
                    LoadingState()
                }
                searchQuery.isBlank() -> {
                    EmptyState(message = "Enter a search term to find books")
                }
                searchResults.isEmpty() -> {
                    EmptyState(message = "No books found for \"$searchQuery\"")
                }
                else -> {
                    BooksGrid(
                        books = searchResults,
                        onBookClick = onBookClick,
                        onFavoriteClick = { book, isFavorite ->
                            Timber.d("Book favorite toggled from search: ${book.title}, isFavorite: $isFavorite")
                            // In a real app: bookViewModel.toggleFavorite(book)
                        },
                        contentPadding = PaddingValues(16.dp)
                    )
                }
            }
        }
    }
}