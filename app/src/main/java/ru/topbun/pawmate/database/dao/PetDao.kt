package ru.topbun.pawmate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.topbun.pawmate.entity.Pet

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun deletePet(id: Int)

    @Query("SELECT * FROM pets")
    fun getPetList(): Flow<List<Pet>>

}