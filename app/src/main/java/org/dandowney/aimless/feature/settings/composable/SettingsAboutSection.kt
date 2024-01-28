package org.dandowney.aimless.feature.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.text.ThemedText
import org.dandowney.aimless.library.design.theme.AimlessTheme

fun LazyListScope.settingsAboutSection(
  onPrivacyPolicyClicked: () -> Unit,
) {
  item {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(
          elevation = 4.dp,
          shape = AimlessTheme.shapes.mediumRoundedCornerShape,
        )
        .background(
          color = AimlessTheme.colors.backgroundPrimary,
          shape = AimlessTheme.shapes.mediumRoundedCornerShape,
        )
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp),
    ) {
      ThemedText(
        text = "About",
        style = AimlessTheme.typography.h4,
        modifier = Modifier.padding(bottom = 8.dp),
      )

      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
          .background(color = AimlessTheme.colors.iconPrimary.copy(alpha = 0.2F)),
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .padding(end = 16.dp)
          .aimlessClickable(onClick = onPrivacyPolicyClicked),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        ThemedText(
          text = "Privacy Policy",
          style = AimlessTheme.typography.body2,
        )

        Icon(
          painter = AimlessTheme.icons.link,
          contentDescription = "Link to privacy policy",
          tint = AimlessTheme.colors.iconPrimary,
          modifier = Modifier.size(32.dp),
        )
      }
    }
  }

}
