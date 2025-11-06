package ru.topbun.pawmate.presentation.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.pawmate.presentation.screens.auth.fragments.login.LoginScreen
import ru.topbun.pawmate.presentation.screens.main.MainScreen
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts

data object AuthScreen: Screen{

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Header()
            Spacer(modifier = Modifier.height(80.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
            ){
                val navigator = LocalNavigator.currentOrThrow
                Navigator(screen = LoginScreen{
                    navigator.replace(MainScreen)
                })
            }
        }
    }

}

@Composable
private fun Header() {
    Text(
        text = "Добро пожаловать! Рады вас видеть",
        style = ru.topbun.pawmate.presentation.theme.Typography.APP_TEXT,
        color = Colors.BLACK,
        fontSize = 30.sp,
        fontFamily = Fonts.SF.BOLD
    )
}