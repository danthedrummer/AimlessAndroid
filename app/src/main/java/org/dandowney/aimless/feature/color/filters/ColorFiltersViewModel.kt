package org.dandowney.aimless.feature.color.filters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersEvent
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersSideEffect
import org.dandowney.aimless.feature.color.filters.contract.ColorFiltersState
import org.dandowney.aimless.feature.color.filters.contract.FilterState
import org.dandowney.aimless.library.architecture.BaseViewModel
import org.dandowney.aimless.library.design.color.random
import javax.inject.Inject
import kotlin.random.Random.Default.nextFloat

@HiltViewModel
class ColorFiltersViewModel @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val sessionDao: SessionDao,
) : BaseViewModel<ColorFiltersEvent, ColorFiltersState, ColorFiltersSideEffect>() {

  private val startTime = System.currentTimeMillis()
  private var filtersMoved = 0

  override fun createInitialState(): ColorFiltersState {
    return ColorFiltersState(
      filters = emptyList(),
      touchInputKey = System.currentTimeMillis(),
    )
  }

  override suspend fun handleEvent(event: ColorFiltersEvent) {
    when (event) {
      is ColorFiltersEvent.CanvasReady -> canvasReady(event.size)
      is ColorFiltersEvent.FilterMoved -> filterMoved(event.filterId, event.newPosition)
      is ColorFiltersEvent.DragGestureStopped -> dragGestureStopped()
      is ColorFiltersEvent.BackClicked -> backClicked()
    }
  }

  private suspend fun canvasReady(size: Size) {
    setState {
      val filters = (0..5).map {
        FilterState(
          id = it.toString(),
          color = Color.random(),
          center = Offset(
            x = nextFloat() * size.width,
            y = nextFloat() * size.height,
          ),
        )
      }
      copy(
        filters = filters,
        touchInputKey = System.currentTimeMillis(),
      )
    }
  }

  private suspend fun filterMoved(filterId: String, newPosition: Offset) {
    setState {
      copy(
        filters = filters.map {
          if (it.id == filterId) {
            it.copy(center = newPosition)
          } else {
            it
          }
        }
      )
    }
  }

  private suspend fun incrementFiltersMoved() {
    filtersMoved++
    withContext(Dispatchers.IO) {
      val key = intPreferencesKey("color_filters_filters_moved")
      dataStore.edit { preferences ->
        preferences[key] = (preferences[key] ?: 0) + 1
      }
    }
  }

  private suspend fun dragGestureStopped() {
    incrementFiltersMoved()
    setState {
      copy(touchInputKey = System.currentTimeMillis())
    }
  }

  private suspend fun backClicked() {
    val endTime = System.currentTimeMillis()
    setEffect {
      ColorFiltersSideEffect.NavigateBack
    }
    firebaseAnalytics.logEvent("color_filters_session_details") {
      param("duration", endTime - startTime)
      param("filters_moved", filtersMoved.toLong())
    }
    sessionDao.createSession(
      SessionEntity("color_filters", startTime, endTime)
    )
  }
}
