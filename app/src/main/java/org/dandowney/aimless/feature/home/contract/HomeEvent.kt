package org.dandowney.aimless.feature.home.contract

import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface HomeEvent : ViewEvent {

  data class ActivitySelected(val aimlessOption: ActivityOption) : HomeEvent

  data object AimlessIconClicked : HomeEvent
}
