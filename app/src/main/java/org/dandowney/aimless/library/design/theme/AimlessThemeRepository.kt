package org.dandowney.aimless.library.design.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AimlessThemeRepository @Inject constructor(
  @ApplicationContext private val context: Context,
) : ThemeRepository {

  private val _darkTheme = MutableStateFlow(true)
  override val darkTheme: StateFlow<Boolean> = _darkTheme

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

  init {
    runBlocking {
      _darkTheme.value = isDarkTheme()
    }
  }

  override suspend fun isDarkTheme(): Boolean {
    return withContext(Dispatchers.IO) {
      val key = booleanPreferencesKey(name = KEY_DARK_THEME)
      context.dataStore.data.first()[key] ?: false
    }
  }

  override suspend fun setDarkTheme(isDarkTheme: Boolean) {
    withContext(Dispatchers.IO) {
      val key = booleanPreferencesKey(name = KEY_DARK_THEME)
      context.dataStore.edit { preferences ->
        preferences[key] = isDarkTheme
      }
    }
    _darkTheme.value = isDarkTheme
  }

  companion object {
    private const val DATA_STORE_NAME = "aimless_theme_data_store"
    private const val KEY_DARK_THEME = "dark_theme"
  }
}
