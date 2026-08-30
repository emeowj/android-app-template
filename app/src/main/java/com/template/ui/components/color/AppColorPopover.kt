package com.template.ui.components.color

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.dialogShadow
import java.util.Locale
import kotlin.math.roundToInt

object AppColorPopoverDefaults {
    val PopoverWidth: Dp = 228.dp
    val PopoverRadius: Dp = AppShapes.CardRadius
    val PopoverPadding: Dp = 10.dp
    val SlSquareHeight: Dp = 116.dp
    val HueStripHeight: Dp = 14.dp
    val HexFieldHeight: Dp = 38.dp
    val SwatchSize: Dp = 20.dp
    val SwatchRadius: Dp = 4.dp
    val SwatchGap: Dp = 6.dp
    val InsetHairlineWidth: Dp = 1.dp
    val ContentGap: Dp = 8.dp
    const val SwatchHoverScale: Float = 1.12f

    val HueGradientColors: List<Color> = listOf(
        Color(0xFFFF0000),
        Color(0xFFFFFF00),
        Color(0xFF00FF00),
        Color(0xFF00FFFF),
        Color(0xFF0000FF),
        Color(0xFFFF00FF),
        Color(0xFFFF0000),
    )
}

/**
 * AppColorPopover is a floating popover card anchored to a tapped color field or gradient stop pin.
 * It contains, top to bottom:
 * 1. Hex input field with live preview swatch
 * 2. 2D Saturation / Lightness draggable square
 * 3. 0-360° Hue spectrum slider strip
 * 4. 24-color curated swatch palette grid
 *
 * @param color The currently selected [Color].
 * @param onColorChange Called in real-time as the user drags/taps any control.
 * @param onDismissRequest Called when the user clicks outside or presses Back/Escape.
 * @param modifier Modifier for the popover container.
 */
@Composable
fun AppColorPopover(
    color: Color,
    onColorChange: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val positionProvider = remember(density) {
        ColorPopoverPositionProvider(
            density = density,
            margin = 10.dp,
            verticalGap = 6.dp,
        )
    }

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        AppColorPopoverCard(
            color = color,
            onColorChange = onColorChange,
            modifier = modifier,
        )
    }
}

/**
 * Convenient overload of [AppColorPopover] accepting and emitting hex strings.
 */
