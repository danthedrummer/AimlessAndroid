package org.dandowney.aimless.library.design.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.dandowney.aimless.library.design.theme.ThemeRepository
import org.dandowney.aimless.library.design.theme.AimlessThemeRepository

@Module
@InstallIn(SingletonComponent::class)
interface ThemeModule {

  @Binds
  fun themeRepository(aimlessThemeRepository: AimlessThemeRepository): ThemeRepository
}
