package ru.topbun.pawmate.presentation.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
    fun AppIcon(painter: Int, onClick: () -> Unit) {
        Image(
            painter = painterResource(painter),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .noRippleClickable {
                    onClick()
                }
        )
    }