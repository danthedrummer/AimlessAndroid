package org.dandowney.aimless.feature.tactile.slot.contract

import androidx.compose.ui.geometry.Offset
import org.dandowney.aimless.library.architecture.ViewState

data class SlotState(
  val title: String,
  val target: Offset?,
  val piece: Offset?,
  val solved: Boolean,
) : ViewState
