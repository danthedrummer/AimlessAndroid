package org.dandowney.aimless.feature.color.switcher.contract

import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface ColorSwitcherEvent : ViewEvent {

  data class ColorSelected(val color: Color) : ColorSwitcherEvent

  data object BackClicked : ColorSwitcherEvent
}
