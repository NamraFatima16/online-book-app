package ie.setu.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ie.setu.bookapp.navigation.AppNavigation
import ie.setu.bookapp.ui.theme.BookAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Timber for logging if not already done in Application class
        if (Timber.forest().isEmpty()) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("MainActivity created")

        setContent {
            BookAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val application = applicationContext as BookApplication


                    AppNavigation(application = application)
                }
            }
        }
    }
}