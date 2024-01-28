package org.dandowney.aimless.feature.tactile.slot.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.feature.tactile.slot.SlotViewModel
import org.dandowney.aimless.feature.tactile.slot.contract.SlotEvent
import org.dandowney.aimless.feature.tactile.slot.contract.SlotSideEffect
import org.dandowney.aimless.feature.tactile.slot.contract.SlotState
import org.dandowney.aimless.library.design.container.AimlessScreen
import org.dandowney.aimless.library.design.haptic.playVibration
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun Slot(
  viewModel: SlotViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  val context = LocalContext.current
  LaunchedEffect(key1 = Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is SlotSideEffect.NavigateBack -> onNavigateBack()
        is SlotSideEffect.PlayVibration -> context.playVibration()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(SlotEvent.BackClicked)
  }
  val state by viewModel.state
  Slot(
    state = state,
    onBackClick = { viewModel.sendEvent(SlotEvent.BackClicked) },
    onPieceMoved = { viewModel.sendEvent(SlotEvent.PieceMoved(it)) },
    onCanvasReady = { size, shapeSize -> viewModel.sendEvent(SlotEvent.CanvasReady(size, shapeSize)) },
  )
}

@Composable
private fun Slot(
  state: SlotState,
  onBackClick: () -> Unit,
  onPieceMoved: (Offset) -> Unit,
  onCanvasReady: (Size, Size) -> Unit,
) {

  AimlessScreen(
    title = state.title,
    leadingIcon = AimlessTheme.icons.back,
    onLeadingIconClick = onBackClick,
  ) {
    val shapeColor = AimlessTheme.colors.iconPrimary
    val backgroundColor = AimlessTheme.colors.backgroundPrimary
    val shapeSize = remember { 96.dp }
    val strokeSize = remember { 16.dp }
    val alpha by animateFloatAsState(
      targetValue = if (state.solved) 0F else 1F,
      label = "",
      animationSpec = tween(1_000),
    )

    Box(
      modifier = Modifier
        .fillMaxSize()
        .drawBehind {
          if (state.target == null && state.piece == null) {
            onCanvasReady(size, Size(shapeSize.toPx(), shapeSize.toPx()))
          }
        },
    ) {
      state.target?.let { targetPosition ->
        TargetCanvas(
          position = targetPosition,
          alpha = alpha,
          shapeColor = shapeColor,
          shapeSize = shapeSize,
          strokeSize = strokeSize,
          backgroundColor = backgroundColor,
          solved = state.solved,
        )
      }

      state.piece?.let { piecePosition ->
        PieceCanvas(
          position = piecePosition,
          alpha = alpha,
          shapeColor = shapeColor,
          shapeSize = shapeSize,
          strokeSize = strokeSize,
          onPieceMoved = onPieceMoved,
          solved = state.solved,
        )
      }
    }
  }
}

@Composable
private fun TargetCanvas(
  position: Offset,
  solved: Boolean,
  alpha: Float,
  shapeColor: Color,
  backgroundColor: Color,
  shapeSize: Dp,
  strokeSize: Dp,
) {
  Canvas(
    modifier = Modifier
      .fillMaxSize()
      .alpha(alpha = alpha)
  ) {
    val shapeSizePx = shapeSize.toPx()
    val cornerRadiusPercentage = 0.2F
    val strokePx = strokeSize.toPx()

    val destinationSize = Size(shapeSizePx, shapeSizePx)
    val targetSize = Size(shapeSizePx - (strokePx * 2), shapeSizePx - (strokePx * 2))

    drawRoundRect(
      color = shapeColor,
      cornerRadius = CornerRadius(shapeSizePx * cornerRadiusPercentage),
      size = destinationSize,
      topLeft = position,
    )

    if (!solved) {
      drawRoundRect(
        color = backgroundColor,
        cornerRadius = CornerRadius((shapeSizePx - (strokePx * 2)) * cornerRadiusPercentage),
        size = targetSize,
        topLeft = position + Offset(strokePx, strokePx),
      )
    }
  }
}

@Composable
private fun PieceCanvas(
  position: Offset,
  onPieceMoved: (Offset) -> Unit,
  alpha: Float,
  solved: Boolean,
  shapeColor: Color,
  shapeSize: Dp,
  strokeSize: Dp,
) {
  if (solved) return

  Canvas(
    modifier = Modifier
      .fillMaxSize()
      .alpha(alpha = alpha)
      .pointerInput(Unit) {
        awaitEachGesture {
          val firstDown = awaitFirstDown()

          onPieceMoved(firstDown.position)

          drag(firstDown.id) {
            it.consume()
            onPieceMoved(it.position)
          }
        }
      },
  ) {
    val shapeSizePx = shapeSize.toPx()
    val cornerRadiusPercentage = 0.2F
    val strokePx = strokeSize.toPx()

    val targetSize = Size(shapeSizePx - (strokePx * 2), shapeSizePx - (strokePx * 2))

    drawRoundRect(
      color = shapeColor,
      cornerRadius = CornerRadius((shapeSizePx - (strokePx * 2)) * cornerRadiusPercentage),
      size = targetSize,
      topLeft = position - Offset(targetSize.width / 2, targetSize.height / 2),
    )
  }
}
