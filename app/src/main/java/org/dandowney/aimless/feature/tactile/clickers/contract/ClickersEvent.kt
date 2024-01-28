package org.dandowney.aimless.feature.tactile.clickers.contract

import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface ClickersEvent : ViewEvent {

  data class ClickedPressed(val id: Int) : ClickersEvent

  data class ClickerUnpressed(val id: Int) : ClickersEvent

  data object BackClicked : ClickersEvent
}
