package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.R
import androidx.compose.foundation.clickable


@Composable
fun SignUpActivity() {
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

        // First Name Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle First Name Change */ },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            // add colour
        )

        // Last Name Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle Last Name Change */ },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            // add colour
        )

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
            // add colour
        )

        // Password Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle Password Change */ },
            label = { Text("Enter Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            // add colour
        )

        // Confirm Password Input Field
        TextField(
            value = "",
            onValueChange = { /* Handle Confirm Password Change */ },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            // add colour
        )

        // Button for creating account
        Button(
            onClick = { /* Handle Sign Up Submit */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(text = "Create Account")
        }

        // Login button (for users who already have an account)
        Text(
            text = "Already have an account? Login",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
                .clickable {
                    // Handle navigation to login screen
                }
        )
    }
}
