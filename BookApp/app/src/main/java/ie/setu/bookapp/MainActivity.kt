// MainActivity.kt
package ie.setu.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import ie.setu.bookapp.navigation.AppNavGraph
import ie.setu.bookapp.ui.theme.BookAppTheme
import ie.setu.bookapp.viewmodel.BookViewModel
import ie.setu.bookapp.viewmodel.UserViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var bookViewModel: BookViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("MainActivity Created")

        // Initialize ViewModels
        val application = application as BookApplication

        bookViewModel = ViewModelProvider(
            this,
            BookViewModel.BookViewModelFactory(application.bookRepository)
        )[BookViewModel::class.java]

        userViewModel = ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(application.userRepository)
        )[UserViewModel::class.java]

        setContent {
            BookAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        bookViewModel = bookViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}