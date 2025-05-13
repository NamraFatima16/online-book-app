package ie.setu.bookapp.view.signup

sealed class SignUpUiEvent {
    data class EmailChanged(val email: String) : SignUpUiEvent()
    data class PasswordChanged(val password: String) : SignUpUiEvent()
    object SignUpButtonClicked : SignUpUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) :SignUpUiEvent()
}
