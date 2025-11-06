package ru.topbun.pawmate.presentation.screens.profile

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.PetRepository
import ru.topbun.pawmate.repository.UserRepository

class ProfileViewModel(private val context: Context): ScreenModelState<ProfileState>(ProfileState()) {

    private val userRepository = UserRepository(context)
    private val petRepository = PetRepository(context)

    fun logOut() = screenModelScope.launch {
        userRepository.logout()
        updateState { copy(isLogout = true) }
    }

    private fun loadPets() = screenModelScope.launch {
        petRepository.getPetList().collect{
            updateState { copy(pets = it) }
        }
    }

    private fun loadUser() = screenModelScope.launch {
        userRepository.getUserInfo()?.let {
            updateState { copy(user = it) }
        }
    }

    fun addPet(pet: Pet) = screenModelScope.launch {
        petRepository.addPep(pet)
    }

    init {
        loadUser()
        loadPets()
    }

}