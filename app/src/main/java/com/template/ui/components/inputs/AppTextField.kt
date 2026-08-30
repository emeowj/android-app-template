package com.template.ui.components.inputs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing

object AppTextFieldDefaults {
    val MinHeight: Dp = 48.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.InputRadius)

    @Composable
    fun colors(): TextFieldColors {
        val colors = AppTheme.colors
        return OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.ink,
            unfocusedTextColor = colors.ink,
            disabledTextColor = colors.inkMuted.copy(alpha = 0.44f),
            errorTextColor = colors.danger,
            focusedContainerColor = colors.surface,
            unfocusedContainerColor = colors.surface,
            disabledContainerColor = colors.surface,
            errorContainerColor = colors.surface,
            cursorColor = colors.accent,
            errorCursorColor = colors.danger,
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.border,
            disabledBorderColor = colors.hairline,
            errorBorderColor = colors.danger,
            focusedLabelColor = colors.accent,
            unfocusedLabelColor = colors.inkMuted,
            disabledLabelColor = colors.inkMuted.copy(alpha = 0.44f),
            errorLabelColor = colors.danger,
            focusedPlaceholderColor = colors.inkMuted,
            unfocusedPlaceholderColor = colors.inkMuted,
            disabledPlaceholderColor = colors.inkMuted.copy(alpha = 0.44f),
            errorPlaceholderColor = colors.inkMuted,
            focusedLeadingIconColor = colors.ink,
            unfocusedLeadingIconColor = colors.inkMuted,
            disabledLeadingIconColor = colors.inkMuted.copy(alpha = 0.44f),
            errorLeadingIconColor = colors.danger,
            focusedTrailingIconColor = colors.ink,
            unfocusedTrailingIconColor = colors.inkMuted,
            disabledTrailingIconColor = colors.inkMuted.copy(alpha = 0.44f),
            errorTrailingIconColor = colors.danger,
            focusedSupportingTextColor = colors.inkMuted,
            unfocusedSupportingTextColor = colors.inkMuted,
            disabledSupportingTextColor = colors.inkMuted.copy(alpha = 0.44f),
            errorSupportingTextColor = colors.danger,
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
    textStyle: TextStyle = AppTheme.typography.bodyLg,
    shape: Shape = AppTextFieldDefaults.Shape,
    colors: TextFieldColors = AppTextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .defaultMinSize(minHeight = AppTextFieldDefaults.MinHeight)
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
        enabled = enabled,
        readOnly = readOnly,
        label = label?.let { { Text(it, style = AppTheme.typography.bodySm) } },
        placeholder = placeholder?.let { { Text(it, style = AppTheme.typography.bodyLg) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText?.let { { Text(it, style = AppTheme.typography.caption) } },
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
        interactionSource = interactionSource,
    )
}

@Composable
fun AppTextField(
    value: androidx.compose.ui.text.input.TextFieldValue,
    onValueChange: (androidx.compose.ui.text.input.TextFieldValue) -> Unit,
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
    textStyle: TextStyle = AppTheme.typography.bodyLg,
    shape: Shape = AppTextFieldDefaults.Shape,
    colors: TextFieldColors = AppTextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .defaultMinSize(minHeight = AppTextFieldDefaults.MinHeight)
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
        enabled = enabled,
        readOnly = readOnly,
        label = label?.let { { Text(it, style = AppTheme.typography.bodySm) } },
        placeholder = placeholder?.let { { Text(it, style = AppTheme.typography.bodyLg) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText?.let { { Text(it, style = AppTheme.typography.caption) } },
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
        interactionSource = interactionSource,
    )
}

@ThemePreviews
@Composable
private fun AppTextFieldPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppTextField(
                value = "Project name",
                onValueChange = {},
                label = "Name",
                modifier = Modifier.fillMaxWidth(),
            )
            AppTextField(
                value = "",
                onValueChange = {},
                label = "Description",
                placeholder = "Enter short summary",
                modifier = Modifier.fillMaxWidth(),
            )
            AppTextField(
                value = "Invalid input",
                onValueChange = {},
                label = "Seed",
                isError = true,
                supportingText = "Seed must be numeric",
                modifier = Modifier.fillMaxWidth(),
            )
            AppTextField(
                value = "Disabled text",
                onValueChange = {},
                label = "Status",
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
