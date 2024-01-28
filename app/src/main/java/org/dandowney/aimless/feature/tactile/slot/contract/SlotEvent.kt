package org.dandowney.aimless.feature.tactile.slot.contract

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface SlotEvent : ViewEvent {

  data class PieceMoved(val position: Offset) : SlotEvent

  data class CanvasReady(
    val size: Size,
    val shapeSize: Size,
  ) : SlotEvent

  data object BackClicked : SlotEvent
}
