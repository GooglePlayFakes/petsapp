package ru.topbun.pawmate.utils

fun String.validationEmail() = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(this)
