package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.components.BookCard
import ie.setu.bookapp.components.BottomNavBar
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.navigation.AppDestinations
import ie.setu.bookapp.viewmodel.BookViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    bookViewModel: BookViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample data - in a real app, you would get this from your repository
    val allBooks = remember {
        listOf(
            Book(1, "About Health", "Dr. Smith", "A comprehensive guide to healthy living...", true),
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", true),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", true),
            Book(4, "Body Shot", "Amy Knupp", "A steamy romance novel...", true),
            Book(5, "Anthony Bourdain", "Biography", "The life of a chef and adventurer...", true)
        )
    }

    val favoriteBooks = remember {
        allBooks.filter { it.isFavorite }
    }

    val downloadedBooks = remember {
        listOf(
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", true),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", true)
        )
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All Books", "Downloads", "Favorites")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Library",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Timber.d("Search clicked in Library")
                        /* TODO: Implement Library Search */
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = AppDestinations.LIBRARY,
                onHomeClick = onHomeClick,
                onLibraryClick = { /* Already on Library */ },
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayBooks) { book ->
                    BookCard(
                        book = book,
                        onClick = {
                            Timber.d("Library book clicked: ${book.title}")
                            /* TODO: Navigate to book details */
                        },
                        onFavoriteClick = { isFavorite ->
                            Timber.d("Library book favorite toggled: ${book.title}, isFavorite: $isFavorite")
                            /* TODO: Update book favorite status in repository */
                        }
                    )
                }
            }
        }
    }
}