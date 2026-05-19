package com.template.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews

@Composable
fun AppScreenTopBar(
    title: String,
    @DrawableRes navigationIconRes: Int,
    navigationContentDescription: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    AppTopBar(
        title = title,
        modifier = modifier,
        alwaysSolid = true,
        leading = {
            TopBarIconButton(
                iconRes = navigationIconRes,
                contentDescription = navigationContentDescription,
                onClick = onNavigationClick,
            )
        },
        trailing = actions,
    )
}

@ThemePreviews
@Preview(widthDp = 412)
@Composable
private fun AppScreenTopBarPreview() {
    AppPreview {
        AppScreenTopBar(
            title = "From a link",
            navigationIconRes = R.drawable.ic_arrow_back,
            navigationContentDescription = "Back",
            onNavigationClick = {},
        )
    }
}
