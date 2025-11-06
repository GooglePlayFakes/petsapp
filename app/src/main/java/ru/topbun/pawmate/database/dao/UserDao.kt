package ru.topbun.pawmate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.topbun.pawmate.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getUser(id: Int): User?

    @Query("SELECT COUNT(*) FROM user WHERE email = :email LIMIT 1")
    suspend fun userIsExists(email: String): Int

}