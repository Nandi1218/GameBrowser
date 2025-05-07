package hu.bme.nandiii.gamebrowser.util

import hu.bme.nandiii.gamebrowser.ui.model.UiText

sealed class UiEvent {
    object Success : UiEvent()

    data class Failure(val message: UiText) : UiEvent()
}