package ru.topbun.pawmate.presentation.theme.components

import cafe.adriel.voyager.core.model.StateScreenModel

open class ScreenModelState<T>(initialState: T): StateScreenModel<T>(initialState) {

    val stateValue get() = state.value

    open fun updateState(update: T.() -> T) {
        mutableState.value = state.value.update()
    }

}