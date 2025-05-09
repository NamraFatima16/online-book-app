package ie.setu.bookapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.ui.components.LoadingState
import ie.setu.bookapp.ui.components.PrimaryButton
import ie.setu.bookapp.viewmodel.BookViewModel
import timber.log.Timber

@Composable
fun BookDetailsScreen(
    bookId: Int,
    bookViewModel: BookViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val book by bookViewModel.getBookById(bookId).collectAsState(initial = null)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        AppTopBar(
            title = "Book Details",
            showBackButton = true,
            onBackClick = onBackClick
        )

        if (book == null) {
            // Loading state
            LoadingState()
        } else {
            // Book details content
            book?.let { bookDetails ->
                BookDetailsContent(
                    title = bookDetails.title,
                    author = bookDetails.author,
                    category = bookDetails.category,
                    description = bookDetails.description ?: "No description available.",
                    isFavorite = bookDetails.isFavorite,
                    onFavoriteClick = { isFavorite ->
                        Timber.d("Favorite toggled: ${bookDetails.title}, isFavorite: $isFavorite")
                        bookViewModel.toggleFavorite(bookDetails)
                    }
                )
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    title: String,
    author: String,
    category: String,
    description: String,
    isFavorite: Boolean,
    onFavoriteClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var favoriteState by remember { mutableStateOf(isFavorite) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Book Cover
        val colorList = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
        val coverColor = colorList[title.length % colorList.size]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(coverColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.first().toString(),
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // Book Title and Favorite Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    favoriteState = !favoriteState
                    onFavoriteClick(favoriteState)
                }
            ) {
                Icon(
                    imageVector = if (favoriteState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (favoriteState) "Remove from favorites" else "Add to favorites",
                    tint = if (favoriteState) Color.Red else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Author
        Text(
            text = "By $author",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Category
        Text(
            text = "Category: $category",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Description
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = description,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Download Button
        PrimaryButton(
            text = "Download Book",
            onClick = {
                Timber.d("Download button clicked for: $title")
                // In a real app, you would call bookViewModel.toggleDownload(book)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}