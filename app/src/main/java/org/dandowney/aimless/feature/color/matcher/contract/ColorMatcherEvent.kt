package org.dandowney.aimless.feature.color.matcher.contract

import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface ColorMatcherEvent : ViewEvent {

  data class ColorPressed(val optionState: ColorOptionState) : ColorMatcherEvent

  data object BackClicked : ColorMatcherEvent
}
