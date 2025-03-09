package ie.setu.bookapp.model

import timber.log.Timber

data class User(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
) {
    init {
        Timber.d("User created: $firstName $lastName")
    }

    // Update user email
    fun updateEmail(newEmail: String) {
        Timber.d("User email changed from $email to $newEmail")
        email = newEmail
    }
}