package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.R

@Composable
fun ProfileEditScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Back Button
        BackButton()

        // Profile Image and Edit Icon
        ProfileImageSection()

        // Form Fields (First Name, Last Name, Email, Phone Number)
        ProfileForm()

        // Save Changes Button
        SaveChangesButton()
    }
}

@Composable
fun BackButton() {
    IconButton(
        onClick = { /* Handle back action */ },
        modifier = Modifier.size(48.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bactbutton),
            contentDescription = "Back",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProfileImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.person_image),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, CircleShape)
        )

        // Edit Icon on Profile Image
        Image(
            painter = painterResource(id = R.drawable.edit),
            contentDescription = "Edit Profile",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(2.dp)
                .size(20.dp)
        )
    }
}

@Composable
fun ProfileForm() {
    Column(modifier = Modifier.fillMaxWidth()) {

        // First Name
        ProfileTextField(label = "First Name", hint = "First Name")

        // Last Name
        ProfileTextField(label = "Last Name", hint = "Last Name")

        // Email
        ProfileTextField(label = "Email", hint = "Email")

        // Phone Number
        ProfileTextField(label = "Phone Number", hint = "Phone Number")
    }
}

@Composable
fun ProfileTextField(label: String, hint: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 16.sp)

        // Text field for input
        BasicTextField(
            value = TextFieldValue(""),
            onValueChange = { /* Handle text change */ },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.1f), shape = CircleShape)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun SaveChangesButton() {
    Button(
        onClick = { /* Handle save action */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
    ) {
        Text(text = "Save Changes", fontSize = 16.sp)
    }
}
