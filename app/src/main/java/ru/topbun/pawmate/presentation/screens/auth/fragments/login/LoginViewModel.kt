package ru.topbun.pawmate.presentation.screens.auth.fragments.login

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ru.topbun.auth.fragments.login.LoginState
import ru.topbun.auth.fragments.login.LoginState.LoginScreenState.*
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.UserRepository

class LoginViewModel(
    private val context: Context
): ScreenModelState<LoginState>(LoginState()) {

    private val repository = UserRepository(context)

    fun checkAuth() = screenModelScope.launch{
        val isAuth = repository.isUserAuth()
        updateState { copy(isAuth = isAuth) }
    }

    fun login() = screenModelScope.launch {
        updateState { copy(loginScreenState = Loading) }
        val email = stateValue.email.trim()
        val password = stateValue.password.trim()
        val isAuth = repository.login(email, password)
        if (isAuth){
            updateState { copy(loginScreenState = Success) }
        } else {
            updateState { copy(loginScreenState = Error("Пользователь не найден")) }
        }
    }

    fun changeEmail(email: String) = updateState { copy(email = email, validFields = validFields(email, password)) }

    fun changePassword(password: String) = updateState { copy(password = password, validFields = validFields(email, password)) }
    fun changePasswordVisible() = updateState { copy(showPassword = !showPassword) }
    private fun validFields(email: String, password: String) = password.isNotEmpty() && email.isNotEmpty()

    init {
        checkAuth()
    }

}