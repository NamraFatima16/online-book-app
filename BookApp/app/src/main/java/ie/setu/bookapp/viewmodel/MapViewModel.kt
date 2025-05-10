package ie.setu.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import ie.setu.bookapp.model.BookstoreLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class MapViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val bookstoresCollection = firestore.collection("bookstores")

    private val _bookstores = MutableStateFlow<List<BookstoreLocation>>(emptyList())
    val bookstores: StateFlow<List<BookstoreLocation>> = _bookstores

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBookstores()
    }

    fun loadBookstores() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = bookstoresCollection.get().await()
                val bookstoresList = snapshot.documents.mapNotNull { document ->
                    try {
                        val id = document.getLong("id")?.toInt() ?: 0
                        val name = document.getString("name") ?: ""
                        val address = document.getString("address") ?: ""
                        val latitude = document.getDouble("latitude") ?: 0.0
                        val longitude = document.getDouble("longitude") ?: 0.0
                        val description = document.getString("description")
                        val website = document.getString("website")
                        val phoneNumber = document.getString("phoneNumber")
                        val associatedBookId = document.getLong("associatedBookId")?.toInt()
                        val dateAdded = document.getLong("dateAdded") ?: System.currentTimeMillis()

                        BookstoreLocation(
                            id = id,
                            name = name,
                            address = address,
                            latitude = latitude,
                            longitude = longitude,
                            description = description,
                            website = website,
                            phoneNumber = phoneNumber,
                            associatedBookId = associatedBookId,
                            dateAdded = dateAdded
                        )
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing bookstore document")
                        null
                    }
                }

                _bookstores.value = bookstoresList
                Timber.d("Loaded ${bookstoresList.size} bookstores from Firestore")
            } catch (e: Exception) {
                Timber.e(e, "Error loading bookstores from Firestore")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBookstore(bookstore: BookstoreLocation) {
        viewModelScope.launch {
            try {
                val bookstoreData = mapOf(
                    "name" to bookstore.name,
                    "address" to bookstore.address,
                    "latitude" to bookstore.latitude,
                    "longitude" to bookstore.longitude,
                    "description" to bookstore.description,
                    "website" to bookstore.website,
                    "phoneNumber" to bookstore.phoneNumber,
                    "associatedBookId" to bookstore.associatedBookId,
                    "dateAdded" to bookstore.dateAdded
                )

                bookstoresCollection.add(bookstoreData).await()
                Timber.d("Bookstore added: ${bookstore.name}")

                // Reload bookstores
                loadBookstores()
            } catch (e: Exception) {
                Timber.e(e, "Error adding bookstore")
            }
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                return MapViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}