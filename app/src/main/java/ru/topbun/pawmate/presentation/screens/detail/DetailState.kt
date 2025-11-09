package ru.topbun.pawmate.presentation.screens.detail

import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.Tip

data class DetailState(
    val pet: Pet,
    val notes: List<Note> = emptyList(),
    val editMode: Boolean = false,
    val shouldCloseScreen: Boolean = false,
    val shouldOpenAddNoteModal: Boolean = false,
    val shouldOpenConfirmDeleteModal: Note? = null,
)
