package ru.topbun.pawmate.presentation.screens.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.auth.fragments.signUp.SignUpState.SignUpScreenState.Loading
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.presentation.screens.auth.AuthScreen
import ru.topbun.pawmate.presentation.screens.detail.DetailScreen
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppIcon
import ru.topbun.pawmate.presentation.theme.components.noRippleClickable
import ru.topbun.pawmate.utils.formatAge

object ProfileScreen: Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var openAddPetModal by remember{ mutableStateOf(false) }
            val context = LocalContext.current
            val navigator = LocalNavigator.currentOrThrow
            val viewModel = rememberScreenModel { ProfileViewModel(context) }
            val state by viewModel.state.collectAsState()
            Header{ viewModel.logOut() }
            Spacer(modifier = Modifier.height(4.dp))
            NameWithCount(state)
            PetList(state){ navigator.push(DetailScreen(it)) }
            if (state.pets.size < 5){
                AppButton(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    text = "Добавить питомца",
                    enabled = true,
                ) {
                    openAddPetModal = true
                }
            }
            if (openAddPetModal){
                AddPetModal(
                    onDialogDismiss = { openAddPetModal = false },
                    onAddPet = { openAddPetModal = false; viewModel.addPet(it) }
                )
            }
            if (state.isLogout){
                navigator.replaceAll(AuthScreen)
            }
        }
    }
}


@Composable
private fun ColumnScope.PetList(state: ProfileState, onClickPet: (Pet) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Spacer(modifier = Modifier.width(8.dp)) }
        items(items = state.pets, key = {it.id}){ PetItem(it, onClickPet) }
        item { Spacer(modifier = Modifier.width(8.dp)) }
    }
}

@Composable
private fun PetItem(pet: Pet, onClickPet: (Pet) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().noRippleClickable { onClickPet(pet) },
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        val bitmap = BitmapFactory.decodeFile(pet.image)
        Box(
            modifier = Modifier
                .size(80.dp)
        ){
            bitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    bitmap = it.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
        Column {
            TitleWithSubtitle(pet.name, pet.age.formatAge())
            Spacer(modifier = Modifier.height(4.dp))
            TitleWithSubtitle(pet.breed ?: "", pet.type.toString())
        }
    }
}

@Composable
private fun TitleWithSubtitle(
    title: String,
    subtitle:String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ){
        Text(
            text = title,
            style = Typography.APP_TEXT,
            color = Colors.BLACK,
            fontSize = 20.sp,
            fontFamily = Fonts.SF.SEMI_BOLD
        )
        Text(
            text = subtitle,
            style = Typography.APP_TEXT,
            color = Colors.BROWN,
            fontSize = 16.sp,
            fontFamily = Fonts.SF.BOLD
        )
    }
}

@Composable
private fun NameWithCount(state: ProfileState) {
    Row {
        Text(
            text = state.user?.name ?: "Загрузка",
            style = Typography.APP_TEXT,
            color = Colors.BLACK,
            fontSize = 24.sp,
            fontFamily = Fonts.SF.SEMI_BOLD
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${state.pets.size}/5",
            style = Typography.APP_TEXT,
            color = Colors.BROWN,
            fontSize = 24.sp,
            fontFamily = Fonts.SF.SEMI_BOLD
        )
    }
}

@Composable
private fun Header(onClickLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val navigator = LocalNavigator.currentOrThrow
        AppIcon(R.drawable.ic_back) {
            navigator.pop()
        }
        Text(
            text = "Профиль",
            style = Typography.APP_TEXT,
            color = Colors.BLACK,
            fontSize = 24.sp,
            fontFamily = Fonts.SF.BOLD
        )
        Spacer(modifier = Modifier.weight(1f))
        AppIcon(R.drawable.ic_quit) {
            onClickLogout()
        }
    }
}