package ru.topbun.pawmate.presentation.screens.detail

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.NoteRepository
import ru.topbun.pawmate.repository.PetRepository

class DetailViewModel(
    context: Context,
    private val pet: Pet
): ScreenModelState<DetailState>(DetailState(pet)) {

    private val petRepository = PetRepository(context)
    private val noteRepository = NoteRepository(context)

    private fun loadNotes() = screenModelScope.launch {
        noteRepository.getNotes(pet.id).collect {
            updateState { copy(notes = it) }
        }
    }

    fun addNote(note: Note) = screenModelScope.launch { noteRepository.addNote(note) }
    fun deleteNote(note: Note) = screenModelScope.launch { noteRepository.deleteNote(note.id) }

    fun changeName(name: String){ updateState { copy(pet.copy(name = name)) } }
    fun changeAge(age: String){ updateState { copy(pet.copy(age = age.toIntOrNull() ?: 0)) } }
    fun changeBreed(breed: String){ updateState { copy(pet.copy(breed = breed)) } }
    fun changeType(type: String){ updateState { copy(pet.copy(type = type)) } }
    fun changeDescription(description: String){ updateState { copy(pet.copy(description = description)) } }
    fun changeImage(image: String){ updateState { copy(pet.copy(image = image)) } }
    fun changeOpenAddNoteModal(value: Boolean){ updateState { copy(shouldOpenAddNoteModal = value) } }
    fun changeOpenConfirmDeleteModal(note:Note?){ updateState { copy(shouldOpenConfirmDeleteModal = note) } }

    fun changeEditMode() = screenModelScope.launch {
        if (stateValue.editMode) petRepository.addPep(stateValue.pet)
        updateState { copy(editMode = !editMode) }
    }

    fun deletePet() = screenModelScope.launch {
        petRepository.deletePet(pet.id)
        updateState { copy(shouldCloseScreen = true) }
    }

    init {
        loadNotes()
    }

}