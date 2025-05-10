package ie.setu.bookapp.view

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import ie.setu.bookapp.R
import ie.setu.bookapp.ui.components.FormInputField
import ie.setu.bookapp.ui.components.PrimaryButton
import ie.setu.bookapp.viewmodel.UserViewModel
import timber.log.Timber

@Composable
fun LoginScreen(
    userViewModel: UserViewModel?,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("test@example.com") }
    var password by remember { mutableStateOf("password") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Configure Google Sign In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Timber.d("Google sign-in success: ${account.email}")
                userViewModel?.signInWithGoogle(account)
                onLoginSuccess()
            } catch (e: ApiException) {
                Timber.e(e, "Google sign-in failed")
                errorMessage = "Google sign-in failed: ${e.message}"
            }
        } else {
            Timber.d("Google sign-in cancelled")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.ctc),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        // App Title
        Text(
            text = "CoverToCover",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Email Input
        FormInputField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            isEmail = true,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Password Input
        FormInputField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Login Button
        PrimaryButton(
            text = "Login",
            onClick = {
                Timber.d("Login attempt with: $email")
                isLoading = true
                userViewModel?.login(email, password)
                onLoginSuccess()
            },
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign In Button
        OutlinedButton(
            onClick = {
                Timber.d("Google sign-in attempt")
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        Text(
            text = "Don't have an account? Sign Up",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onSignUpClick() }
        )
    }
}