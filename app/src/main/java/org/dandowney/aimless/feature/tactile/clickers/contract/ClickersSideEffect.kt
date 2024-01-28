package org.dandowney.aimless.feature.tactile.clickers.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface ClickersSideEffect : ViewSideEffect {
  data object NavigateBack : ClickersSideEffect
}
