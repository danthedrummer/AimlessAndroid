package org.dandowney.aimless.feature.color.filters.contract

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.library.architecture.ViewState

data class ColorFiltersState(
  val filters: List<FilterState>,
  val touchInputKey: Long,
) : ViewState

data class FilterState(
  val id: String,
  val color: Color,
  val center: Offset,
)
