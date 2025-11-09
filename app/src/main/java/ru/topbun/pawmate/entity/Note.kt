package ru.topbun.pawmate.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val title: String,
    val descr: String,
    val date: Long
)