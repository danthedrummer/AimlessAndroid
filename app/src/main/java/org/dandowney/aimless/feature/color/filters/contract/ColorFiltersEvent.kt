package org.dandowney.aimless.feature.color.filters.contract

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface ColorFiltersEvent : ViewEvent {

  data class CanvasReady(val size: Size) : ColorFiltersEvent

  data class FilterMoved(val filterId: String, val newPosition: Offset) : ColorFiltersEvent

  data object DragGestureStopped : ColorFiltersEvent

  data object BackClicked : ColorFiltersEvent
}
