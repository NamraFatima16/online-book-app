package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import ie.setu.bookapp.ui.components.CategorySelector
import ie.setu.bookapp.ui.components.SectionHeader
import ie.setu.bookapp.viewmodel.BookViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    bookViewModel: BookViewModel,
    onLibraryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample book data
    val books = remember {
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

    // Categories
    var selectedCategory by remember { mutableStateOf("Fiction") }
    val categories = listOf("Fiction", "Non-Fiction", "Poetry", "Romance", "Fantasy", "Self-Help")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        AppTopBar(
            title = "CoverToCover",
            showSearchButton = true,
            showFilterButton = true,
            onSearchClick = onSearchClick
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Categories Section
            SectionHeader(title = "Categories")

            // Category Selector
            CategorySelector(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category

                }
            )

            // Books Grid
            BooksGrid(
                books = books,
                onBookClick = onBookClick,
                onFavoriteClick = { book, isFavorite ->
                    Timber.d("Book favorite toggled: ${book.title}, isFavorite: $isFavorite")

                },
                contentPadding = PaddingValues(bottom = 16.dp)
            )
        }
    }
}