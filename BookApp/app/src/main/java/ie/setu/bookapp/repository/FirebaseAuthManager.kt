package ie.setu.bookapp.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import ie.setu.bookapp.model.User
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Timber.d("User signed in: ${result.user?.email}")
            result.user
        } catch (e: Exception) {
            Timber.e(e, "Error signing in with email")
            null
        }
    }

    suspend fun registerWithEmail(email: String, password: String, firstName: String, lastName: String): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Timber.d("User registered: ${result.user?.email}")

            // Create user document in Firestore
            result.user?.let { firebaseUser ->
                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = ""
                )
                createUserInFirestore(firebaseUser.uid, user)
            }

            result.user
        } catch (e: Exception) {
            Timber.e(e, "Error registering with email")
            null
        }
    }

    suspend fun signInWithGoogle(googleAccount: GoogleSignInAccount): FirebaseUser? {
        return try {
            val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Timber.d("User signed in with Google: ${result.user?.email}")

            // Create or update user document in Firestore
            result.user?.let { firebaseUser ->
                val names = googleAccount.displayName?.split(" ") ?: listOf("", "")
                val firstName = names.firstOrNull() ?: ""
                val lastName = if (names.size > 1) names.last() else ""

                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = googleAccount.email ?: "",
                    password = ""
                )
                createUserInFirestore(firebaseUser.uid, user)
            }

            result.user
        } catch (e: Exception) {
            Timber.e(e, "Error signing in with Google")
            null
        }
    }

    suspend fun signOut() {
        auth.signOut()
        Timber.d("User signed out")
    }

    private suspend fun createUserInFirestore(uid: String, user: User) {
        try {
            // Checking if user already exists in Firestore
            val userDoc = usersCollection.document(uid).get().await()
            if (!userDoc.exists()) {
                // Creates a layout with user data
                val userData = mapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "dateCreated" to user.dateCreated,
                    "lastLogin" to System.currentTimeMillis()
                )

                usersCollection.document(uid).set(userData).await()
                Timber.d("User added to Firestore: ${user.email}")
            } else {
                // Update lastLogin
                usersCollection.document(uid).update("lastLogin", System.currentTimeMillis()).await()
                Timber.d("Updated last login for user: ${user.email}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error creating/updating user in Firestore")
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun getUserData(uid: String): User? {
        return try {
            val userDoc = usersCollection.document(uid).get().await()
            if (userDoc.exists()) {
                val firstName = userDoc.getString("firstName") ?: ""
                val lastName = userDoc.getString("lastName") ?: ""
                val email = userDoc.getString("email") ?: ""

                User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting user data from Firestore")
            null
        }
    }
}