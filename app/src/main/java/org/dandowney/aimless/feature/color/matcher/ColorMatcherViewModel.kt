package org.dandowney.aimless.feature.color.matcher

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dandowney.aimless.data.db.dao.SessionDao
import org.dandowney.aimless.data.db.entity.SessionEntity
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherEvent
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherSideEffect
import org.dandowney.aimless.feature.color.matcher.contract.ColorMatcherState
import org.dandowney.aimless.feature.color.matcher.contract.ColorOptionState
import org.dandowney.aimless.library.architecture.BaseViewModel
import org.dandowney.aimless.library.design.color.random
import javax.inject.Inject
import kotlin.math.max
import kotlin.random.Random.Default.nextInt

@HiltViewModel
class ColorMatcherViewModel @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<ColorMatcherEvent, ColorMatcherState, ColorMatcherSideEffect>() {

  private val startTime = System.currentTimeMillis()
  private var clicks = 0
  private var score = 0

  override fun createInitialState(): ColorMatcherState {
    val colors = buildList<Color> {
      add(Color.random())
      do {
        val color = Color.random()
        // We want all of the colors to have somewhat distinct values for rgb so the game is not
        // full of extremely similar or even identical color components.
        if (this.none { it similarTo color }) {
          add(color)
        }
      } while (size < 3)
    }

    val colorOptions = buildList {
      val reds = colors.map { it.copy(green = 0F, blue = 0F) }.shuffled().toMutableList()
      val greens = colors.map { it.copy(red = 0F, blue = 0F) }.shuffled().toMutableList()
      val blues = colors.map { it.copy(red = 0F, green = 0F) }.shuffled().toMutableList()

      repeat(3) {
        add(
          ColorOptionState(
            channel = "red",
            color = reds.removeFirst(),
            isPressed = false,
          )
        )
        add(
          ColorOptionState(
            channel = "green",
            color = greens.removeFirst(),
            isPressed = false,
          )
        )
        add(
          ColorOptionState(
            channel = "blue",
            color = blues.removeFirst(),
            isPressed = false,
          )
        )
      }
    }

    return ColorMatcherState(
      colorTarget = colors[nextInt(3)],
      selectedColor = Color.White,
      colorOptions = colorOptions,
      isResetting = false,
    )
  }

  private infix fun Color.similarTo(other: Color): Boolean {
    // This value causes the shift to be +/- 10% and can't really be safely increased since we
    // could end up with an infinite loop trying to find a color that can't exist.
    val diff = 0.1F
    return red in other.red.shiftingRange(diff) ||
        green in other.green.shiftingRange(diff) ||
        blue in other.blue.shiftingRange(diff)
  }

  /**
   * Shifts a floating point range to coerce the values to be within the min and max values.
   */
  private fun Float.shiftingRange(
    diff: Float,
    min: Float = 0F,
    max: Float = 1F,
  ): ClosedFloatingPointRange<Float> {
    if (diff < 0F || diff > 1F) error("diff should be between 0 and 1")

    val range = (max - min) * diff

    val bottom = this - range
    val top = this + range

    val bottomShift = max(min - bottom, min)
    val topShift = max - max(top, max)

    val shiftedBottom = bottom + bottomShift + topShift
    val shiftedTop = top + bottomShift + topShift
    return shiftedBottom..shiftedTop
  }

  override suspend fun handleEvent(event: ColorMatcherEvent) {
    when (event) {
      is ColorMatcherEvent.ColorPressed -> colorPressed(event.optionState)
      is ColorMatcherEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun colorPressed(optionState: ColorOptionState) {
    setState {
      val updatedOptions = colorOptions.map {
        if (it.channel == optionState.channel) {
          it.copy(isPressed = !it.isPressed && it.color == optionState.color)
        } else {
          it
        }
      }
      copy(
        colorOptions = updatedOptions,
        selectedColor = Color(
          red = updatedOptions.firstOrNull { it.channel == "red" && it.isPressed }?.color?.red ?: 0F,
          green = updatedOptions.firstOrNull { it.channel == "green" && it.isPressed }?.color?.green ?: 0F,
          blue = updatedOptions.firstOrNull { it.channel == "blue" && it.isPressed }?.color?.blue ?: 0F,
        ),
      )
    }

    incrementTotalClicks()

    if (state.value.selectedColor == state.value.colorTarget) {
      incrementTotalScore()
      updateHighScore()
      setEffect {
        ColorMatcherSideEffect.PlayVibration
      }
      viewModelScope.launch {
        setState {
          copy(isResetting = true)
        }
        delay(1_000)
        setState {
          createInitialState()
        }
      }
    }
  }

  private suspend fun incrementTotalClicks() {
    clicks++
    withContext(Dispatchers.IO) {
      val key = intPreferencesKey("color_matcher_total_clicks")
      dataStore.edit { preferences ->
        preferences[key] = (preferences[key] ?: 0) + 1
      }
    }
  }

  private suspend fun updateHighScore() {
    withContext(Dispatchers.IO) {
      val key = intPreferencesKey("color_matcher_high_score")
      dataStore.edit { preferences ->
        preferences[key] = max((preferences[key] ?: 0), score)
      }
    }
  }

  private suspend fun incrementTotalScore() {
    score++
    withContext(Dispatchers.IO) {
      val key = intPreferencesKey("color_matcher_total_score")
      dataStore.edit { preferences ->
        preferences[key] = (preferences[key] ?: 0) + 1
      }
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      ColorMatcherSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("color_matcher_session_details") {
      param("duration", endTime - startTime)
      param("clicks", clicks.toLong())
      param("score", score.toLong())
    }
    sessionDao.createSession(
      SessionEntity("color_matcher", startTime, endTime)
    )
  }
}
