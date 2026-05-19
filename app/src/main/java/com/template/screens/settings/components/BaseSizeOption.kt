package com.template.screens.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.BaseSize
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun BaseSizeOption(
    size: BaseSize,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.card,
        color = if (selected) colors.surfaceAlt else colors.bg,
        border =
            BorderStroke(
                width = if (selected) 2.dp else Padding.hairline,
                color = if (selected) colors.accent else colors.hairline,
            ),
    ) {
        Row(
            modifier = Modifier.padding(Padding.md),
            horizontalArrangement = Arrangement.spacedBy(Padding.md),
        ) {
            BaseSizeGlyph(size = size, modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Padding.xs),
            ) {
                Text(
                    text = stringResource(size.displayNameRes),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (selected) colors.ink else colors.inkSoft,
                )
                Text(
                    text = stringResource(R.string.settings_base_size_sp_value, size.bodySizeSp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkMuted,
                )
            }
        }
    }
}

@Composable
private fun BaseSizeGlyph(size: BaseSize, modifier: Modifier = Modifier) {
    val colors = LocalColorRoles.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Padding.xs),
    ) {
        Text(
            text = stringResource(R.string.settings_base_size_sample),
            fontSize = size.bodySizeSp.sp,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.ink,
        )
        repeat(2) { index ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(if (index == 0) 0.86f else 0.62f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(if (index == 0) colors.inkSoft else colors.inkMuted),
            )
        }
    }
}

@Composable
@ThemePreviews
private fun BaseSizeOptionPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            BaseSize.entries.forEach { size ->
                BaseSizeOption(
                    size = size,
                    selected = size == BaseSize.DEFAULT,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
