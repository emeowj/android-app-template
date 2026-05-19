package com.template.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = AppShape.card,
    color: Color = LocalColorRoles.current.surface,
    contentColor: Color = LocalColorRoles.current.ink,
    border: BorderStroke? = BorderStroke(Padding.hairline, LocalColorRoles.current.hairline),
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            modifier = modifier,
            content = content,
        )
    } else {
        Surface(
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            modifier = modifier,
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun AppCardPreview() {
    AppPreview {
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.md),
        ) {
            Column(modifier = Modifier.padding(Padding.md)) {
                Text(
                    text = "Card title",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Supporting detail",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
