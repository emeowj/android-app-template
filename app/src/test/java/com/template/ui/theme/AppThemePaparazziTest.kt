package com.template.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

class AppThemePaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotColorTokensLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ColorPaletteSpecimen()
            }
        }
    }

    @Test
    fun snapshotColorTokensDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ColorPaletteSpecimen()
            }
        }
    }

    @Test
    fun snapshotTypographyEditorial() {
        paparazzi.snapshot {
            AppTheme(pairing = AppTypePairing.Editorial) {
                TypographySpecimen(pairingName = "Editorial (Roboto Serif + Hanken Grotesk)")
            }
        }
    }

    @Test
    fun snapshotTypographyLiterary() {
        paparazzi.snapshot {
            AppTheme(pairing = AppTypePairing.Literary) {
                TypographySpecimen(pairingName = "Literary (Cormorant Garamond + Hanken Grotesk)")
            }
        }
    }

    @Test
    fun snapshotTypographyModern() {
        paparazzi.snapshot {
            AppTheme(pairing = AppTypePairing.Modern) {
                TypographySpecimen(pairingName = "Modern (Space Grotesk + IBM Plex Sans)")
            }
        }
    }

    @Test
    fun snapshotDensityAndShapes() {
        paparazzi.snapshot {
            AppTheme {
                DensityAndShapesSpecimen()
            }
        }
    }
}

@Composable
private fun ColorPaletteSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = if (colors.isDark) "AppColors — Dark Mode (OKLab Derived)" else "AppColors — Light Mode",
                style = typography.titleMd,
                color = colors.ink,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ColorSwatch(name = "bg", color = colors.background, textColor = colors.ink)
                ColorSwatch(name = "surface", color = colors.surface, textColor = colors.ink)
                ColorSwatch(name = "ink", color = colors.ink, textColor = colors.background)
                ColorSwatch(name = "inkMuted", color = colors.inkMuted, textColor = colors.background)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ColorSwatch(name = "border", color = colors.border, textColor = colors.ink)
                ColorSwatch(name = "accent", color = colors.accent, textColor = Color.White)
                ColorSwatch(name = "danger", color = colors.danger, textColor = Color.White)
                ColorSwatch(name = "inkFixed", color = colors.inkFixed, textColor = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Alpha Ramps", style = typography.overline, color = colors.inkMuted)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AlphaSwatch(name = "hairline", color = colors.hairline)
                AlphaSwatch(name = "ink04", color = colors.ink04)
                AlphaSwatch(name = "ink08", color = colors.ink08)
                AlphaSwatch(name = "ink14", color = colors.ink14)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AlphaSwatch(name = "ink56", color = colors.ink56)
                AlphaSwatch(name = "inkSoft", color = colors.inkSoft)
                AlphaSwatch(name = "accent12", color = colors.accent12)
                AlphaSwatch(name = "danger12", color = colors.danger12)
            }
        }
    }
}

@Composable
private fun ColorSwatch(name: String, color: Color, textColor: Color) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Column(
        modifier = Modifier
            .width(80.dp)
            .background(color, RoundedCornerShape(AppShapes.SwatchRadius))
            .border(1.dp, colors.hairline, RoundedCornerShape(AppShapes.SwatchRadius))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.size(24.dp))
        Text(text = name, style = typography.caption, color = textColor)
    }
}

@Composable
private fun AlphaSwatch(name: String, color: Color) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Column(
        modifier = Modifier
            .width(80.dp)
            .background(colors.surface, RoundedCornerShape(AppShapes.SwatchRadius))
            .border(1.dp, colors.hairline, RoundedCornerShape(AppShapes.SwatchRadius))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, RoundedCornerShape(4.dp)),
        )
        Text(text = name, style = typography.caption, color = colors.ink, maxLines = 1)
    }
}

@Composable
private fun TypographySpecimen(pairingName: String) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = pairingName, style = typography.titleMd, color = colors.ink)
            Text(text = "KICKER OVERLINE", style = typography.overline, color = colors.accent)
            Text(text = "10:42", style = typography.displayClock, color = colors.ink)
            Text(text = "Title Large — Display 21sp", style = typography.titleLg, color = colors.ink)
            Text(text = "Title Medium — Display 19sp", style = typography.titleMd, color = colors.ink)
            Text(text = "Title Small — Display 18sp", style = typography.titleSm, color = colors.ink)
            Text(text = "Body Large — 15sp regular reading copy for buttons and hero items.", style = typography.bodyLg, color = colors.ink)
            Text(text = "Body Medium — 14sp standard secondary information.", style = typography.bodyMd, color = colors.inkMuted)
            Text(text = "Body Small — 13.5sp metadata text.", style = typography.bodySm, color = colors.inkMuted)
            Text(text = "Caption — 12.5sp hint and footnote copy.", style = typography.caption, color = colors.inkMuted)
            Text(text = "Numeric tabular: 1,234,567 / 98.6% (Seed: #A4B29E)", style = typography.numeric, color = colors.ink)
        }
    }
}

@Composable
private fun DensityAndShapesSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "Grouped List Shapes (listItemShape)", style = typography.titleSm, color = colors.ink)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                // Top item (24dp top, 6dp bottom)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface, AppShapes.listItemShape(index = 0, count = 3))
                        .border(1.dp, colors.hairline, AppShapes.listItemShape(index = 0, count = 3))
                        .padding(16.dp),
                ) {
                    Text(text = "Top Row (24dp cap, 6dp joint)", style = typography.bodyLg, color = colors.ink)
                }

                // Middle item (6dp all corners)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface, AppShapes.listItemShape(index = 1, count = 3))
                        .border(1.dp, colors.hairline, AppShapes.listItemShape(index = 1, count = 3))
                        .padding(16.dp),
                ) {
                    Text(text = "Middle Row (6dp joints)", style = typography.bodyLg, color = colors.ink)
                }

                // Bottom item (6dp top, 24dp bottom)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface, AppShapes.listItemShape(index = 2, count = 3))
                        .border(1.dp, colors.hairline, AppShapes.listItemShape(index = 2, count = 3))
                        .padding(16.dp),
                ) {
                    Text(text = "Bottom Row (6dp joint, 24dp cap)", style = typography.bodyLg, color = colors.ink)
                }
            }
        }
    }
}
