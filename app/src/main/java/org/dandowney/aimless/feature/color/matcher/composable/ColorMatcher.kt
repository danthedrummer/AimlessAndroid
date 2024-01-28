package org.dandowney.aimless.feature.color.matcher.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.feature.color.matcher.ColorMatcherViewModel
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherEvent
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherSideEffect
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherState
import org.dandowney.aimless.feature.color.matcher.contract.ColorOptionState
import org.dandowney.aimless.library.design.container.AimlessScreen
import org.dandowney.aimless.library.design.haptic.playVibration
import org.dandowney.aimless.library.design.modifier.reactiveShadow
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun ColorMatcher(
  viewModel: ColorMatcherViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  val context = LocalContext.current
  LaunchedEffect(key1 = Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ColorMatcherSideEffect.NavigateBack -> onNavigateBack()
        is ColorMatcherSideEffect.PlayVibration -> context.playVibration()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(ColorMatcherEvent.BackClicked)
  }
  val state by viewModel.state
  ColorMatcher(
    state = state,
    onBackClicked = { viewModel.sendEvent(ColorMatcherEvent.BackClicked) },
    onColorPressed = { viewModel.sendEvent(ColorMatcherEvent.ColorPressed(it)) }
  )
}

@Composable
private fun ColorMatcher(
  state: ColorMatcherState,
  onBackClicked: () -> Unit,
  onColorPressed: (ColorOptionState) -> Unit,
) {
  AimlessScreen(
    title = "Colour Matcher",
    onLeadingIconClick = onBackClicked,
    leadingIcon = AimlessTheme.icons.back,
  ) {

    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      MatcherContents(state = state, onColorPressed = onColorPressed)

      ResetOverlay(state = state)
    }
  }
}

@Composable
fun MatcherContents(
  state: ColorMatcherState,
  onColorPressed: (ColorOptionState) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    contentPadding = PaddingValues(32.dp),
    verticalArrangement = spacedBy(32.dp, alignment = Alignment.CenterVertically),
    horizontalArrangement = spacedBy(32.dp),
  )
  {
    item(span = { GridItemSpan(3) }) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(3F)
          .reactiveShadow(
            elevation = 8.dp,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
            surfaceColor = AimlessTheme.colors.backgroundPrimary,
          )
          .background(
            color = state.colorTarget,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
          ),
      )
    }

    items(items = state.colorOptions) { colorOption ->
      val elevation by animateDpAsState(
        targetValue = if (colorOption.isPressed) 8.dp else 2.dp,
        label = "",
      )
      val padding by animateDpAsState(
        targetValue = if (colorOption.isPressed) 0.dp else 8.dp,
        label = "",
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1F)
          .padding(padding)
          .reactiveShadow(
            elevation = elevation,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
            surfaceColor = AimlessTheme.colors.backgroundPrimary,
          )
          .background(
            color = colorOption.color,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
          )
          .aimlessClickable {
            onColorPressed(colorOption)
          },
      )
    }

    item(span = { GridItemSpan(3) }) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(3F)
          .reactiveShadow(
            elevation = 8.dp,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
            surfaceColor = AimlessTheme.colors.backgroundPrimary,
          )
          .background(
            color = state.selectedColor,
            shape = AimlessTheme.shapes.largeRoundedCornerShape,
          ),
      )
    }
  }
}

@Composable
fun ResetOverlay(state: ColorMatcherState) {
  val alpha by animateFloatAsState(
    targetValue = if (state.isResetting) 1F else 0F,
    animationSpec = tween(1_000),
    label = "",
  )
  Box(
    modifier = Modifier
      .fillMaxSize()
      .alpha(alpha)
      .background(color = AimlessTheme.colors.backgroundPrimary)
  )
}
