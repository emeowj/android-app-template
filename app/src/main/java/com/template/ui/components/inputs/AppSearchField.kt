package com.template.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.template.R
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.components.buttons.AppIconButtonTone
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.Padding

@Composable
fun AppSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    onClear: (() -> Unit)? = null,
    clearContentDescription: String = stringResource(R.string.search_clear_cd),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
            )
        },
        trailingIcon =
            if (onClear != null && value.isNotEmpty()) {
                {
                    AppIconButton(
                        iconRes = R.drawable.ic_close,
                        onClick = onClear,
                        contentDescription = clearContentDescription,
                        tone = AppIconButtonTone.Muted,
                    )
                }
            } else {
                null
            },
    )
}

@ThemePreviews
@Composable
private fun AppSearchFieldPreview() {
    AppPreview {
        AppSearchField(
            value = "woolf",
            onValueChange = {},
            onClear = {},
            label = "Search",
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.md),
        )
    }
}
