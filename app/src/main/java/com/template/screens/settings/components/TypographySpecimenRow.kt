package com.template.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.AppTypePairing
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding

@Composable
fun TypographySpecimenRow(
    pairing: AppTypePairing,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val densityTokens = LocalAppDensity.current
    val displayFont = FontFamily(Font(pairing.displayFontFamilyRes))
    val bodyFont = FontFamily(Font(pairing.bodyFontFamilyRes))

    val pairingName = when (pairing) {
        AppTypePairing.Editorial -> stringResource(R.string.type_pairing_editorial)
        AppTypePairing.Literary -> stringResource(R.string.type_pairing_literary)
        AppTypePairing.Modern -> stringResource(R.string.type_pairing_modern)
    }

    val pairingDesc = when (pairing) {
        AppTypePairing.Editorial -> stringResource(R.string.settings_typography_editorial_desc)
        AppTypePairing.Literary -> stringResource(R.string.settings_typography_literary_desc)
        AppTypePairing.Modern -> stringResource(R.string.settings_typography_modern_desc)
    }

    Surface(
        onClick = onClick,
        color = colors.surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = densityTokens.screenPadding, vertical = Padding.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Padding.xs),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Padding.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = pairingName,
                        style = AppTheme.typography.bodyLg,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.ink,
                    )
                    Text(
                        text = "· $pairingDesc",
                        style = AppTheme.typography.bodySm,
                        color = colors.inkMuted,
                    )
                }

                Text(
                    text = "Aesthetic Minimal Horizon",
                    fontFamily = displayFont,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.ink,
                )
                Text(
                    text = "Linear Gradient · 4K UHD",
                    fontFamily = bodyFont,
                    fontSize = 12.5.sp,
                    lineHeight = 16.sp,
                    color = colors.inkSoft,
                )
            }

            if (selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun TypographySpecimenRowPreview() {
    AppPreview {
        Column(modifier = Modifier.fillMaxWidth()) {
            TypographySpecimenRow(
                pairing = AppTypePairing.Editorial,
                selected = true,
                onClick = {},
            )
            TypographySpecimenRow(
                pairing = AppTypePairing.Literary,
                selected = false,
                onClick = {},
            )
        }
    }
}
