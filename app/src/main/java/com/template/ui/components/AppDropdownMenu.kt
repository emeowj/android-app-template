package com.template.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = 240.dp,
    offset: DpOffset = DpOffset(x = 0.dp, y = Padding.sm),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalColorRoles.current
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = minWidth),
        offset = offset,
        shape = AppShape.input,
        containerColor = colors.surfaceAlt,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(Padding.hairline, colors.hairline),
        properties = properties,
    ) {
        Column(
            modifier = Modifier.padding(Padding.sm),
            verticalArrangement = Arrangement.spacedBy(Padding.xxs),
            content = content,
        )
    }
}

@Composable
fun AppDropdownMenuSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = colors.inkSoft,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.md, vertical = Padding.sm),
    )
}

@Composable
fun AppDropdownMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalColorRoles.current
    val itemShape = RoundedCornerShape(AppShape.smallRadius)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clip(itemShape)
                .background(if (selected) colors.surface else Color.Transparent)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                .semantics { this.selected = selected }
                .padding(horizontal = Padding.md, vertical = Padding.xs),
        horizontalArrangement = Arrangement.spacedBy(Padding.md),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun AppDropdownMenuCheckIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    Box(modifier = modifier.size(20.dp), contentAlignment = Alignment.Center) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = colors.ink,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppDropdownMenuPreview() {
    AppPreview {
        val colors = LocalColorRoles.current
        Box(modifier = Modifier.padding(Padding.md)) {
            Surface(
                modifier =
                    Modifier
                        .width(280.dp),
                shape = AppShape.large,
                color = colors.surface,
                border = BorderStroke(Padding.hairline, colors.hairline),
            ) {
                Column(
                    modifier = Modifier.padding(Padding.sm),
                    verticalArrangement = Arrangement.spacedBy(Padding.xxs),
                ) {
                    AppDropdownMenuSectionLabel(text = "Current")
                    listOf("2026", "2025", "2024", "All time").forEachIndexed { index, label ->
                        val selected = index == 0
                        AppDropdownMenuItem(selected = selected, onClick = {}) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.ink,
                                fontWeight =
                                    if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                modifier = Modifier.weight(1f),
                            )
                            AppDropdownMenuCheckIcon(selected = selected)
                        }
                    }
                }
            }
        }
    }
}
