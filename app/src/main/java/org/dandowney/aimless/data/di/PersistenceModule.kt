package org.dandowney.aimless.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.dandowney.aimless.data.db.AimlessDatabase
import org.dandowney.aimless.data.db.dao.SessionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PersistenceModule {

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

  @Provides
  @Singleton
  fun database(
    @ApplicationContext context: Context,
  ): AimlessDatabase {
    return Room.databaseBuilder(
      context = context,
      klass = AimlessDatabase::class.java,
      name = "aimless_database",
    ).build()
  }

  @Provides
  @Reusable
  fun sessionDao(database: AimlessDatabase): SessionDao = database.sessionDao()

  @Provides
  @Reusable
  fun dataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

  companion object {
    private const val DATA_STORE_NAME = "aimless_general_data_store"
  }
}
