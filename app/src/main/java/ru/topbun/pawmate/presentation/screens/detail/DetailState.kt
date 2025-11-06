package ru.topbun.pawmate.presentation.screens.detail

import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.Tip

data class DetailState(
    val pet: Pet,
    val tips: List<Tip> = emptyList(),
    val editMode: Boolean = false,
    val shouldCloseScreen: Boolean = false
)
