package org.dandowney.aimless.feature.color.filters.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.feature.color.filters.ColorFiltersViewModel
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersEvent
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersSideEffect
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersState
import org.dandowney.aimless.library.design.container.AimlessScreen
import org.dandowney.aimless.library.design.theme.AimlessTheme
import kotlin.math.abs

@Composable
fun ColorFilters(
  viewModel: ColorFiltersViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  LaunchedEffect(key1 = Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ColorFiltersSideEffect.NavigateBack -> onNavigateBack()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(ColorFiltersEvent.BackClicked)
  }
  val state by viewModel.state
  ColorFilters(
    state = state,
    onBackClick = { viewModel.sendEvent(ColorFiltersEvent.BackClicked) },
    onCanvasReady = { viewModel.sendEvent(ColorFiltersEvent.CanvasReady(it)) },
    onFilterMoved = { filterId, newPosition ->
      viewModel.sendEvent(ColorFiltersEvent.FilterMoved(filterId, newPosition))
    },
    onDragGestureStopped = { viewModel.sendEvent(ColorFiltersEvent.DragGestureStopped) },
  )
}

@Composable
private fun ColorFilters(
  state: ColorFiltersState,
  onBackClick: () -> Unit,
  onCanvasReady: (Size) -> Unit,
  onFilterMoved: (String, Offset) -> Unit,
  onDragGestureStopped: () -> Unit,
) {
  AimlessScreen(
    title = "Colour Filters",
    leadingIcon = AimlessTheme.icons.back,
    onLeadingIconClick = onBackClick,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .drawBehind {
          if (state.filters.isEmpty()) {
            onCanvasReady(size)
          }
        },
      contentAlignment = Alignment.Center,
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(state.touchInputKey) {
            awaitEachGesture {
              val firstDown = awaitFirstDown()

              val filter = state.filters.firstOrNull {
                val distance = (it.center - firstDown.position).getDistance()
                abs(distance) <= 64.dp.toPx()
              } ?: return@awaitEachGesture

              onFilterMoved(filter.id, firstDown.position)

              drag(firstDown.id) {
                it.consume()
                onFilterMoved(filter.id, it.position)
              }

              onDragGestureStopped()
            }
          },
      ) {
        state.filters.forEach { filter ->
          drawCircle(
            color = filter.color,
            center = filter.center,
            radius = 64.dp.toPx(),
            blendMode = BlendMode.Difference,
          )
        }
      }
    }
  }
}
