// BottomNavBar.kt
package ie.setu.bookapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ie.setu.bookapp.navigation.AppDestinations
import timber.log.Timber

@Composable
fun BottomNavBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == AppDestinations.HOME,
            onClick = {
                Timber.d("Navigation: Home")
                onHomeClick()
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == AppDestinations.LIBRARY,
            onClick = {
                Timber.d("Navigation: Library")
                onLibraryClick()
            },
            icon = { Icon(Icons.Default.Book, contentDescription = "Library") },
            label = { Text("Library") }
        )
        NavigationBarItem(
            selected = currentRoute == AppDestinations.PROFILE,
            onClick = {
                Timber.d("Navigation: Profile")
                onProfileClick()
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}