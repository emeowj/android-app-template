package com.template.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding
import com.template.ui.theme.softTint

enum class AppChipStyle {
    /** Circular filter chip with a selected/unselected state. */
    Filter,

    /** Compact soft-tint pill for metadata or status labels (read-only by default). */
    Pill,

    /** Filter-shaped chip with a leading colored dot, used for status indicators. */
    Status,
}

@Composable
fun AppChip(
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    style: AppChipStyle = AppChipStyle.Filter,
    selected: Boolean = false,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingText: String? = null,
    labelFontFamily: FontFamily? = null,
    tint: Color? = null,
) {
    val roles = LocalColorRoles.current
    val shape: Shape = when (style) {
        AppChipStyle.Filter, AppChipStyle.Status -> CircleShape
        AppChipStyle.Pill -> AppShape.pill
    }
    val container: Color
    val content: Color
    val border: BorderStroke?
    when (style) {
        AppChipStyle.Filter, AppChipStyle.Status -> {
            container = when {
                selected -> tint ?: roles.ink
                else -> Color.Transparent
            }
            content = when {
                selected -> roles.bg
                else -> roles.inkSoft
            }
            border = if (selected) null else BorderStroke(Padding.hairline, roles.hairline)
        }

        AppChipStyle.Pill -> {
            val accent = tint ?: roles.accent
            container = accent.softTint()
            content = accent
            border = null
        }
    }
    val horizontalPadding = if (style == AppChipStyle.Pill) Padding.sm else Padding.lg
    val verticalPadding = if (style == AppChipStyle.Pill) Padding.xs else Padding.sm
    val labelStyle =
        if (style == AppChipStyle.Pill) {
            MaterialTheme.typography.labelSmall
        } else {
            MaterialTheme.typography.bodyMedium
        }
    val labelWeight = if (style == AppChipStyle.Pill) FontWeight.Medium else FontWeight.SemiBold
    val rowContent: @Composable () -> Unit = {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke(this)
            Text(
                text = label,
                style = labelStyle,
                fontFamily = labelFontFamily,
                fontWeight = labelWeight,
                letterSpacing = 0.sp,
                color = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            trailingIcon?.invoke(this)
            trailingText?.let {
                Text(
                    text = it,
                    style = labelStyle,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp,
                    color = if (selected) content.copy(alpha = 0.78f) else roles.inkMuted,
                    maxLines = 1,
                )
            }
        }
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            shape = shape,
            color = container,
            contentColor = content,
            border = border,
            modifier = modifier,
            content = rowContent,
        )
    } else {
        Surface(
            shape = shape,
            color = container,
            contentColor = content,
            border = border,
            modifier = modifier,
            content = rowContent,
        )
    }
}

@ThemePreviews
@Composable
private fun AppChipPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Padding.sm)) {
                AppChip(
                    label = "All",
                    onClick = {},
                    selected = true,
                    trailingText = "42",
                )
                AppChip(
                    label = "Active",
                    onClick = {},
                    selected = false,
                    trailingText = "3",
                )
                AppChip(
                    label = "Audio",
                    onClick = {},
                    selected = false,
                    trailingText = "1",
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Padding.sm)) {
                AppChip(label = "Paper", style = AppChipStyle.Pill)
                AppChip(label = "304 pages", style = AppChipStyle.Pill)
                AppChip(label = "Highlight", style = AppChipStyle.Pill)
            }
        }
    }
}
