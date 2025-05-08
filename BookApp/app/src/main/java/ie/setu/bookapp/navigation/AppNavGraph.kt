// AppNavGraph.kt
package ie.setu.bookapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.bookapp.view.*
import ie.setu.bookapp.viewmodel.BookViewModel
import ie.setu.bookapp.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN
    ) {
        composable(AppDestinations.LOGIN) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = { navController.navigate(AppDestinations.HOME) },
                onSignUpClick = { navController.navigate(AppDestinations.SIGN_UP) }
            )
        }

        composable(AppDestinations.SIGN_UP) {
            SignUpScreen(
                userViewModel = userViewModel,
                onSignUpSuccess = { navController.navigate(AppDestinations.HOME) },
                onLoginClick = { navController.navigate(AppDestinations.LOGIN) }
            )
        }

        composable(AppDestinations.HOME) {
            HomeScreen(
                bookViewModel = bookViewModel,
                onLibraryClick = { navController.navigate(AppDestinations.LIBRARY) },
                onProfileClick = { navController.navigate(AppDestinations.PROFILE) }
            )
        }

        composable(AppDestinations.LIBRARY) {
            LibraryScreen(
                bookViewModel = bookViewModel,
                onHomeClick = { navController.navigate(AppDestinations.HOME) },
                onProfileClick = { navController.navigate(AppDestinations.PROFILE) }
            )
        }

        composable(AppDestinations.PROFILE) {
            ProfileScreen(
                userViewModel = userViewModel,
                onHomeClick = { navController.navigate(AppDestinations.HOME) },
                onLibraryClick = { navController.navigate(AppDestinations.LIBRARY) },
                onEditProfileClick = { navController.navigate(AppDestinations.PROFILE_EDIT) }
            )
        }

        composable(AppDestinations.PROFILE_EDIT) {
            ProfileEditScreen(
                userViewModel = userViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}