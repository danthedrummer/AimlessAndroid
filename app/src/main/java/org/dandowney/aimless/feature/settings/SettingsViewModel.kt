package org.dandowney.aimless.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.dandowney.aimless.BuildConfig
import org.dandowney.aimless.feature.settings.contract.SettingsEvent
import org.dandowney.aimless.feature.settings.contract.SettingsSideEffect
import org.dandowney.aimless.feature.settings.contract.SettingsState
import org.dandowney.aimless.library.architecture.BaseViewModel
import org.dandowney.aimless.library.design.theme.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val themeRepository: ThemeRepository,
  private val dataStore: DataStore<Preferences>,
  private val firebaseAnalytics: FirebaseAnalytics,
) : BaseViewModel<SettingsEvent, SettingsState, SettingsSideEffect>() {

  init {
    viewModelScope.launch {
      themeRepository.darkTheme.collectLatest {
        setState { copy(isDarkTheme = it) }
      }
    }
  }

  override fun initialize() {
    super.initialize()

    if (!BuildConfig.DEBUG) return

    viewModelScope.launch {
      var details = emptyList<Pair<String, Any>>()
      dataStore.edit {
        details = it.asMap().map { (k, v) -> k.name to v }
      }
      setState {
        copy(analytics = details)
      }
    }
  }

  override fun createInitialState(): SettingsState {
    return runBlocking {
      SettingsState(
        title = "Settings",
        isDarkTheme = false,
        analytics = emptyList(),
        isDebug = BuildConfig.DEBUG,
      )
    }
  }

  override suspend fun handleEvent(event: SettingsEvent) {
    when (event) {
      is SettingsEvent.ToggleDarkTheme -> toggleDarkTheme(event.value)
      is SettingsEvent.PrivacyPolicyClicked -> privacyPolicyClicked()
    }
  }

  private suspend fun toggleDarkTheme(value: Boolean) {
    themeRepository.setDarkTheme(value)
    setState { copy(isDarkTheme = value) }
  }

  private fun privacyPolicyClicked() {
    firebaseAnalytics.logEvent("privacy_policy_clicked") {}
    setEffect {
      SettingsSideEffect.OpenPrivacyPolicy(uri = "https://www.dandowney.org/aimless-privacy-policy")
    }
  }
}
