package org.dandowney.aimless.feature.color.switcher

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dandowney.aimless.data.db.dao.SessionDao
import org.dandowney.aimless.data.db.entity.SessionEntity
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherEvent
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherSideEffect
import org.dandowney.aimless.feature.color.switcher.contract.ColorSwitcherState
import org.dandowney.aimless.library.architecture.BaseViewModel
import org.dandowney.aimless.library.design.color.random
import javax.inject.Inject

@HiltViewModel
class ColorSwitcherViewModel @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<ColorSwitcherEvent, ColorSwitcherState, ColorSwitcherSideEffect>() {

  private val startTime: Long = System.currentTimeMillis()
  private var colorSwitches = 0

  override fun createInitialState(): ColorSwitcherState {
    return ColorSwitcherState(
      availableColors = buildList {
        repeat(60) {
          add(Color.random())
        }
      },
      selectedColor = null,
    )
  }

  override suspend fun handleEvent(event: ColorSwitcherEvent) {
    when (event) {
      is ColorSwitcherEvent.ColorSelected -> colorSelected(event.color)
      is ColorSwitcherEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun colorSelected(color: Color) {
    incrementColorSwitches()
    setState {
      copy(selectedColor = color.takeIf { selectedColor != color })
    }
  }

  private suspend fun incrementColorSwitches() {
    colorSwitches++
    withContext(Dispatchers.IO) {
      val key = intPreferencesKey("color_switcher_color_switches")
      dataStore.edit { preferences ->
        preferences[key] = (preferences[key] ?: 0) + 1
      }
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      ColorSwitcherSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("color_switcher_session_details") {
      param("duration", endTime - startTime)
      param("color_switches", colorSwitches.toLong())
    }
    sessionDao.createSession(
      SessionEntity("color_switcher", startTime, endTime)
    )
  }

}
