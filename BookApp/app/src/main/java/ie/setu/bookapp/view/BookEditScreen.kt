package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.model.Book
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.ui.components.PrimaryButton
import ie.setu.bookapp.viewmodel.BookViewModel
import ie.setu.bookapp.viewmodel.OperationStatus
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditScreen(
    bookId: Int,
    bookViewModel: BookViewModel,
    onSaveComplete: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val book by bookViewModel.getBookById(bookId).collectAsState(initial = null)
    val operationStatus by bookViewModel.operationStatus.collectAsState()

    val isNewBook = bookId <= 0

    // Book fields state
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Fiction") }
    var publisher by remember { mutableStateOf("") }
    var publishedDate by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }

    // Category options
    val categories = listOf("Fiction", "Non-Fiction", "Poetry", "Romance", "Fantasy", "Self-Help", "Biography", "Science Fiction", "Mystery", "Thriller")

    // Initialize fields with existing book data
    LaunchedEffect(book) {
        book?.let { existingBook ->
            title = existingBook.title
            author = existingBook.author
            description = existingBook.description ?: ""
            category = existingBook.category
            publisher = existingBook.publisher ?: ""
            publishedDate = existingBook.publishedDate ?: ""
            pageCount = existingBook.pageCount?.toString() ?: ""
            isbn = existingBook.isbn ?: ""
            language = existingBook.language ?: ""
        }
    }

    // Handle operation status
    LaunchedEffect(operationStatus) {
        if (operationStatus is OperationStatus.Success) {
            onSaveComplete()
        }
    }

    // Show confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to delete this book? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        book?.let { bookViewModel.deleteBook(it) }
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        AppTopBar(
            title = if (isNewBook) "Add Book" else "Edit Book",
            showBackButton = true,
            onBackClick = onBackClick
        )

        // Form content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isEmpty()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Author
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth(),
                isError = author.isEmpty()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { /* TODO */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { /* Read-only */ },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Category") }
                )

                DropdownMenu(
                    expanded = false,
                    onDismissRequest = { /* TODO */ },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    categories.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                category = option
                                /* TODO: close dropdown */
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Publisher
            OutlinedTextField(
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("Publisher") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Published Date
            OutlinedTextField(
                value = publishedDate,
                onValueChange = { publishedDate = it },
                label = { Text("Published Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Page Count
            OutlinedTextField(
                value = pageCount,
                onValueChange = { pageCount = it },
                label = { Text("Page Count") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ISBN
            OutlinedTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = { Text("ISBN") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Language
            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                label = { Text("Language") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            PrimaryButton(
                text = if (isNewBook) "Add Book" else "Save Changes",
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank() && category.isNotBlank()) {
                        val updatedBook = Book(
                            id = if (isNewBook) 0 else bookId,
                            title = title.trim(),
                            author = author.trim(),
                            description = description.trim().ifBlank { null },
                            category = category,
                            publisher = publisher.trim().ifBlank { null },
                            publishedDate = publishedDate.trim().ifBlank { null },
                            pageCount = pageCount.trim().toIntOrNull(),
                            isbn = isbn.trim().ifBlank { null },
                            language = language.trim().ifBlank { null },
                            isFavorite = book?.isFavorite ?: false,
                            isDownloaded = book?.isDownloaded ?: false,
                            dateAdded = book?.dateAdded ?: System.currentTimeMillis(),
                            lastModified = System.currentTimeMillis()
                        )

                        if (isNewBook) {
                            bookViewModel.addBook(updatedBook)
                        } else {
                            bookViewModel.updateBook(updatedBook)
                        }
                    } else {
                        // Show error for required fields
                        Timber.d("Required fields are empty")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Delete Button (only for existing books)
            if (!isNewBook) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete Book")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}