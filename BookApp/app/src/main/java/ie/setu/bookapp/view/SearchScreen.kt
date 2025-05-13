package ie.setu.bookapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.ui.components.BookCard
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
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // State for category filter
    var showCategoryFilter by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("All", "Fiction", "Non-Fiction", "Poetry", "Romance", "Fantasy", "Self-Help")

    // Collect books from ViewModel
    val allBooks by bookViewModel.allBooks.collectAsState()
    var searchResults by remember { mutableStateOf<List<Book>>(emptyList()) }

    // Effect to perform search
    LaunchedEffect(searchQuery, selectedCategory) {
        isSearching = true
        delay(300) // Debounce

        if (searchQuery.isBlank() && selectedCategory == null) {
            searchResults = emptyList()
        } else {
            var results = if (searchQuery.isBlank()) {
                allBooks
            } else {
                allBooks.filter { book ->
                    book.title.contains(searchQuery, ignoreCase = true) ||
                            book.author.contains(searchQuery, ignoreCase = true) ||
                            (book.description?.contains(searchQuery, ignoreCase = true) ?: false) ||
                            book.category.contains(searchQuery, ignoreCase = true)
                }
            }

            // Apply category filter if selected
            if (selectedCategory != null && selectedCategory != "All") {
                results = results.filter { it.category == selectedCategory }
            }

            searchResults = results
            Timber.d("Search query: '$searchQuery', category: $selectedCategory, found ${results.size} results")
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
            },
            actions = {
                IconButton(onClick = { showCategoryFilter = !showCategoryFilter }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter by Category"
                    )
                }
            }
        )

        // Category filter chips
        AnimatedVisibility(visible = showCategoryFilter) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = {
                            selectedCategory = if (category == selectedCategory) null else category
                        },
                        label = { Text(category) }
                    )
                }
            }
        }

        // Content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isSearching -> {
                    LoadingState()
                }
                searchQuery.isBlank() && selectedCategory == null -> {
                    EmptyState(message = "Enter a search term or select a category to find books")
                }
                searchResults.isEmpty() -> {
                    EmptyState(message = "No books found for your search criteria")
                }
                else -> {
                    // Display search results in a list or grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(searchResults) { book ->
                            BookCard(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onFavoriteClick = { isFavorite ->
                                    bookViewModel.toggleFavorite(book)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}