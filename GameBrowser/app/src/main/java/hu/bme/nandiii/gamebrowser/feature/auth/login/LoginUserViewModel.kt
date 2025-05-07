package hu.bme.nandiii.gamebrowser.feature.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.data.auth.AuthService
import hu.bme.nandiii.gamebrowser.domain.usecase.IsEmailValidUseCase
import hu.bme.nandiii.gamebrowser.ui.model.UiText
import hu.bme.nandiii.gamebrowser.ui.model.toUiText
import hu.bme.nandiii.gamebrowser.util.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    private val authService: AuthService,
    private val isEmailValid: IsEmailValidUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUserState())
    val state = _state.asStateFlow()

    private val email get() = state.value.email

    private val password get() = state.value.password
    private val passwordAgain get() = state.value.passwordAgain

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    fun autoLogin() {
        viewModelScope.launch {
            if (authService.hasUser) {
                _uiEvent.send(UiEvent.Success)
                Log.d("AUTH", "AutoLogin successful with email: $email")
            }
        }
    }

    fun onEvent(event: LoginUserEvent) {
        when (event) {
            is LoginUserEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }

            is LoginUserEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }

            is LoginUserEvent.PasswordConfirmChanged -> {
                val newPasswordAgain = event.passwordAgain.trim()
                _state.update { it.copy(passwordAgain = newPasswordAgain) }
            }

            LoginUserEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }

            LoginUserEvent.PasswordAgainVisibilityChanged -> {
                _state.update { it.copy(passwordAgainVisibility = !state.value.passwordAgainVisibility) }
            }

            LoginUserEvent.RegisterStateSwitch -> {
                _state.update { it.copy(registerState = !state.value.registerState) }
            }

            LoginUserEvent.Validate -> {
                _state.update {
                    it.copy(
                        emailError = !isEmailValid(email),
                        passwordError = password.isBlank()
                    )
                }
            }

            LoginUserEvent.SignIn -> {
                Log.d("AUTH", "Sign in button pressed")
                onSignIn()
            }

            LoginUserEvent.Register -> {
                onRegister()
            }
        }
    }

    private fun onSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.invalid_email))
                    )
                    Log.d("AUTH", "Invalid email snackbar sent")
                } else {
                    if (password.isBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.password_cannot_be_empty))
                        )
                        Log.d("AUTH", "Password cannot be empty snackbar sent")
                    } else {
                        authService.authenticate(email, password)
                        _uiEvent.send(UiEvent.Success)
                        Log.d("AUTH", "Auth success")
                    }
                }
            } catch (e: Exception) {
                Log.d("AUTH", "Failure with email: $email password: $password")
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
                Log.d("AUTH", "Snackbar shown")

            }
        }
    }

    private fun onRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.invalid_email))

                    )
                } else {
                    if (password.isBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                        )
                    } else if (passwordAgain.isBlank()) {
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.second_password_empty)))
                    } else if (passwordAgain.equals(password).not()) {
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.password_not_matching)))
                    } else {
                        authService.signUp(email, password)
                        _uiEvent.send(UiEvent.Success)
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}


data class LoginUserState(
    val email: String = "",
    val password: String = "",
    val passwordAgain: String = "",
    val passwordVisibility: Boolean = false,
    val passwordAgainVisibility: Boolean = false,
    val registerState: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
)

sealed class LoginUserEvent {
    data class EmailChanged(val email: String) : LoginUserEvent()
    data class PasswordChanged(val password: String) : LoginUserEvent()
    data class PasswordConfirmChanged(val passwordAgain: String) : LoginUserEvent()
    object PasswordVisibilityChanged : LoginUserEvent()
    object PasswordAgainVisibilityChanged : LoginUserEvent()
    object SignIn : LoginUserEvent()
    object Register : LoginUserEvent()
    object RegisterStateSwitch : LoginUserEvent()
    object Validate : LoginUserEvent()


}