@Composable
fun AppColorPopover(
    hex: String,
    onHexChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resolvedColor = remember(hex) {
        ColorUtils.parseColorHex(hex, fallback = Color.Black)
    }
    AppColorPopover(
        color = resolvedColor,
        onColorChange = { newColor ->
            onHexChange(newColor.toHex())
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    )
}

/**
 * The inner visual card of the color popover, including hex input,
 * Saturation/Lightness square, Hue strip, and 24-color swatch grid.
 */
@Composable
fun AppColorPopoverCard(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnColorChange by rememberUpdatedState(onColorChange)
    val colors = AppTheme.colors

    var hsl by remember { mutableStateOf(color.toHsl()) }

    LaunchedEffect(color) {
        val currentHslColor = color.toHsl()
        if (currentHslColor.toHex() != hsl.toHex()) {
            hsl = currentHslColor
        }
    }

    val updateColorFromHsl = remember {
        { newHsl: HslColor ->
            hsl = newHsl
            currentOnColorChange(newHsl.toColor())
        }
    }

    val shape = RoundedCornerShape(AppColorPopoverDefaults.PopoverRadius)

    Column(
        modifier = modifier
            .width(AppColorPopoverDefaults.PopoverWidth)
            .dialogShadow(shape = shape)
            .clip(shape)
            .background(colors.surface)
            .border(
                width = AppColorPopoverDefaults.InsetHairlineWidth,
                color = colors.hairline,
                shape = shape,
            )
            .padding(AppColorPopoverDefaults.PopoverPadding),
        verticalArrangement = Arrangement.spacedBy(AppColorPopoverDefaults.ContentGap),
    ) {
        ColorHexInputField(
            color = color,
            onColorChange = { newColor ->
                hsl = newColor.toHsl()
                currentOnColorChange(newColor)
            },
        )

        SaturationLightnessSquare(
            hue = hsl.normalizedHue,
            saturation = hsl.clampedSaturation,
            lightness = hsl.clampedLightness,
            onSaturationLightnessChange = { newSat, newLight ->
                updateColorFromHsl(hsl.copy(saturation = newSat, lightness = newLight))
            },
        )

        HueSpectrumStrip(
            hue = hsl.normalizedHue,
            onHueChange = { newHue ->
                updateColorFromHsl(hsl.copy(hue = newHue))
            },
        )

        ColorPaletteSwatches(
            selectedColor = color,
            onColorSelected = { selected ->
                hsl = selected.toHsl()
                currentOnColorChange(selected)
            },
        )
    }
}

@Composable
private fun ColorHexInputField(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    var textValue by remember(color) { mutableStateOf(color.toHex()) }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val fieldShape = RoundedCornerShape(AppShapes.InputRadius)

    BasicTextField(
        value = textValue,
        onValueChange = { input ->
            val upper = input.uppercase(Locale.ROOT)
            textValue = upper
            val clean = upper.removePrefix("#").trim()
            if (clean.length == 6 && clean.all { it in "0123456789ABCDEF" }) {
                val parsed = ColorUtils.parseColorHex("#$clean", fallback = color)
                onColorChange(parsed)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(AppColorPopoverDefaults.HexFieldHeight)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!focusState.isFocused) {
                    textValue = color.toHex()
                }
            },
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = colors.ink,
            letterSpacing = 0.04.sp,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        cursorBrush = SolidColor(colors.accent),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(fieldShape)
                    .background(colors.surface)
                    .border(
                        width = 1.dp,
                        color = if (isFocused) colors.accent else colors.border,
                        shape = fieldShape,
                    )
                    .padding(horizontal = Padding.small),
                horizontalArrangement = Arrangement.spacedBy(Padding.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                        .border(
                            width = AppColorPopoverDefaults.InsetHairlineWidth,
                            color = colors.hairline,
                            shape = RoundedCornerShape(4.dp),
                        ),
                )

                Box(modifier = Modifier.weight(1f)) {
                    innerTextField()
                }
            }
        },
    )
}

@Composable
private fun SaturationLightnessSquare(
    hue: Float,
    saturation: Float,
    lightness: Float,
    onSaturationLightnessChange: (saturation: Float, lightness: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnChange by rememberUpdatedState(onSaturationLightnessChange)
    val pureHueColor = remember(hue) {
        HslColor(hue = hue, saturation = 1f, lightness = 0.5f).toColor()
    }

    val squareShape = RoundedCornerShape(AppShapes.InputRadius)
    val colors = AppTheme.colors

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(AppColorPopoverDefaults.SlSquareHeight)
            .clip(squareShape)
            .border(
                width = AppColorPopoverDefaults.InsetHairlineWidth,
                color = colors.hairline,
                shape = squareShape,
            )
            .drawBehind {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7F7F7F), pureHueColor),
                    ),
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.Transparent,
                            Color.Black,
                        ),
                    ),
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val sat = (offset.x / size.width).coerceIn(0f, 1f)
                    val light = (1f - (offset.y / size.height)).coerceIn(0f, 1f)
                    currentOnChange(sat, light)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val sat = (change.position.x / size.width).coerceIn(0f, 1f)
                    val light = (1f - (change.position.y / size.height)).coerceIn(0f, 1f)
                    currentOnChange(sat, light)
                }
            },
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        val thumbXPx = (saturation.coerceIn(0f, 1f) * widthPx).roundToInt()
        val thumbYPx = ((1f - lightness.coerceIn(0f, 1f)) * heightPx).roundToInt()

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (thumbXPx - with(density) { 5.dp.toPx() }).roundToInt(),
                        y = (thumbYPx - with(density) { 5.dp.toPx() }).roundToInt(),
                    )
                }
                .size(10.dp)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    color = Color.White,
                    shape = CircleShape,
                )
                .drawBehind {
                    drawCircle(
                        color = Color(0x6613141A),
                        radius = (size.minDimension + 2.dp.toPx()) / 2f,
                        style = Stroke(width = 1.dp.toPx()),
                    )
                },
        )
    }
}

