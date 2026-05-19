package com.template.ui.components.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonDefaults
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Composable
fun AppSheetActionBar(
    secondaryLabel: String,
    onSecondary: () -> Unit,
    primaryLabel: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
) {
    AppSheetActionBarContainer(modifier = modifier) {
        AppButton(
            label = secondaryLabel,
            onClick = onSecondary,
            variant = AppButtonVariant.Secondary,
            shape = AppShape.input,
            height = AppButtonDefaults.Height,
            modifier = Modifier.weight(0.9f),
        )
        AppButton(
            label = primaryLabel,
            onClick = onPrimary,
            enabled = primaryEnabled,
            variant = AppButtonVariant.Primary,
            shape = AppShape.input,
            height = AppButtonDefaults.Height,
            modifier = Modifier.weight(1.8f),
        )
    }
}

@Composable
fun AppSheetActionBar(
    primaryLabel: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
) {
    AppSheetActionBarContainer(modifier = modifier) {
        AppButton(
            label = primaryLabel,
            onClick = onPrimary,
            enabled = primaryEnabled,
            variant = AppButtonVariant.Primary,
            shape = AppShape.input,
            height = AppButtonDefaults.Height,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AppSheetActionBarContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalColorRoles.current
    Surface(
        color = colors.surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = Padding.lg,
                        vertical = Padding.sm,
                    ),
            horizontalArrangement = Arrangement.spacedBy(Padding.md),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun AppSheetActionBarPreview() {
    AppPreview {
        AppSheetActionBar(
            secondaryLabel = "Cancel",
            onSecondary = {},
            primaryLabel = "Done · 2 lists",
            onPrimary = {},
        )
    }
}

@ThemePreviews
@Composable
private fun AppSheetActionBarSinglePreview() {
    AppPreview {
        AppSheetActionBar(
            primaryLabel = "Done",
            onPrimary = {},
        )
    }
}
