package ru.topbun.pawmate.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveImageToLocalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "images")
        if (!file.exists()) {
            file.mkdir()
        }
        val imageFile = File(file, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(imageFile)

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        imageFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}