@Composable
private fun HueSpectrumStrip(
    hue: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnHueChange by rememberUpdatedState(onHueChange)
    val colors = AppTheme.colors
    val stripShape = RoundedCornerShape(AppShapes.PillRadius)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(AppColorPopoverDefaults.HueStripHeight)
            .clip(stripShape)
            .border(
                width = AppColorPopoverDefaults.InsetHairlineWidth,
                color = colors.hairline,
                shape = stripShape,
            )
            .drawBehind {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = AppColorPopoverDefaults.HueGradientColors,
                    ),
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val ratio = (offset.x / size.width).coerceIn(0f, 1f)
                    currentOnHueChange(ratio * 360f)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val ratio = (change.position.x / size.width).coerceIn(0f, 1f)
                    currentOnHueChange(ratio * 360f)
                }
            },
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val thumbXPx = ((hue.coerceIn(0f, 360f) / 360f) * widthPx).roundToInt()

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = (thumbXPx - with(density) { 6.dp.toPx() }).roundToInt(),
                        y = 0,
                    )
                }
                .size(12.dp)
                .clip(CircleShape)
                .background(colors.surface)
                .border(
                    width = 1.3.dp,
                    color = colors.ink,
                    shape = CircleShape,
                ),
        )
    }
}

@Composable
private fun ColorPaletteSwatches(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rows = remember {
        listOf(
            ColorPickerPalette.Row1,
            ColorPickerPalette.Row2,
            ColorPickerPalette.Row3,
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppColorPopoverDefaults.SwatchGap),
    ) {
        rows.forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowColors.forEach { swatchColor ->
                    val isSelected = remember(selectedColor, swatchColor) {
                        selectedColor.toHex() == swatchColor.toHex()
                    }
                    PopoverSwatch(
                        color = swatchColor,
                        selected = isSelected,
                        onClick = { onColorSelected(swatchColor) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PopoverSwatch(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isHovered || isPressed) AppColorPopoverDefaults.SwatchHoverScale else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "PopoverSwatchScale",
    )

    val colors = AppTheme.colors
    val swatchShape = RoundedCornerShape(AppColorPopoverDefaults.SwatchRadius)

    Box(
        modifier = modifier
            .size(AppColorPopoverDefaults.SwatchSize)
            .scale(scale)
            .semantics {
                role = Role.RadioButton
                this.selected = selected
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .drawWithContent {
                drawContent()

                val hairlinePx = AppColorPopoverDefaults.InsetHairlineWidth.toPx()
                val radiusPx = with(density) { AppColorPopoverDefaults.SwatchRadius.toPx() }

                drawRoundRect(
                    color = colors.hairline,
                    topLeft = Offset(hairlinePx / 2f, hairlinePx / 2f),
                    size = Size(size.width - hairlinePx, size.height - hairlinePx),
                    cornerRadius = CornerRadius(radiusPx, radiusPx),
                    style = Stroke(width = hairlinePx),
                )

                if (selected) {
                    val gapPx = with(density) { 1.5.dp.toPx() }
                    val accentRingPx = with(density) { 2.5.dp.toPx() }

                    drawRoundRect(
                        color = colors.surface,
                        topLeft = Offset(-gapPx / 2f, -gapPx / 2f),
                        size = Size(size.width + gapPx, size.height + gapPx),
                        cornerRadius = CornerRadius(radiusPx + gapPx / 2f, radiusPx + gapPx / 2f),
                        style = Stroke(width = gapPx),
                    )

                    val totalOffset = gapPx + (accentRingPx / 2f)
                    drawRoundRect(
                        color = colors.accent,
                        topLeft = Offset(-totalOffset, -totalOffset),
                        size = Size(size.width + (totalOffset * 2f), size.height + (totalOffset * 2f)),
                        cornerRadius = CornerRadius(radiusPx + totalOffset, radiusPx + totalOffset),
                        style = Stroke(width = accentRingPx),
                    )
                }
            }
            .clip(swatchShape)
            .background(color),
    )
}

@ThemePreviews
@Composable
private fun AppColorPopoverCardPreview() {
    var currentColor by remember { mutableStateOf(Color(0xFF5FA8D3)) }

    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(Padding.large),
            contentAlignment = Alignment.Center,
        ) {
            AppColorPopoverCard(
                color = currentColor,
                onColorChange = { currentColor = it },
            )
        }
    }
}
