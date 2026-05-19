package com.template.ui.components.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun AppSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    kicker: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = LocalColorRoles.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = TopAppBarDefaults.TopAppBarExpandedHeight)
            .padding(horizontal = Padding.lg, vertical = Padding.sm),
        verticalArrangement = Arrangement.spacedBy(
            Padding.sm,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Padding.sm),
            ) {
                if (!kicker.isNullOrBlank()) {
                    Text(
                        text = kicker.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.inkMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            actions()
        }
    }
}

@ThemePreviews
@Composable
private fun AppSheetHeaderPreview() {
    AppPreview {
        Column {
            AppSheetHeader(
                kicker = "Add \"The Magic Mountain\"",
                title = "Choose lists",
            )
            HorizontalDivider()
            AppSheetHeader(title = "Selection Title")
        }
    }
}
