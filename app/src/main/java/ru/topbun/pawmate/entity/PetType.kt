package ru.topbun.pawmate.entity

enum class PetType {
    DOG, CAT;

    override fun toString(): String {
        return when(this){
            CAT -> "Кошка"
            DOG -> "Собака"
        }
    }
}