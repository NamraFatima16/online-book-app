package ie.setu.bookapp.navigation

object AppDestinations {
    // Authentication routes
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SIGN_UP = "sign_up"

    // Main navigation routes
    const val HOME = "home"
    const val LIBRARY = "library"
    const val PROFILE = "profile"

    // Detail and edit routes
    const val PROFILE_EDIT = "profile_edit"
    const val BOOK_DETAILS = "book_details/{bookId}"

    // Search and category routes
    const val SEARCH = "search"
    const val CATEGORY = "category/{categoryName}"

    fun bookDetailsRoute(bookId: Int): String = "book_details/$bookId"


    fun categoryRoute(categoryName: String): String = "category/$categoryName"
}