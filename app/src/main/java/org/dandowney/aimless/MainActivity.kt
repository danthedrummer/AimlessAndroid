package org.dandowney.aimless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.dandowney.aimless.library.design.theme.AimlessTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AimlessTheme {
        AimlessApp()
      }
    }
  }
}
