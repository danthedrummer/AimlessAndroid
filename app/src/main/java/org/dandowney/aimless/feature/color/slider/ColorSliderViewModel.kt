package org.dandowney.aimless.feature.color.slider

import androidx.compose.ui.graphics.Color
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.dandowney.aimless.data.db.dao.SessionDao
import org.dandowney.aimless.data.db.entity.SessionEntity
import org.dandowney.aimless.feature.color.slider.contract.ColorModeButton
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderChannelState
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderEvent
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderSideEffect
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderState
import org.dandowney.aimless.library.architecture.BaseViewModel
import org.dandowney.aimless.library.design.color.random
import javax.inject.Inject

@HiltViewModel
class ColorSliderViewModel @Inject constructor(
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<ColorSliderEvent, ColorSliderState, ColorSliderSideEffect>() {

  private val startTime: Long = System.currentTimeMillis()

  private lateinit var strategy: ColorSliderStrategy

  override fun createInitialState(): ColorSliderState = ColorSliderState(
    channels = emptyList(),
    color = Color.Transparent,
    modeButtons = listOf(
      ColorModeButton(name = "rgb", isSelected = true),
      ColorModeButton(name = "hsv", isSelected = false),
      ColorModeButton(name = "cmyk", isSelected = false),
    )
  )

  override suspend fun handleEvent(event: ColorSliderEvent) {
    when (event) {
      is ColorSliderEvent.ChannelValueChange -> channelValueChange(event.channel, event.newValue)
      is ColorSliderEvent.InitialiseWithMode -> initializeWithMode(event.mode)
      is ColorSliderEvent.OnModeSelected -> colorModeSelected(event.mode)
      is ColorSliderEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun initializeWithMode(mode: String) {
    strategy = ColorSliderStrategy.from(mode)
    strategy.setColor(Color.random())
    setState {
      copy(
        channels = strategy.channels,
        color = strategy.getColor(),
        modeButtons = modeButtons.map {
          it.copy(isSelected = it.name == mode)
        }
      )
    }
  }

  private suspend fun channelValueChange(
    channelState: ColorSliderChannelState,
    newValue: Float,
  ) {
    strategy.updateChannel(channelState, newValue)
    setState {
      copy(channels = strategy.channels, color = strategy.getColor())
    }
  }

  private suspend fun colorModeSelected(mode: String) {
    // Only tracking for new selections to reduce noise
    if (state.value.modeButtons.firstOrNull { it.isSelected }?.name != mode) {
      firebaseAnalytics.logEvent("color_slider_mode_selected") {
        param("mode", mode)
      }
    }

    val currentColor = strategy.getColor()
    strategy = ColorSliderStrategy.from(mode)
    strategy.setColor(currentColor)
    setState {
      ColorSliderState(
        channels = strategy.channels,
        color = currentColor,
        modeButtons = modeButtons.map {
          it.copy(isSelected = it.name == mode)
        }
      )
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      ColorSliderSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("color_slider_session_details") {
      param("duration", endTime - startTime)
    }
    sessionDao.createSession(
      SessionEntity("color_mixer", startTime, endTime)
    )
  }
}
