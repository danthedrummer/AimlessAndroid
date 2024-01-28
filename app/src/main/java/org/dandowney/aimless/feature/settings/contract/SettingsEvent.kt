package org.dandowney.aimless.feature.settings.contract

import org.dandowney.aimless.library.architecture.ViewEvent

sealed interface SettingsEvent : ViewEvent {

  data class ToggleDarkTheme(val value: Boolean) : SettingsEvent

  data object PrivacyPolicyClicked : SettingsEvent
}
