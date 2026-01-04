package com.template.ui.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light")
@Preview(name = "Large Screen", device = Devices.PIXEL_TABLET)
@Preview(name = "API 30", apiLevel = 30)
annotation class Previews

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light")
annotation class ThemePreviews
