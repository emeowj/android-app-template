package com.template.screens.settings.components

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.ColorPreset
import com.template.ui.theme.Padding
import com.template.ui.theme.darkThemeFromSettings

sealed class ColorChoice {
    data object Dynamic : ColorChoice()

    data class Preset(val id: String) : ColorChoice()
}

private val SwatchShape = RoundedCornerShape(percent = 50)

@Composable
fun ColorChipGridContent(
    selected: ColorChoice,
    onSelect: (ColorChoice) -> Unit,
    modifier: Modifier = Modifier,
) {
    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val dynamicLabel = stringResource(R.string.settings_dynamic_color_title)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.navigationBarsPadding(),
        contentPadding = PaddingValues(horizontal = Padding.medium, vertical = Padding.small),
        horizontalArrangement = Arrangement.spacedBy(Padding.small),
        verticalArrangement = Arrangement.spacedBy(Padding.small),
    ) {
        if (supportsDynamic) {
            item(key = "dynamic") {
                ColorChip(
                    label = dynamicLabel,
                    selected = selected is ColorChoice.Dynamic,
                    seedColor = null,
                    isDynamic = true,
                    onClick = { onSelect(ColorChoice.Dynamic) },
                )
            }
        }

        items(items = ColorPreset.OPTIONS, key = { preset -> preset.id }) { preset ->
            ColorChip(
                label = stringResource(preset.nameRes),
                selected = selected is ColorChoice.Preset && selected.id == preset.id,
                seedColor = preset.color,
                isDynamic = false,
                onClick = { onSelect(ColorChoice.Preset(preset.id)) },
            )
        }
    }
}

@Composable
internal fun ColorChip(
    label: String,
    selected: Boolean,
    seedColor: Color?,
    isDynamic: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme =
        if (isDynamic || seedColor == null) {
            val context = LocalContext.current
            val isDark = darkThemeFromSettings()
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                    if (isDark) dynamicDarkColorScheme(context)
                    else dynamicLightColorScheme(context)

                isDark -> darkColorScheme()
                else -> lightColorScheme()
            }
        } else {
            rememberDynamicColorScheme(
                seedColor = seedColor,
                isDark = false,
                isAmoled = false,
                style = PaletteStyle.TonalSpot,
            )
        }

    Surface(
        onClick = onClick,
        shape = AppShape.card,
        color =
            if (selected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(Padding.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Padding.small),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Padding.extraSmall)) {
                listOf(
                    colorScheme.primary,
                    colorScheme.primaryContainer,
                    colorScheme.secondary,
                    colorScheme.tertiary,
                    colorScheme.surfaceContainer,
                )
                    .forEach { color ->
                        Surface(
                            color = color,
                            shape = SwatchShape,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.67f),
                            content = {},
                        )
                    }
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                color =
                    if (selected) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
            )
        }
    }
}

@Composable
@ThemePreviews
private fun ColorChipGridContentPreview() {
    AppPreview {
        ColorChipGridContent(
            selected = ColorChoice.Preset("cyan"),
            onSelect = {},
        )
    }
}
