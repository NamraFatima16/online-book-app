package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.R
import ie.setu.bookapp.ui.theme.BookAppTheme
import timber.log.Timber

@Composable
fun LoginActivity(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loggedIn by remember { mutableStateOf(false) }

    // Check if loggedIn state changes, invoke onLoginSuccess when true
    if (loggedIn) {
        onLoginSuccess()  // Trigger navigation or success logic
        Timber.i("Login Success")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ctc),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 20.dp)
        )

        // Login and Sign Up Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Login Button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Handle Login */ },
                    modifier = Modifier.background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(text = "Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(3.dp)
                        .background(Color(0xFF6200EE)) // Replace with your purple color
                )
            }

            // Sign Up Button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Handle Sign Up */ },
                    modifier = Modifier.background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(),
                ) {
                    Text(text = "Sign Up", fontSize = 18.sp, color = Color.Black)
                }
            }
        }

        // Email Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle Email Change */ },
            label = { Text("Enter Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            //add colour maybe???  colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp)
        )

        // Password Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle Password Change */ },
            label = { Text("Enter Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(8.dp)
            //add colour fix this issue with colour  colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
        )

        // Login Submit Button
        Button(
            onClick = {   },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(text = "Login")
        }

        // Forgot Password Text
        Text(
            text = "Forgot Password?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginActivity(){
    BookAppTheme {
      LoginActivity(
          modifier = Modifier,
          onLoginSuccess = {true}
      )
    }
}
