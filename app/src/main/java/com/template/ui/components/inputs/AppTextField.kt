package com.template.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

object AppTextFieldDefaults {
    val Shape: Shape = AppShape.input

    @Composable
    fun colors(): TextFieldColors {
        val roles = LocalColorRoles.current
        return OutlinedTextFieldDefaults.colors(
            focusedTextColor = roles.ink,
            unfocusedTextColor = roles.ink,
            disabledTextColor = roles.inkMuted,
            errorTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = roles.surface,
            unfocusedContainerColor = roles.surface,
            disabledContainerColor = roles.surfaceAlt,
            errorContainerColor = roles.surface,
            cursorColor = roles.ink,
            errorCursorColor = MaterialTheme.colorScheme.error,
            focusedBorderColor = roles.ink,
            unfocusedBorderColor = roles.hairline,
            disabledBorderColor = roles.hairline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = roles.inkSoft,
            unfocusedLabelColor = roles.inkMuted,
            disabledLabelColor = roles.inkMuted,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedPlaceholderColor = roles.inkMuted,
            unfocusedPlaceholderColor = roles.inkMuted,
        )
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    supportingText: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = AppTextFieldDefaults.Shape,
    colors: TextFieldColors = AppTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        label = label?.let { { Text(it) } },
        placeholder = placeholder?.let { { Text(it) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText?.let { { Text(it) } },
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        colors = colors,
    )
}

@ThemePreviews
@Composable
private fun AppTextFieldPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
        ) {
            AppTextField(
                value = "Project name",
                onValueChange = {},
                label = "Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Padding.sm),
            )
            AppTextField(
                value = "",
                onValueChange = {},
                label = "Description",
                placeholder = "Short summary",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
