package ru.topbun.pawmate.presentation.screens.main

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.registry.screenModule
import kotlinx.coroutines.launch
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.TipRepository

class MainViewModel(
    context: Context
): ScreenModelState<MainState>(MainState()) {

    private val repository = TipRepository(context)

    fun loadTip() = screenModelScope.launch {
        val newTip = repository.getRandomTip()
        updateState { copy(newTip?.text ?: "") }
    }

}