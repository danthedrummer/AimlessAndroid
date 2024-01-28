package org.dandowney.aimless.feature.settings.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface SettingsSideEffect : ViewSideEffect {

  data class OpenPrivacyPolicy(val uri: String) : SettingsSideEffect
}
