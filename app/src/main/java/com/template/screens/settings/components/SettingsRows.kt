package com.template.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

data class SettingsRow(
    val title: String,
    val value: String,
    val subtitle: String? = null,
    val actionLabel: String? = null,
    val accentValue: Boolean = false,
    val onClick: (() -> Unit)? = null,
)

@Composable
fun SettingsSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = Padding.lg,
) {
    val colors = LocalColorRoles.current
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = colors.inkMuted,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .padding(bottom = Padding.md),
    )
}

@Composable
fun SettingsRowItem(
    row: SettingsRow,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = Padding.lg,
) {
    val colors = LocalColorRoles.current
    val content: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .padding(horizontal = horizontalPadding)
                        .padding(vertical = Padding.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.md),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = row.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (row.subtitle != null) FontWeight.SemiBold else FontWeight.Normal,
                        color = colors.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (row.subtitle != null) {
                        Text(
                            text = row.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.inkMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text(
                    text = row.actionLabel ?: row.value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = if (row.accentValue) colors.accent else colors.inkMuted,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (showDivider) {
                HorizontalDivider(
                    color = colors.hairline,
                    thickness = Padding.hairline,
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                )
            }
        }
    }

    if (row.onClick != null) {
        Surface(
            onClick = row.onClick,
            color = colors.bg,
            modifier = modifier.fillMaxWidth(),
            content = content,
        )
    } else {
        Surface(
            color = colors.bg,
            modifier = modifier.fillMaxWidth(),
            content = content,
        )
    }
}

@Composable
private fun RowActionPill(label: String, modifier: Modifier = Modifier) {
    val colors = LocalColorRoles.current
    Surface(
        modifier = modifier,
        shape = AppShape.listFull,
        color = colors.bg,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = Padding.md,
                    vertical = Padding.sm,
                ),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = colors.ink,
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = colors.ink,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
