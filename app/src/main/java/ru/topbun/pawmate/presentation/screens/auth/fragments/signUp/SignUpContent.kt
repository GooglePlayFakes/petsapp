package ru.topbun.pawmate.presentation.screens.auth.fragments.signUp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.pawmate.presentation.screens.auth.fragments.login.LoginScreen
import ru.topbun.auth.fragments.signUp.SignUpState
import ru.topbun.auth.fragments.signUp.SignUpState.SignUpScreenState.Error
import ru.topbun.auth.fragments.signUp.SignUpState.SignUpScreenState.Loading
import ru.topbun.auth.fragments.signUp.SignUpState.SignUpScreenState.Success
import ru.topbun.pawmate.R
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography.APP_TEXT
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppTextField
import ru.topbun.pawmate.presentation.theme.components.noRippleClickable

data class SignUpScreen(
    private val onAuth: () -> Unit
): Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel = rememberScreenModel { SignUpViewModel(context) }
        val state by viewModel.state.collectAsState()
        viewModel.checkAuth()
        LaunchedEffect(state.isAuth) {
            if (state.isAuth) {
                onAuth()
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Регистрация",
                style = APP_TEXT,
                fontSize = 24.sp,
                fontFamily = Fonts.SF.BOLD,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            Fields(viewModel, state)
            ButtonAccountExists(onAuth)
            Spacer(modifier = Modifier.height(50.dp))
            ButtonSignUp(viewModel)
        }
        when(val screenState = state.signUpScreenState){
            is Error -> LaunchedEffect(screenState) {
                Toast.makeText(context, screenState.msg, Toast.LENGTH_SHORT).show()
            }
            Success -> {
                onAuth()
            }
            else -> {}
        }
    }

}


@Composable
private fun ColumnScope.ButtonAccountExists(onAuth: () -> Unit){
    val navigator = LocalNavigator.currentOrThrow
    Text(
        modifier = Modifier
            .padding(end = 5.dp, top = 5.dp)
            .align(Alignment.End)
            .noRippleClickable {
                navigator.push(LoginScreen(onAuth))
            },
        text = buildAnnotatedString {
            append("Уже есть аккаунт? ")
            pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
            append("Войти")
        },
        style = APP_TEXT,
        fontSize = 13.sp,
        color = Colors.GRAY_DARK
    )
}

@Composable
private fun ButtonSignUp(viewModel: SignUpViewModel) {
    val state by viewModel.state.collectAsState()
    val screenState = state.signUpScreenState
    val enabled = screenState != Loading && state.validFields
    AppButton(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        text = "Зарегистрироваться",
        enabled = enabled,
        loading = state.signUpScreenState == Loading
    ) {
        viewModel.signUp()
    }

}

@Composable
private fun Fields(viewModel: SignUpViewModel, state: SignUpState) {
    AppTextField(
        modifier = Modifier.height(48.dp),
        value = state.username,
        errorText = if (state.usernameError) "Имя владельца не может быть меньше 2 символов" else null,
        onValueChange = viewModel::changeUsername,
        placeholder = "Имя владельца",
    )
    Spacer(modifier = Modifier.height(10.dp))
    AppTextField(
        modifier = Modifier.height(48.dp),
        value = state.email,
        errorText = if (state.emailError) "Неверный формат эл. почты" else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        onValueChange = viewModel::changeEmail,
        placeholder = "Почта",
    )
    Spacer(modifier = Modifier.height(10.dp))
    AppTextField(
        modifier = Modifier.height(48.dp),
        value = state.password,
        errorText = if (state.passwordError) "Пароль не может быть меньше 6 символов" else null,
        onValueChange = viewModel::changePassword,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = "Пароль",
        visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        iconButton = {
            IconButton(
                modifier = Modifier.size(28.dp),
                onClick = { viewModel.changePasswordVisible() }
            ) {
                Icon(
                    painter = painterResource(
                        if (state.showPassword) R.drawable.ic_show
                        else R.drawable.ic_hide
                    ),
                    contentDescription = "Показать пароль",
                    tint = Colors.BLUE_GRAY
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    AppTextField(
        modifier = Modifier.height(48.dp),
        value = state.confirmPassword,
        errorText = if (state.confirmPasswordError) "Пароли не совпадают" else null,
        onValueChange = viewModel::changeConfirmPassword,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = "Подтвердите пароль",
        visualTransformation = if (state.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
        iconButton = {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    viewModel.changeConfirmPasswordVisible()
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (state.showConfirmPassword) R.drawable.ic_show
                        else R.drawable.ic_hide
                    ),
                    contentDescription = "Показать пароль",
                    tint = Colors.BLUE_GRAY
                )
            }
        }
    )
}
