package ru.topbun.pawmate.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.pawmate.entity.PetType
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.DialogWrapper
import ru.topbun.pawmate.presentation.theme.components.rippleClickable

@Composable
fun ChoicePetTypeModal(petType: PetType, onDismissDialog: () -> Unit, onChangePetType: (PetType) -> Unit) {
    DialogWrapper(onDismissDialog = onDismissDialog) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            var petType by remember { mutableStateOf(petType) }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                PetType.entries.forEach{
                    val isSelected = it == petType
                    Box(
                        modifier = Modifier
                            .size(300.dp, 55.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Colors.BROWN else Colors.WHITE)
                            .rippleClickable { petType = it },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = it.toString(),
                            style = Typography.APP_TEXT,
                            color = if(isSelected) Colors.WHITE else Colors.BROWN,
                            fontSize = 24.sp,
                            fontFamily = Fonts.SF.SEMI_BOLD
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Сохранить",
                enabled = true,
            ) {
                onChangePetType(petType)
            }
        }
    }
}