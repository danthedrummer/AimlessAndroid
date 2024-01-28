package org.dandowney.aimless.feature.settings.contract

import org.dandowney.aimless.library.architecture.ViewState

data class SettingsState(
  val title: String,
  val isDarkTheme: Boolean,
  val analytics: List<Pair<String, Any?>>,
  val isDebug: Boolean,
) : ViewState
