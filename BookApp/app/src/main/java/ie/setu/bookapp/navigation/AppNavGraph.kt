package ie.setu.bookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.bookapp.view.LibraryActivity
import ie.setu.bookapp.view.SignUpActivity

//@Composable
//fun NavHostProvider(
//    modifier: Modifier,
//    navController: NavHostController,
//    paddingValues: PaddingValues
//) {
//    NavHost(
//        navController = navController,
//        startDestination = AppDestinations.SIGN_UP,
//        modifier = Modifier.padding(paddingValues = paddingValues)
//    ) {
//        composable(route = AppDestinations.SIGN_UP) {
//            SignUpActivity(navController = navController) // Calling the SignUp Screen
//        }
//        composable(route = AppDestinations.LIBRARY) {
//            LibraryActivity() // Calling the Library Screen
//        }
//    }
//}
