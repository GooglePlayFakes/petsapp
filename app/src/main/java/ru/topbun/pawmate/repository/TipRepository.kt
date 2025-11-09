package ru.topbun.pawmate.repository

import android.R.attr.type
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.topbun.pawmate.database.AppDatabase
import ru.topbun.pawmate.entity.PetType
import ru.topbun.pawmate.entity.Tip

class TipRepository(
    private val context: Context
) {
    private val dao = AppDatabase.getInstance(context).tipDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initTips()
        }
    }

    suspend fun getRandomTip(): Tip? {
        initTips()
        val tips = dao.getTips().takeIf { it.isNotEmpty() } ?: return null
        return tips.random()
    }
    suspend fun getTipList() = dao.getTips().shuffled()

    private suspend fun initTips(){
        if (dao.getTips().size < 50){
            dao.deleteAll()
            val jsonString = context.assets.open("tips.json").bufferedReader().use { it.readText() }
            val tipsList = Json.decodeFromString<List<Tip>>(jsonString)
            dao.insertTips(tipsList)
        }
    }


}