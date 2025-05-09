package ie.setu.bookapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.ui.components.ProfileHeader
import ie.setu.bookapp.ui.components.SecondaryButton
import ie.setu.bookapp.viewmodel.UserViewModel
import timber.log.Timber

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onHomeClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        AppTopBar(
            title = "Profile"
        )

        // Profile Header
        ProfileHeader(
            firstName = "Jeffy",
            lastName = "Jeffy",
            email = "jeffy@example.com"
        )

        Divider()

        // Profile Actions
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Favorites Button
            SecondaryButton(
                text = "Favorites",
                onClick = {
                    Timber.d("Favorites clicked")
                    /* TODO: Navigate to Favorites */
                },
                leadingIcon = Icons.Default.Favorite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Downloads Button
            SecondaryButton(
                text = "Downloads",
                onClick = {
                    Timber.d("Downloads clicked")
                    /* TODO: Navigate to Downloads */
                },
                leadingIcon = Icons.Default.Bookmark
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
        }
    }
}