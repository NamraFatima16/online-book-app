package ie.setu.bookapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.model.Book
import timber.log.Timber

@Composable
fun BooksGrid(
    books: List<Book>,
    onBookClick: (Int) -> Unit,
    onFavoriteClick: (Book, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 16.dp),
    columns: Int = 2
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                onClick = {
                    Timber.d("Book clicked: ${book.title}")
                    onBookClick(book.id)
                },
                onFavoriteClick = { isFavorite ->
                    Timber.d("Book favorite toggled: ${book.title}, isFavorite: $isFavorite")
                    onFavoriteClick(book, isFavorite)
                }
            )
        }
    }
}