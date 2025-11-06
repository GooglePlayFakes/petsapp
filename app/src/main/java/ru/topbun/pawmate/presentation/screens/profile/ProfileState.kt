package ru.topbun.pawmate.presentation.screens.profile

import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.User

data class ProfileState(
    val user: User? = null,
    val pets: List<Pet> = emptyList(),
    val isLogout: Boolean = false,
)
