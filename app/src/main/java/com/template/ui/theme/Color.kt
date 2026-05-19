package com.template.ui.theme

import androidx.compose.ui.graphics.Color

inline val Color.hairline: Color
    get() = copy(alpha = 0.1f)

inline val Color.lightest: Color
    get() = copy(alpha = 0.3f)

inline val Color.lighter: Color
    get() = copy(alpha = 0.4f)

inline val Color.light: Color
    get() = copy(alpha = 0.8f)

fun Color.softTint(alpha: Float = 0.14f): Color = copy(alpha = alpha)
