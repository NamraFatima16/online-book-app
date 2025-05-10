package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.ui.components.ProfileHeader
import ie.setu.bookapp.ui.components.SecondaryButton
import ie.setu.bookapp.viewmodel.BookViewModel
import ie.setu.bookapp.viewmodel.UserViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    bookViewModel: BookViewModel,
    onHomeClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val allBooks by bookViewModel.allBooks.collectAsState()
    val favoriteBooks by bookViewModel.favoriteBooks.collectAsState()
    val downloadedBooks by bookViewModel.downloadedBooks.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Profile"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            currentUser?.let { user ->
                ProfileHeader(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email
                )
            }

            Divider()

            // Book Statistics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your Book Statistics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatisticItem(
                            icon = Icons.Default.MenuBook,
                            label = "Total Books",
                            value = allBooks.size.toString(),
                            modifier = Modifier.weight(1f)
                        )

                        StatisticItem(
                            icon = Icons.Default.Favorite,
                            label = "Favorites",
                            value = favoriteBooks.size.toString(),
                            modifier = Modifier.weight(1f)
                        )

                        StatisticItem(
                            icon = Icons.Default.Download,
                            label = "Downloads",
                            value = downloadedBooks.size.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Most popular category
                    val categoryCount = allBooks.groupBy { it.category }
                        .mapValues { it.value.size }

                    val topCategory = categoryCount.maxByOrNull { it.value }

                    topCategory?.let {
                        Text(
                            text = "Most Read Category: ${it.key} (${it.value} books)",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Profile Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Favorites Button
                SecondaryButton(
                    text = "Favorites",
                    onClick = {
                        Timber.d("Favorites clicked")
                        onLibraryClick()
                    },
                    leadingIcon = Icons.Default.Favorite
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Downloads Button
                SecondaryButton(
                    text = "Downloads",
                    onClick = {
                        Timber.d("Downloads clicked")
                        onLibraryClick()
                    },
                    leadingIcon = Icons.Default.Download
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Edit Profile Button
                SecondaryButton(
                    text = "Edit Profile",
                    onClick = {
                        Timber.d("Edit Profile clicked")
                        onEditProfileClick()
                    },
                    leadingIcon = Icons.Default.Edit
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = {
                        Timber.d("Logout clicked")
                        userViewModel.logout()
                        onLogoutClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun StatisticItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}