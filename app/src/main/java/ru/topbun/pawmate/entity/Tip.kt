package ru.topbun.pawmate.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "tips")
@Serializable
data class Tip(
    @PrimaryKey
    val id: Int = 0,
    val type: PetType,
    val text: String
)
