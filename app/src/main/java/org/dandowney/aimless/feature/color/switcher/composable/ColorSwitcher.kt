package org.dandowney.aimless.feature.color.switcher.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.feature.color.switcher.ColorSwitcherViewModel
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherEvent
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherSideEffect
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherState
import org.dandowney.aimless.library.design.color.isDark
import org.dandowney.aimless.library.design.header.Header
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.theme.AimlessTheme

private const val ANIMATION_DURATION = 1_200

@Composable
fun ColorSwitcher(
  viewModel: ColorSwitcherViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  LaunchedEffect(key1 = Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ColorSwitcherSideEffect.NavigateBack -> onNavigateBack()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(ColorSwitcherEvent.BackClicked)
  }
  val state by viewModel.state
  ColorSwitcher(
    state = state,
    onBackClicked = { viewModel.sendEvent(ColorSwitcherEvent.BackClicked) },
    onColorSelected = { viewModel.sendEvent(ColorSwitcherEvent.ColorSelected(it)) },
  )
}

@Composable
private fun ColorSwitcher(
  state: ColorSwitcherState,
  onBackClicked: () -> Unit,
  onColorSelected: (Color) -> Unit,
) {
  val backgroundColor by animateColorAsState(
    targetValue = state.selectedColor ?: AimlessTheme.colors.backgroundPrimary,
    label = "Animating the background to the selected color",
    animationSpec = tween(ANIMATION_DURATION)
  )
  val borderColor by animateColorAsState(
    targetValue = if ((state.selectedColor ?: AimlessTheme.colors.backgroundPrimary).isDark()) {
      Color.White
    } else {
      Color.Black
    },
    label = "Animating the border color for the selector buttons",
    animationSpec = tween(ANIMATION_DURATION),
  )
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color = backgroundColor),
    contentAlignment = Alignment.BottomCenter,
  ) {

    Header(
      leadingIcon = AimlessTheme.icons.back,
      onLeadingIconClick = onBackClicked,
      title = "Colour Switcher",
      modifier = Modifier.align(Alignment.TopCenter),
    )

    LazyHorizontalGrid(
      rows = GridCells.Fixed(3),
      modifier = Modifier
        .height(320.dp)
        .padding(bottom = 32.dp),
      horizontalArrangement = spacedBy(16.dp),
      verticalArrangement = spacedBy(16.dp),
      contentPadding = PaddingValues(horizontal = 32.dp),
    ) {
      items(items = state.availableColors) { color ->
        val elevation by animateDpAsState(
          targetValue = if (state.selectedColor == color) 0.dp else 4.dp,
          label = "Animating the color selector elevation",
          animationSpec = tween(ANIMATION_DURATION),
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1F)
            .shadow(
              elevation = elevation,
              shape = AimlessTheme.shapes.largeRoundedCornerShape,
            )
            .background(
              color = color,
              shape = AimlessTheme.shapes.largeRoundedCornerShape,
            )
            .border(
              color = borderColor.copy(alpha = 0F),
              shape = AimlessTheme.shapes.largeRoundedCornerShape,
              width = 1.dp,
            )
            .aimlessClickable { onColorSelected(color) },
        )
      }
    }
  }
}
