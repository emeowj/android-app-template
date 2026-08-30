package com.template.screens.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import java.util.Locale

@Composable
fun SettingsSectionKicker(
    title: String,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val densityTokens = LocalAppDensity.current

    Text(
        text = title.uppercase(Locale.getDefault()),
        style = typography.overline,
        color = colors.inkMuted,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = densityTokens.screenPadding)
            .padding(top = Padding.xl, bottom = Padding.sm),
    )
}

@ThemePreviews
@Composable
private fun SettingsSectionKickerPreview() {
    AppPreview {
        SettingsSectionKicker(title = "Appearance & Theming")
    }
}
