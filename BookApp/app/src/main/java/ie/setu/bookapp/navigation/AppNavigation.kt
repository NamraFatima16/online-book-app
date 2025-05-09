package ie.setu.bookapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ie.setu.bookapp.BookApplication
import ie.setu.bookapp.ui.components.MainAppScaffold
import ie.setu.bookapp.view.BookDetailsScreen
import ie.setu.bookapp.view.HomeScreen
import ie.setu.bookapp.view.LibraryScreen
import ie.setu.bookapp.view.LoginScreen
import ie.setu.bookapp.view.ProfileEditScreen
import ie.setu.bookapp.view.ProfileScreen
import ie.setu.bookapp.view.RegisterScreen
import ie.setu.bookapp.view.SearchScreen
import ie.setu.bookapp.view.SignUpScreen
import ie.setu.bookapp.viewmodel.BookViewModel
import ie.setu.bookapp.viewmodel.UserViewModel
import timber.log.Timber

@Composable
fun AppNavigation(
    application: BookApplication,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.LOGIN
) {
    // Create ViewModels with their factories
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModel.UserViewModelFactory(application.userRepository)
    )

    val bookViewModel: BookViewModel = viewModel(
        factory = BookViewModel.BookViewModelFactory(application.bookRepository)
    )

    // For development testing, we'll start directly at HOME
    // Change this back to: startDestination = if (isLoggedIn) AppDestinations.HOME else startDestination
    // when you want the actual login flow to work

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ) {
        // Login Screen
        composable(AppDestinations.LOGIN) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    Timber.d("Login successful, navigating to Home")
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.LOGIN) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    Timber.d("Navigate to Sign Up")
                    navController.navigate(AppDestinations.REGISTER)
                }
            )
        }

        // Register Screen
        composable(AppDestinations.REGISTER) {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    Timber.d("Registration successful, navigating to Home")
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.REGISTER) { inclusive = true }
                    }
                },
                onBackClick = {
                    Timber.d("Back to Login")
                    navController.popBackStack()
                }
            )
        }

        // Alternative Sign Up Screen
        composable(AppDestinations.SIGN_UP) {
            SignUpScreen(
                userViewModel = userViewModel,
                onSignUpSuccess = {
                    Timber.d("Sign up successful, navigating to Home")
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.SIGN_UP) { inclusive = true }
                    }
                },
                onLoginClick = {
                    Timber.d("Back to Login")
                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(AppDestinations.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(AppDestinations.HOME) {
            MainAppScaffold(navController = navController) { innerPadding ->
                HomeScreen(
                    bookViewModel = bookViewModel,
                    onLibraryClick = {
                        Timber.d("Navigate to Library")
                        navController.navigate(AppDestinations.LIBRARY)
                    },
                    onProfileClick = {
                        Timber.d("Navigate to Profile")
                        navController.navigate(AppDestinations.PROFILE)
                    },
                    onSearchClick = {
                        Timber.d("Navigate to Search")
                        navController.navigate(AppDestinations.SEARCH)
                    },
                    onBookClick = { bookId ->
                        Timber.d("Navigate to Book Details for book: $bookId")
                        navController.navigate(AppDestinations.bookDetailsRoute(bookId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Library Screen
        composable(AppDestinations.LIBRARY) {
            MainAppScaffold(navController = navController) { innerPadding ->
                LibraryScreen(
                    bookViewModel = bookViewModel,
                    onHomeClick = {
                        Timber.d("Navigate to Home")
                        navController.navigate(AppDestinations.HOME) {
                            // Avoid creating multiple instances of the home screen
                            popUpTo(AppDestinations.HOME) { inclusive = true }
                        }
                    },
                    onProfileClick = {
                        Timber.d("Navigate to Profile")
                        navController.navigate(AppDestinations.PROFILE)
                    },
                    onBookClick = { bookId ->
                        Timber.d("Navigate to Book Details for book: $bookId")
                        navController.navigate(AppDestinations.bookDetailsRoute(bookId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Profile Screen
        composable(AppDestinations.PROFILE) {
            MainAppScaffold(navController = navController) { innerPadding ->
                ProfileScreen(
                    userViewModel = userViewModel,
                    onHomeClick = {
                        Timber.d("Navigate to Home")
                        navController.navigate(AppDestinations.HOME) {
                            // Avoid creating multiple instances of the home screen
                            popUpTo(AppDestinations.HOME) { inclusive = true }
                        }
                    },
                    onLibraryClick = {
                        Timber.d("Navigate to Library")
                        navController.navigate(AppDestinations.LIBRARY)
                    },
                    onEditProfileClick = {
                        Timber.d("Navigate to Profile Edit")
                        navController.navigate(AppDestinations.PROFILE_EDIT)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Profile Edit Screen
        composable(AppDestinations.PROFILE_EDIT) {
            ProfileEditScreen(
                userViewModel = userViewModel,
                onBackClick = {
                    Timber.d("Back to Profile")
                    navController.popBackStack()
                }
            )
        }

        // Book Details Screen
        composable(
            route = AppDestinations.BOOK_DETAILS,
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: -1
            BookDetailsScreen(
                bookId = bookId,
                bookViewModel = bookViewModel,
                onBackClick = {
                    Timber.d("Back from Book Details")
                    navController.popBackStack()
                }
            )
        }

        // Search Screen
        composable(AppDestinations.SEARCH) {
            SearchScreen(
                bookViewModel = bookViewModel,
                onBackClick = {
                    Timber.d("Back from Search")
                    navController.popBackStack()
                },
                onBookClick = { bookId ->
                    Timber.d("Navigate to Book Details from Search for book: $bookId")
                    navController.navigate(AppDestinations.bookDetailsRoute(bookId))
                }
            )
        }

        // Category Screen
        composable(
            route = AppDestinations.CATEGORY,
            arguments = listOf(
                navArgument("categoryName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Fiction"
            // Since we don't have a CategoryScreen yet, we'll just navigate back
            // You can implement this screen later
            LaunchedEffect(Unit) {
                Timber.d("Category screen for $categoryName not implemented yet")
                navController.popBackStack()
            }
        }
    }
}