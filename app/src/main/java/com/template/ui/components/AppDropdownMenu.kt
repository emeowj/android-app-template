package com.template.ui.components

import androidx.annotation.DrawableRes
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
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding

@Composable
fun AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = 220.dp,
    offset: DpOffset = DpOffset(x = 0.dp, y = Padding.sm),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppTheme.colors
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = minWidth),
        offset = offset,
        shape = RoundedCornerShape(AppShapes.CardRadius),
        containerColor = colors.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, colors.hairline),
        properties = properties,
    ) {
        Column(
            modifier = Modifier.padding(Padding.xs),
            verticalArrangement = Arrangement.spacedBy(Padding.hairline),
            content = content,
        )
    }
}

@Composable
fun AppDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    selected: Boolean = false,
    danger: Boolean = false,
    enabled: Boolean = true,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val itemShape = RoundedCornerShape(AppShapes.InputRadius)

    val itemColor = when {
        danger -> colors.danger
        selected -> colors.accent
        else -> colors.ink
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clip(itemShape)
            .background(if (selected) colors.accent12 else Color.Transparent)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(horizontal = Padding.md, vertical = Padding.xs),
        horizontalArrangement = Arrangement.spacedBy(Padding.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = itemColor,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(
            text = text,
            style = typography.bodyMd,
            color = itemColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )
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

@Composable
fun AppDropdownMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AppTheme.colors
    val itemShape = RoundedCornerShape(AppShapes.InputRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clip(itemShape)
            .background(if (selected) colors.accent12 else Color.Transparent)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(horizontal = Padding.md, vertical = Padding.xs),
        horizontalArrangement = Arrangement.spacedBy(Padding.md),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@ThemePreviews
@Composable
private fun AppDropdownMenuPreview() {
    AppPreview {
        val colors = AppTheme.colors
        Box(modifier = Modifier.padding(Padding.md)) {
            Surface(
                modifier = Modifier.width(260.dp),
                shape = RoundedCornerShape(AppShapes.CardRadius),
                color = colors.surface,
                border = BorderStroke(1.dp, colors.hairline),
            ) {
                Column(
                    modifier = Modifier.padding(Padding.xs),
                    verticalArrangement = Arrangement.spacedBy(Padding.hairline),
                ) {
                    AppDropdownMenuItem(text = "Edit collection", iconRes = R.drawable.ic_tune, onClick = {})
                    AppDropdownMenuItem(text = "Active view", selected = true, onClick = {})
                    AppDropdownMenuItem(text = "Delete item", iconRes = R.drawable.ic_close, danger = true, onClick = {})
                }
            }
        }
    }
}
