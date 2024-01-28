package org.dandowney.aimless.feature.tactile.slot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
import org.dandowney.aimless.feature.tactile.slot.contract.SlotEvent
import org.dandowney.aimless.feature.tactile.slot.contract.SlotSideEffect
import org.dandowney.aimless.feature.tactile.slot.contract.SlotState
import org.dandowney.aimless.library.architecture.BaseViewModel
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random.Default.nextFloat

@HiltViewModel
class SlotViewModel @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<SlotEvent, SlotState, SlotSideEffect>() {

  private var size: Size = Size(0F, 0F)
  private var shapeSize: Size = Size(0F, 0F)
  private var resetting: Boolean = false

  private var startTime: Long = System.currentTimeMillis()
  private var score = 0

  override fun createInitialState(): SlotState = SlotState(
    title = "Shape Slot",
    target = null,
    piece = null,
    solved = false,
  )

  override suspend fun handleEvent(event: SlotEvent) {
    when (event) {
      is SlotEvent.CanvasReady -> canvasReady(event.size, event.shapeSize)
      is SlotEvent.PieceMoved -> pieceMoved(event.position)
      is SlotEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun canvasReady(size: Size, shapeSize: Size) {
    this.size = size
    this.shapeSize = shapeSize
    setState {
      copy(
        target = randomOffset(size, shapeSize),
        piece = randomOffset(size, shapeSize),
      )
    }
  }

  private fun randomOffset(size: Size, shapeSize: Size): Offset {
    return Offset(
      ((size.width - (shapeSize.width * 2)) * nextFloat()) + shapeSize.width,
      ((size.height - (shapeSize.height * 2)) * nextFloat()) + shapeSize.height,
    )
  }

  private suspend fun pieceMoved(position: Offset) {
    if (resetting) return

    val pieceRect = Rect(
      state.value.piece!! - Offset(
        shapeSize.width / 2F,
        shapeSize.width / 2F
      ),
      shapeSize,
    )
    if (position !in pieceRect) return

    var solved = false
    setState {
      solved = target != null &&
          abs(position.x - (target.x + (shapeSize.width / 2F))) < 20F &&
          abs(position.y - (target.y + (shapeSize.height / 2F))) < 20F
      copy(
        piece = if (solved) target!! + Offset(shapeSize.width / 2F, shapeSize.height / 2F) else position,
        solved = solved,
      )
    }

    if (solved) {
      resetGame()
      incrementTotalScore()
      updateHighScore()

      setEffect {
        SlotSideEffect.PlayVibration
      }
    }
  }

  private suspend fun incrementTotalScore() {
    score++
    withContext(Dispatchers.IO) {
      val totalScoreKey = intPreferencesKey(name = "shape_slot_total_score")
      dataStore.edit { preferences ->
        preferences[totalScoreKey] = (preferences[totalScoreKey] ?: 0) + 1
      }
    }
  }

  private suspend fun updateHighScore() {
    withContext(Dispatchers.IO) {
      val highScoreKey = intPreferencesKey(name = "shape_slot_high_score")
      dataStore.edit { preferences ->
        preferences[highScoreKey] = max((preferences[highScoreKey] ?: 0), score)
      }
    }
  }

  private fun resetGame() {
    viewModelScope.launch {
      resetting = true
      delay(1_000L)
      setState {
        copy(
          target = randomOffset(size, shapeSize),
          piece = randomOffset(size, shapeSize),
          solved = false,
        )
      }
      resetting = false
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      SlotSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("shape_slot_session_details") {
      param("duration", endTime - startTime)
      param("score", score.toLong())
    }
    sessionDao.createSession(
      SessionEntity("shape_slot", startTime, endTime)
    )
  }
}
