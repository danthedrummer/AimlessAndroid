package org.dandowney.aimless.library.design.theme

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
  themeRepository: ThemeRepository,
) : ViewModel() {

  val isDarkTheme: Flow<Boolean> = themeRepository.darkTheme
}
