package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
fun HomeScreen(
    bookViewModel: BookViewModel,
    onLibraryClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample book data - in a real app, you would get this from your repository
    val books = remember {
        listOf(
            Book(1, "Atomic Habits", "James Clear", "Small changes, remarkable results...", false),
            Book(2, "It Starts With Us", "Colleen Hoover", "The sequel to It Ends With Us...", false),
            Book(3, "It Ends With Us", "Colleen Hoover", "A brave and heartbreaking novel...", false),
            Book(4, "Body Shot", "Amy Knupp", "A steamy romance novel...", false),
            Book(5, "Milk and Honey", "Rupi Kaur", "A collection of poetry and prose...", false),
            Book(6, "Harry Potter", "J.K. Rowling", "The boy who lived comes to Hogwarts...", false),
            Book(7, "The Great Gatsby", "F. Scott Fitzgerald", "The story of the mysteriously wealthy...", false),
            Book(8, "The Catcher in the Rye", "J.D. Salinger", "The story of Holden Caulfield...", false),
            Book(9, "To Kill a Mockingbird", "Harper Lee", "A story of race and class...", false)
        )
    }

    var selectedCategory by remember { mutableStateOf("Fiction") }
    val categories = listOf("Fiction", "Non-Fiction", "Poetry", "Romance", "Mystery", "Sci-Fi")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CoverToCover",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Timber.d("Search clicked")
                        /* TODO: Implement Search */
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {
                        Timber.d("Filter clicked")
                        /* TODO: Implement Filter */
                    }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = AppDestinations.HOME,
                onHomeClick = { /* Already on Home */ },
                onLibraryClick = onLibraryClick,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Categories Section
            Text(
                text = "Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Category Chips
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    SegmentedButton(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            Timber.d("Category selected: $category")
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = categories.size
                        )
                    ) {
                        Text(category)
                    }
                }
            }

            // Books Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(books) { book ->
                    BookCard(
                        book = book,
                        onClick = {
                            Timber.d("Book clicked: ${book.title}")
                            /* TODO: Navigate to book details */
                        },
                        onFavoriteClick = { isFavorite ->
                            Timber.d("Book favorite toggled: ${book.title}, isFavorite: $isFavorite")
                            /* TODO: Update book favorite status in repository */
                        }
                    )
                }
            }
        }
    }
}