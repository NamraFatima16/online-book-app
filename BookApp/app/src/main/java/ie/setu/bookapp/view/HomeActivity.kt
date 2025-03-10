package ie.setu.bookapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
fun HomeActivity() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Home Header Section
        HomeHeaderSection()

        // Home Main Content
        HomeMainContent()

        // Home Bottom Navigation
        HomeBottomNavigation()
    }
}

@Composable
fun HomeHeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title of the app
        Text(
            text = "CoverToCover",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        // Sort and Filter buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { /* Handle Sort */ },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Sort")
            }

            Button(
                onClick = { /* Handle Filter */ },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Filter")
            }
        }
    }
}

@Composable
fun HomeMainContent() {
    // Use LazyVerticalGrid for a grid layout that can scroll
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns for the grid
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dynamically create items for the grid
        items(30) { index -> // Let's say there are 30 books (you can change this dynamically)
            HomeGridItem(index)
        }
    }
}

@Composable
fun HomeGridItem(index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Gray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (index) {
                0 -> "Atomic habits"
                1 -> "It starts with us"
                2 -> "It ends with us"
                3 -> "Body shot"
                4 -> "Milk and honey"
                5 -> "Harry Potter"
                6 -> "The Great Gatsby"
                7 -> "The Catcher in the Rye"
                8 -> "To Kill a Mockingbird"
                // You can add more titles here...
                else -> "Book Title $index" // Default name for other books
            },
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun HomeBottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home button
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

        // Library button
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

        // User Profile button
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
