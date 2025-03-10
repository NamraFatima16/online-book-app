package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.R
import timber.log.Timber.Forest.i


@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section: Profile Image and Name
        ProfileHeaderSection()

        // Section Buttons (Favourites, Downloads, etc.)
        ProfileSectionButtons()

        // Bottom Navigation
        ProfileBottomNavigation()
    }
}

@Composable
fun ProfileHeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.person_image),
            contentDescription = "Profile Image",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Profile Info (Name and Email)
        Column {
            Text(
                text = "Jeffy Jeffy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "jeffy@example.com",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }

    // Divider line below the header
    Divider(modifier = Modifier.padding(vertical = 16.dp))
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black)
    )
}

@Composable
fun ProfileSectionButtons() {
    val sectionItems = listOf(
        SectionItem("Favourites", R.drawable.fav),
        SectionItem("Downloads", R.drawable.download),
        SectionItem("Languages", R.drawable.languages),
        SectionItem("Location", R.drawable.location),
        SectionItem("Edit Profile", R.drawable.editaccount)
    )

    // Create buttons for each section item
    sectionItems.forEach { section ->
        SectionButton(section)
    }
}

@Composable
fun SectionButton(sectionItem: SectionItem) {
    Button (
        onClick = { /* Handle section item click */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = sectionItem.iconRes),
                contentDescription = sectionItem.name,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = sectionItem.name, fontSize = 16.sp)
        }
    }
}

data class SectionItem(val name: String, val iconRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(iconRes = Icons.Filled.Home, description = "Home")
        BottomNavItem(iconRes = Icons.Filled.Star, description = "Library")
        BottomNavItem(iconRes = Icons.Filled.AccountCircle, description = "Profile")
    }
}

@Composable
fun BottomNavItem(iconRes: ImageVector, description: String) {
   IconButton(onClick = {i("button clicked fromkj profile $description")  }) {
       Icon(imageVector = iconRes, contentDescription = description, modifier = Modifier.size(24.dp), tint = Color.Red)

   }
}

@Preview(showBackground = true)
@Composable
fun PrivewProfile(){

        ProfileScreen()

}