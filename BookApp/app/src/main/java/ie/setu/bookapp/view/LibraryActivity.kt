package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.bookapp.R





@Composable
fun LibraryActivity(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        LibraryHeaderSection()

        // Main Content
        LibraryMainContent()

        // Bottom Navigation
        LibraryBottomNavigation()
    }
}

@Composable
fun LibraryHeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "My Library" Heading
        Text(
            text = "My Library",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LibraryMainContent() {
    // LazyVerticalGrid allows scrollability in the grid
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns for the grid
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(30) { index -> // Let's say there are 30 books (you can change this dynamically)
            LibraryGridItem(index)
        }
    }
}

@Composable
fun LibraryGridItem(index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Gray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (index) {
                0 -> "About Health"
                1 -> "It Starts with Us"
                2 -> "It Ends with Us"
                3 -> "Body Shot"
                4 -> "Anthony Bourdain"
                else -> "Harry Potter"
            },
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun LibraryBottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home Button
        IconButton(
            onClick = { /* Navigate to Home */ },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Home",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Library Button
        IconButton(
            onClick = { /* Navigate to Library */ },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.book),
                contentDescription = "Library",
                modifier = Modifier.fillMaxSize()
            )
        }

        // User Profile Button
        IconButton(
            onClick = { /* Navigate to User Profile */ },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.account),
                contentDescription = "User Profile",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
