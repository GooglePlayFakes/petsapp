package ru.topbun.pawmate.utils

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun pickImageLauncher(context: Context, onChangeImage: (String) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->
    uri?.let {
        val filePath = saveImageToLocalStorage(context, it)
        filePath?.let { path -> onChangeImage(path) }
    }
}