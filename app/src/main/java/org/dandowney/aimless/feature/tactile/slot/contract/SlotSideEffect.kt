package org.dandowney.aimless.feature.tactile.slot.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface SlotSideEffect : ViewSideEffect {
  data object NavigateBack : SlotSideEffect
  data object PlayVibration : SlotSideEffect
}
