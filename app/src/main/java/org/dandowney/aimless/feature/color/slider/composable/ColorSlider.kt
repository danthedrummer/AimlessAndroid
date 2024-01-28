package org.dandowney.aimless.feature.color.slider.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.feature.color.slider.ColorSliderViewModel
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderChannelState
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderEvent
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderSideEffect
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderState
import org.dandowney.aimless.library.design.color.DarkColorPalette
import org.dandowney.aimless.library.design.color.LightColorPalette
import org.dandowney.aimless.library.design.color.isDark
import org.dandowney.aimless.library.design.container.AimlessScreen
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun ColorSlider(
  viewModel: ColorSliderViewModel = hiltViewModel(),
  mode: String,
  onNavigateBack: () -> Unit,
) {
  LaunchedEffect(key1 = Unit) {
    viewModel.sendEvent(ColorSliderEvent.InitialiseWithMode(mode = mode))
    viewModel.effect.collect { effect ->
      when (effect) {
        is ColorSliderSideEffect.NavigateBack -> onNavigateBack()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(ColorSliderEvent.BackClicked)
  }

  val state by viewModel.state
  ColorSlider(
    state = state,
    onBackClick = { viewModel.sendEvent(ColorSliderEvent.BackClicked) },
    onChannelValueChange = { channel, value ->
      viewModel.sendEvent(ColorSliderEvent.ChannelValueChange(channel, value))
    },
    onModeSelected = { viewModel.sendEvent(ColorSliderEvent.OnModeSelected(it)) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorSlider(
  state: ColorSliderState,
  onBackClick: () -> Unit,
  onChannelValueChange: (ColorSliderChannelState, Float) -> Unit,
  onModeSelected: (String) -> Unit,
) {
  val darkBackground = state.color.isDark()
  val iconColor by animateColorAsState(
    targetValue = if (darkBackground) DarkColorPalette.iconPrimary else LightColorPalette.iconPrimary,
    label = "",
    animationSpec = tween(500)
  )
  AimlessScreen(
    title = "Colour Mixer",
    leadingIcon = AimlessTheme.icons.back,
    onLeadingIconClick = onBackClick,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = state.color),
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .padding(horizontal = 64.dp),
        verticalArrangement = spacedBy(32.dp),
      ) {
        items(items = state.channels) { channel ->
          Slider(
            value = channel.value,
            onValueChange = { newValue -> onChannelValueChange(channel, newValue) },
            colors = if (state.color.isDark()) darkSliderColors else lightSliderColors,
            thumb = {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .shadow(
                    elevation = 4.dp,
                    shape = AimlessTheme.shapes.mediumRoundedCornerShape,
                  )
                  .background(
                    color = iconColor,
                    shape = AimlessTheme.shapes.mediumRoundedCornerShape,
                  )
              )
            }
          )
        }
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .padding(bottom = 32.dp),
            horizontalArrangement = spacedBy(16.dp),
          ) {

            state.modeButtons.forEach { modeButton ->
              val textColor by animateColorAsState(
                targetValue = when {
                  (darkBackground && modeButton.isSelected) || !darkBackground && !modeButton.isSelected -> DarkColorPalette.textButton
                  else -> LightColorPalette.textButton
                },
                label = "",
                animationSpec = tween(500)
              )
              Box(
                modifier = Modifier
                  .height(64.dp)
                  .fillMaxWidth()
                  .weight(1F)
                  .shadow(
                    elevation = if (modeButton.isSelected) 4.dp else 0.dp,
                    shape = AimlessTheme.shapes.mediumRoundedCornerShape,
                  )
                  .background(
                    color = if (modeButton.isSelected) iconColor else Color.Transparent,
                    shape = AimlessTheme.shapes.mediumRoundedCornerShape,
                  )
                  .border(
                    width = 2.dp,
                    color = iconColor,
                    shape = AimlessTheme.shapes.mediumRoundedCornerShape,
                  ),
                contentAlignment = Alignment.Center,
              ) {
                Text(
                  text = modeButton.name.uppercase(),
                  color = textColor,
                  style = AimlessTheme.typography.button,
                  modifier = Modifier
                    .aimlessClickable { onModeSelected(modeButton.name) },
                )
              }
            }
          }
        }
      }
    }
  }
}
