package ru.topbun.pawmate.utils



fun Int.formatAge(): String {
    val suffix = when {
        this % 100 in 11..19 -> "лет"
        this % 10 == 1 -> "год"
        this % 10 in 2..4 -> "года"
        else -> "лет"
    }
    return "$this $suffix"
}