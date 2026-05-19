package com.template.ui.components.sheets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun AppSheetSectionLabel(text: String, modifier: Modifier = Modifier) {
    val colors = LocalColorRoles.current
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        letterSpacing = 2.4.sp,
        fontWeight = FontWeight.SemiBold,
        color = colors.inkMuted,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.md, vertical = Padding.sm),
    )
}

@Composable
fun AppSheetActionRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    subtitle: String? = null,
    description: String? = null,
    trailing: String? = null,
    selected: Boolean = false,
) {
    val colors = LocalColorRoles.current
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        contentColor = colors.ink,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.md, vertical = Padding.md),
            horizontalArrangement = Arrangement.spacedBy(Padding.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = colors.inkMuted,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Padding.xxs),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = colors.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!description.isNullOrBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkSoft,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (!trailing.isNullOrBlank()) {
                Spacer(modifier = Modifier.size(Padding.xs))
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.inkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppSheetRowsPreview() {
    AppPreview {
        AppSheetSurface {
            AppSheetSectionLabel(text = "Sort by")
            AppSheetActionRow(label = "Recently added", selected = true, onClick = {})
            AppSheetActionRow(
                label = "Enrich details",
                iconRes = R.drawable.ic_auto_awesome,
                trailing = "Default",
                onClick = {},
            )
            AppSheetActionRow(
                label = "OpenAI",
                subtitle = "GPT-5.4 mini",
                description = "Use this option for generated suggestions and metadata help.",
                selected = false,
                onClick = {},
            )
        }
    }
}
