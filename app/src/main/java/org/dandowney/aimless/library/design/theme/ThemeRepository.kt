package org.dandowney.aimless.library.design.theme

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

  val darkTheme: StateFlow<Boolean>

  suspend fun isDarkTheme(): Boolean

  suspend fun setDarkTheme(isDarkTheme: Boolean)
}
