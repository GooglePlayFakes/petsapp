package ru.topbun.pawmate.presentation.screens.detail

import android.R.attr.enabled
import android.R.attr.onClick
import android.graphics.BitmapFactory
import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.Tip
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppIcon
import ru.topbun.pawmate.presentation.theme.components.noRippleClickable
import ru.topbun.pawmate.presentation.theme.components.rippleClickable
import ru.topbun.pawmate.utils.formatAge
import ru.topbun.pawmate.utils.formatDate
import ru.topbun.pawmate.utils.pickImageLauncher

@Parcelize
data class DetailScreen(
    val pet: Pet
): Screen, Parcelable {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            val context = LocalContext.current
            val navigator = LocalNavigator.currentOrThrow
            val viewModel = rememberScreenModel { DetailViewModel(context, pet) }
            val state by viewModel.state.collectAsState()
            Header(
                state.editMode,
                onClickEdit = viewModel::changeEditMode,
                onClickDelete = { viewModel.deletePet() },
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(state.pet.image, state.editMode){ viewModel.changeImage(it) }
            Spacer(modifier = Modifier.height(20.dp))
            Fields(viewModel)
            Spacer(modifier = Modifier.height(20.dp))
            Notes(viewModel, state.notes)
            if (state.shouldCloseScreen){ navigator.pop() }
            state.shouldOpenConfirmDeleteModal?.let {
                ConfirmDeleteNoteModal(
                    note = it,
                    onDialogDismiss = {
                        viewModel.changeOpenConfirmDeleteModal(null)
                    }
                ) {
                    viewModel.deleteNote(it)
                    viewModel.changeOpenConfirmDeleteModal(null)
                }
            }
            if (state.shouldOpenAddNoteModal){
                AddNoteModal(
                    petId = pet.id,
                    onDialogDismiss = {
                        viewModel.changeOpenAddNoteModal(false)
                    }
                ) {
                    viewModel.addNote(it)
                    viewModel.changeOpenAddNoteModal(false)
                }
            }
        }
    }

}

@Composable
private fun Notes(viewModel: DetailViewModel, notes: List<Note>) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Заметки:",
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 24.sp,
                fontFamily = Fonts.SF.SEMI_BOLD
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {
                    viewModel.changeOpenAddNoteModal(true)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "",
                    tint = Colors.BROWN
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ){
            notes.forEach {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Colors.WHITE, RoundedCornerShape(12.dp))
                        .combinedClickable(
                            onLongClick = {
                                viewModel.changeOpenConfirmDeleteModal(it)
                            }
                        ){}
                        .padding(12.dp),
                ){
                    Text(
                        text = it.title,
                        style = Typography.APP_TEXT,
                        color = Colors.BLACK,
                        fontSize = 18.sp,
                        fontFamily = Fonts.SF.BOLD
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDate(it.date),
                        style = Typography.APP_TEXT,
                        color = Colors.BROWN,
                        fontSize = 16.sp,
                        fontFamily = Fonts.SF.BOLD
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.descr,
                        style = Typography.APP_TEXT,
                        color = Colors.BLACK,
                        fontSize = 16.sp,
                        fontFamily = Fonts.SF.MEDIUM
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Fields(viewModel: DetailViewModel) {
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        TitleWithTextField(
            title = "Кличка:",
            value = state.pet.name,
            enabled = state.editMode,
            keyboardType = KeyboardType.Text,
            onChangeValue = viewModel::changeName
        )
        TitleWithTextField(
            title = "Возраст:",
            value = if(state.editMode) state.pet.age.toString() else state.pet.age.formatAge(),
            enabled = state.editMode,
            keyboardType = KeyboardType.Number,
            onChangeValue = viewModel::changeAge
        )

        TitleWithTextField(
            title = "Порода:",
            value = state.pet.breed ?: "Неизвестно",
            enabled = state.editMode,
            keyboardType = KeyboardType.Text,
            onChangeValue = viewModel::changeBreed
        )
        TitleWithTextField(
            title = "Тип:",
            value = state.pet.type,
            enabled = state.editMode,
            keyboardType = KeyboardType.Text,
            onChangeValue = viewModel::changeType
        )
        Column {
            Text(
                text = "Описание",
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 20.sp,
                fontFamily = Fonts.SF.SEMI_BOLD
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicTextField(
                value = state.pet.description,
                onValueChange = viewModel::changeDescription,
                enabled = state.editMode,
                singleLine = false,
                textStyle = TextStyle(
                    color = Colors.BROWN,
                    fontSize = 16.sp,
                    fontFamily = Fonts.SF.SEMI_BOLD,
                    textDecoration = if (state.editMode) TextDecoration.Underline else TextDecoration.None
                )
            )
        }
    }
}

@Composable
private fun TitleWithTextField(
    title: String,
    value: String,
    enabled: Boolean,
    keyboardType: KeyboardType,
    onChangeValue: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = title,
            style = Typography.APP_TEXT,
            color = Colors.BLACK,
            fontSize = 20.sp,
            fontFamily = Fonts.SF.SEMI_BOLD
        )
        Spacer(modifier = Modifier.width(4.dp))

        BasicTextField(
            value = value,
            onValueChange = onChangeValue,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            textStyle = TextStyle(
                color = Colors.BROWN,
                fontSize = 20.sp,
                fontFamily = Fonts.SF.SEMI_BOLD,
                textDecoration = if (enabled) TextDecoration.Underline else TextDecoration.None
            )
        )
    }
}

@Composable
private fun Image(image: String?,editMode: Boolean, onChangeImage: (String) -> Unit) {
    val context = LocalContext.current
    val launcher = pickImageLauncher(context, onChangeImage)
    val modifier = if (editMode) Modifier.rippleClickable { launcher.launch("image/*")} else Modifier
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(1.5.dp, Colors.BLUE_GRAY, RoundedCornerShape(16.dp))
            .then(modifier)
    ) {
        val bitmap = BitmapFactory.decodeFile(image)
        bitmap?.let {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                bitmap = it.asImageBitmap(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Header(modeEdit: Boolean, onClickEdit: () -> Unit, onClickDelete: () -> Unit) {
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
        Spacer(modifier = Modifier.weight(1f))
        AppIcon(
            if (modeEdit) R.drawable.ic_accept
            else R.drawable.ic_edit
        ) {
            onClickEdit()
        }
        AppIcon(R.drawable.ic_delete) {
            onClickDelete()
        }
    }
}