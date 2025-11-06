package ru.topbun.pawmate.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.pawmate.R
import ru.topbun.pawmate.presentation.screens.profile.ProfileScreen
import ru.topbun.pawmate.presentation.screens.reminder.ReminderScreen
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppIcon
import ru.topbun.pawmate.presentation.theme.components.rippleClickable

object MainScreen: Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 20.dp)
        ) {
            val context = LocalContext.current
            val viewModel = rememberScreenModel { MainViewModel(context) }
            val state by viewModel.state.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.loadTip()
            }
            val navigator = LocalNavigator.currentOrThrow
            Header(
                onClickNotify = { navigator.push(ReminderScreen) },
                onClickProfile = { navigator.push(ProfileScreen) }
            )
            Tip(state.tip){
                viewModel.loadTip()
            }
        }
    }

}

@Composable
private fun BoxScope.Tip(tipText: String, onClickButton: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .background(Colors.WHITE, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Совет",
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 24.sp,
                fontFamily = Fonts.SF.BOLD
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = tipText,
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 16.sp,
                fontFamily = Fonts.SF.MEDIUM,
                textAlign = TextAlign.Justify
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(54.dp)
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Colors.WHITE)
                .rippleClickable { onClickButton() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_random),
                contentDescription = null,
                tint = Colors.BROWN
            )
        }
    }
}

@Composable
private fun Header(onClickNotify: () -> Unit, onClickProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Главная",
            style = Typography.APP_TEXT,
            color = Colors.BLACK,
            fontSize = 24.sp,
            fontFamily = Fonts.SF.BOLD
        )
        Spacer(modifier = Modifier.weight(1f))
        AppIcon(R.drawable.ic_notification) {
            onClickNotify()
        }
        AppIcon(R.drawable.ic_profile) {
            onClickProfile()
        }
    }
}