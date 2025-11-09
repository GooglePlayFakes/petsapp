package ru.topbun.pawmate.repository

import android.content.Context
import ru.topbun.pawmate.database.AppDatabase
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.workers.NotificationManager

class NoteRepository(
    private val context: Context
) {

    private val dao = AppDatabase.getInstance(context).noteDao()

    fun getNotes(petId: Int) = dao.getNoteList(petId)

    suspend fun addNote(note: Note) = dao.insertNote(note)

    suspend fun deleteNote(id: Int) = dao.deleteNote(id)


}