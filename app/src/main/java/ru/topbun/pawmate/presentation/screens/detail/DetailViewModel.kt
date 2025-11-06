package ru.topbun.pawmate.presentation.screens.detail

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.PetType
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.PetRepository
import ru.topbun.pawmate.repository.TipRepository

class DetailViewModel(
    context: Context,
    private val pet: Pet
): ScreenModelState<DetailState>(DetailState(pet)) {

    private val tipRepository = TipRepository(context)
    private val petRepository = PetRepository(context)

    private fun loadTips() = screenModelScope.launch {
        val tips = tipRepository.getTipList(pet.type)
        updateState { copy(tips = tips) }
    }

    fun changeName(name: String){ updateState { copy(pet.copy(name = name)) } }
    fun changeAge(age: String){ updateState { copy(pet.copy(age = age.toIntOrNull() ?: 0)) } }
    fun changeBreed(breed: String){ updateState { copy(pet.copy(breed = breed)) } }
    fun changeType(type: PetType){ updateState { copy(pet.copy(type = type)) } }
    fun changeImage(image: String){ updateState { copy(pet.copy(image = image)) } }

    fun changeEditMode() = screenModelScope.launch {
        if (stateValue.editMode) petRepository.addPep(stateValue.pet)
        updateState { copy(editMode = !editMode) }
    }

    fun deletePet() = screenModelScope.launch {
        petRepository.deletePet(pet.id)
        updateState { copy(shouldCloseScreen = true) }
    }

    init {
        loadTips()
    }

}