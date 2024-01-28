package org.dandowney.aimless.feature.color.slider.contract

import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface ColorSliderEvent : ViewEvent {

  data class InitialiseWithMode(val mode: String) : ColorSliderEvent

  data class ChannelValueChange(val channel: ColorSliderChannelState, val newValue: Float) : ColorSliderEvent

  data class OnModeSelected(val mode: String) : ColorSliderEvent

  data object BackClicked : ColorSliderEvent
}
