package ie.setu.bookapp.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ie.setu.bookapp.model.BookstoreLocation
import ie.setu.bookapp.ui.components.AppTopBar
import ie.setu.bookapp.viewmodel.MapViewModel
import timber.log.Timber

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    onBackClick: () -> Unit,
    onBookstoreClick: (BookstoreLocation) -> Unit,
    onAddBookstoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookstores by mapViewModel.bookstores.collectAsState()
    val isLoading by mapViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Check if we have location permission
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(53.3498, -6.2603), 10f) // Default to Dublin
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        )
    }

    LaunchedEffect(bookstores) {
        if (bookstores.isNotEmpty()) {
            // Build bounds for all bookstores to ensure they're visible
            val boundsBuilder = LatLngBounds.builder()
            bookstores.forEach { bookstore ->
                boundsBuilder.include(LatLng(bookstore.latitude, bookstore.longitude))
            }

            try {
                val bounds = boundsBuilder.build()
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngBounds(bounds, 100)
                )
            } catch (e: IllegalStateException) {
                // Handle case with no bookstores or invalid bounds
                Timber.e(e, "Error setting map bounds")
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Bookstore Map",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddBookstoreClick() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bookstore")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                onMapLoaded = {
                    if (!hasLocationPermission) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            ) {
                // Add markers for bookstores
                bookstores.forEach { bookstore ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(bookstore.latitude, bookstore.longitude)
                        ),
                        title = bookstore.name,
                        snippet = bookstore.address,
                        onInfoWindowClick = {
                            onBookstoreClick(bookstore)
                        }
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}