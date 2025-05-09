package ie.setu.bookapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ie.setu.bookapp.navigation.AppDestinations
import timber.log.Timber

/**
 * Main app scaffold that includes the bottom navigation bar for the main app screens
 * This should be used for Home, Library, and Profile screens that share the bottom navigation
 */
@Composable
fun MainAppScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    // Get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppDestinations.HOME

    // For debugging - print the current route
    Timber.d("Current route in MainAppScaffold: $currentRoute")

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onHomeClick = {
                    Timber.d("Bottom Nav: Navigate to Home")
                    navController.navigate(AppDestinations.HOME) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                onLibraryClick = {
                    Timber.d("Bottom Nav: Navigate to Library")
                    navController.navigate(AppDestinations.LIBRARY) {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    Timber.d("Bottom Nav: Navigate to Profile")
                    navController.navigate(AppDestinations.PROFILE) {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}