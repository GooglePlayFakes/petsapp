package ru.topbun.pawmate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Pet

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM notes WHERE petId=:petId")
    fun getNoteList(petId: Int): Flow<List<Note>>

    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id: Int)

}