package org.dandowney.aimless.feature.tactile.clickers

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
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickerItemState
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersEvent
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersSideEffect
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersState
import org.dandowney.aimless.library.architecture.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ClickersViewModel @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<ClickersEvent, ClickersState, ClickersSideEffect>() {

  private val startTime: Long = System.currentTimeMillis()
  private var clicks: Int = 0
  private var completions: Int = 0

  override fun createInitialState(): ClickersState {
    return ClickersState(
      resetTimer = 1_000,
      clickers = buildList {
        repeat(12) {
          add(ClickerItemState(id = it, isPressed = false))
        }
      },
    )
  }

  override suspend fun handleEvent(event: ClickersEvent) {
    when (event) {
      is ClickersEvent.ClickedPressed -> clickerPressed(event.id)
      is ClickersEvent.ClickerUnpressed -> clickerUnpressed(event.id)
      is ClickersEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun clickerPressed(id: Int) {
    val updatedClickers = state.value.clickers.map {
      if (it.id == id) it.copy(isPressed = true) else it
    }
    setState {
      copy(clickers = updatedClickers)
    }

    updateAnalytics(gameCompleted = updatedClickers.all { it.isPressed })
  }

  private suspend fun updateAnalytics(gameCompleted: Boolean) {
    withContext(Dispatchers.IO) {
      val totalClicksKey = intPreferencesKey("clickers_total_clicks")
      dataStore.edit { preferences ->
        preferences[totalClicksKey] = (preferences[totalClicksKey] ?: 0) + 1
        if (gameCompleted) {
          val totalCompletionsKey = intPreferencesKey("clickers_total_completions")
          preferences[totalCompletionsKey] = (preferences[totalCompletionsKey] ?: 0) + 1
        }
      }
    }
  }

  private suspend fun clickerUnpressed(id: Int) {
    val updatedClickers = state.value.clickers.map {
      if (it.id == id) it.copy(isPressed = false) else it
    }
    setState {
      copy(clickers = updatedClickers)
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      ClickersSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("clickers_session_details") {
      param("duration", endTime - startTime)
      param("clicks", clicks.toLong())
      param("completions", completions.toLong())
    }
    sessionDao.createSession(
      SessionEntity("clickers", startTime, endTime)
    )
  }
}
