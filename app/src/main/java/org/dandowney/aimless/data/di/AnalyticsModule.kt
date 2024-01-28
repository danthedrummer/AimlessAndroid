package org.dandowney.aimless.data.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class AnalyticsModule {

  @Provides
  @Reusable
  fun firebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}
