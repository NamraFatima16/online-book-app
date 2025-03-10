package ie.setu.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.view.LoginActivity
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("MainActivity Created")

        setContent {
              LoginActivity {  }


        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = stringResource(id = R.string.app_name),
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        fontFamily = FontFamily.SansSerif,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier)
}

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MainApp() {
//    var loggedIn by remember { mutableStateOf(false) }
//
//    Scaffold(modifier = Modifier.fillMaxSize()) {
//        // If loggedIn state is true, show LibraryActivity else show LoginActivity
//        if (loggedIn) {
//            // You would render the Library Activity or Home Screen here
//            // For now, we show a simple text that represents a logged-in state
//            LibraryActivity()
//        } else {
//            // Show LoginActivity and pass the onLoginSuccess function
//            LoginActivity(onLoginSuccess = { loggedIn = true })
//        }
//    }
//}
//
//

//@Composable
//fun MainScreen(modifier: Modifier = Modifier) {
//
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(16.dp),
////        horizontalAlignment = Alignment.CenterHorizontally,
////        verticalArrangement = Arrangement.Center
////    ) {
////        Button(onClick = {
////            Timber.d("Navigating to LibraryScreen")
////
////        }) {
////            Text("Go to Library")
////        }
////        Spacer(modifier = Modifier.height(10.dp))
////        Button(onClick = {
////            Timber.d("Navigating to ProfileScreen")
////
////        }) {
////            Text("Go to Profile")
////        }
////    }
//}
