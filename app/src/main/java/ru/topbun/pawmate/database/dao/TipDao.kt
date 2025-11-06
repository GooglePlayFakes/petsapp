package ru.topbun.pawmate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.topbun.pawmate.entity.PetType
import ru.topbun.pawmate.entity.Tip

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTips(tip: List<Tip>)

    @Query("SELECT * FROM tips WHERE type = :type")
    suspend fun getTips(type: PetType): List<Tip>

    @Query("SELECT * FROM tips")
    suspend fun getTips(): List<Tip>

    @Query("DELETE FROM tips")
    suspend fun deleteAll()

}