package ru.topbun.pawmate.presentation.screens.profile

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.PetType
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppTextField
import ru.topbun.pawmate.presentation.theme.components.DialogWrapper
import ru.topbun.pawmate.presentation.theme.components.rippleClickable
import ru.topbun.pawmate.utils.pickImageLauncher
import ru.topbun.pawmate.utils.saveImageToLocalStorage

@Composable
fun AddPetModal(
    onDialogDismiss: () -> Unit,
    onAddPet: (Pet) -> Unit
) {
    DialogWrapper(onDismissDialog = onDialogDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            var name by remember { mutableStateOf("") }
            var age by remember { mutableStateOf("") }
            var breed by remember { mutableStateOf("") }
            var image by remember { mutableStateOf<String?>(null) }
            var typePet by remember { mutableStateOf(PetType.DOG) }
            Title()
            Spacer(modifier = Modifier.height(16.dp))
            FieldWithPhoto(
                name = name,
                age = age,
                breed = breed,
                image = image,
                onChangeName = { name = it },
                onChangeAge = { age = it },
                onChangeBreed = { breed = it },
                onChangeImage = { image = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            ChoicePetType(typePet){ typePet = it }
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Привязать питомца",
                enabled = true,
            ) {
                val pet = Pet(
                    name = name,
                    age = age.toIntOrNull() ?: 0,
                    breed = breed.takeIf { it.isNotBlank() },
                    type = typePet,
                    image = image
                )
                onAddPet(pet)
            }
        }
    }
}

@Composable
private fun ChoicePetType(type: PetType, onChangeType: (PetType) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PetType.entries.forEach {
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onChangeType(it) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = type == it,
                    onClick = { onChangeType(it) },
                    colors = RadioButtonDefaults.colors(
                        Colors.BROWN,
                        Colors.BROWN,
                        Colors.BROWN,
                        Colors.BROWN
                    )
                )
                Text(
                    text = it.toString(),
                    style = Typography.APP_TEXT,
                    color = Colors.BLACK,
                    fontSize = 14.sp,
                    fontFamily = Fonts.SF.MEDIUM
                )
            }
        }
    }
}

@Composable
private fun FieldWithPhoto(
    name: String,
    age: String,
    breed: String,
    image: String?,
    onChangeName: (String) -> Unit,
    onChangeAge: (String) -> Unit,
    onChangeBreed: (String) -> Unit,
    onChangeImage: (String) -> Unit,
) {
    var columnHeight by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned { layoutCoordinates ->
                    columnHeight = layoutCoordinates.size.height
                },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AppTextField(
                modifier = Modifier.height(48.dp),
                value = name,
                errorText = null,
                onValueChange = { onChangeName(it) },
                placeholder = "Имя питомца",
            )
            AppTextField(
                modifier = Modifier.height(48.dp),
                value = age,
                errorText = null,
                onValueChange = { onChangeAge(it) },
                placeholder = "Возраст",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            AppTextField(
                modifier = Modifier.height(48.dp),
                value = breed,
                errorText = null,
                onValueChange = { onChangeBreed(it) },
                placeholder = "Порода",
            )
        }
        val bitmap = BitmapFactory.decodeFile(image)
        val context = LocalContext.current
        val launcher = pickImageLauncher(context, onChangeImage)
        Box(
            modifier = Modifier
                .height(with(LocalDensity.current) { columnHeight.toDp() })
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, Colors.BLUE_GRAY, RoundedCornerShape(8.dp))
                .rippleClickable { launcher.launch("image/*")  }
        ){
            bitmap?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Добавить питомца",
        style = Typography.APP_TEXT,
        color = Colors.BLACK,
        fontSize = 24.sp,
        fontFamily = Fonts.SF.SEMI_BOLD
    )
}