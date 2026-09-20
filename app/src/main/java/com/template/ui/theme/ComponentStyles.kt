package com.template.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.border
import androidx.compose.foundation.style.disabled
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Ambient theme token accessors for [StyleScope].
 *
 * Resolves theme tokens dynamically when the style is evaluated,
 * allowing styles to remain declarative and decoupled from Composable lifecycles.
 */
val StyleScope.colors: AppColors
    get() = LocalAppColors.currentValue

val StyleScope.typography: AppTypography
    get() = LocalAppTypography.currentValue

val StyleScope.shapes: AppShapes
    get() = LocalAppShapes.currentValue

val StyleScope.appDensity: AppDensityTokens
    get() = LocalAppDensity.currentValue

/**
 * Central design system component styles definition.
 *
 * Grouped hierarchically by component namespace (e.g. `AppTheme.styles.button.primary`).
 */
object ComponentStyles {
    val button = ButtonStyles
    val iconButton = IconButtonStyles
    val card = CardStyles
    val chip = ChipStyles
    val feedback = FeedbackStyles
    val navigation = NavigationStyles
}

object ButtonStyles {
    val primary: Style = Style {
        background(colors.ink)
        contentColor(colors.background)
        shape(RoundedCornerShape(AppShapes.PillRadius))
        minHeight(56.dp)
        disabled {
            background(colors.ink14)
            contentColor(colors.inkMuted)
        }
    }

    val secondary: Style = Style {
        background(colors.surface)
        contentColor(colors.ink)
        border(1.dp, colors.border)
        shape(RoundedCornerShape(AppShapes.PillRadius))
        minHeight(56.dp)
        disabled {
            background(colors.surface)
            contentColor(colors.inkMuted)
            border(1.dp, colors.hairline)
        }
    }

    val text: Style = Style {
        background(Color.Transparent)
        contentColor(colors.ink)
        shape(RoundedCornerShape(AppShapes.PillRadius))
        minHeight(44.dp)
        disabled {
            background(Color.Transparent)
            contentColor(colors.inkMuted)
        }
    }

    val textDanger: Style = Style {
        background(Color.Transparent)
        contentColor(colors.danger)
        shape(RoundedCornerShape(AppShapes.PillRadius))
        minHeight(44.dp)
        disabled {
            background(Color.Transparent)
            contentColor(colors.danger.copy(alpha = 0.4f))
        }
    }
}

object IconButtonStyles {
    val standard: Style = Style {
        background(Color.Transparent)
        contentColor(colors.ink)
        shape(CircleShape)
        disabled {
            background(Color.Transparent)
            contentColor(colors.inkMuted)
        }
    }

    val filled: Style = Style {
        background(colors.ink)
        contentColor(colors.background)
        shape(CircleShape)
        disabled {
            background(colors.ink14)
            contentColor(colors.inkMuted)
        }
    }

    val tonal: Style = Style {
        background(colors.ink14)
        contentColor(colors.ink)
        shape(CircleShape)
        disabled {
            background(colors.ink04)
            contentColor(colors.inkMuted)
        }
    }

    val outlined: Style = Style {
        background(colors.surface)
        contentColor(colors.ink)
        border(1.dp, colors.border)
        shape(CircleShape)
        disabled {
            background(colors.surface)
            contentColor(colors.inkMuted)
            border(1.dp, colors.hairline)
        }
    }

    val overlay: Style = Style {
        background(colors.ink.copy(alpha = 0.34f))
        contentColor(colors.surfaceFixed)
        shape(CircleShape)
        disabled {
            background(colors.ink.copy(alpha = 0.16f))
            contentColor(colors.surfaceFixed.copy(alpha = 0.4f))
        }
    }
}

object CardStyles {
    val elevated: Style = Style {
        background(colors.surface)
        contentColor(colors.ink)
        border(1.dp, colors.hairline)
        shape(shapes.card)
    }

    val filled: Style = Style {
        background(colors.ink04)
        contentColor(colors.ink)
        border(1.dp, colors.hairline)
        shape(shapes.card)
    }

    val outlined: Style = Style {
        background(colors.background)
        contentColor(colors.ink)
        border(1.dp, colors.border)
        shape(shapes.card)
    }
}

object ChipStyles {
    val filter: Style = Style {
        background(colors.surface)
        contentColor(colors.inkMuted)
        border(0.5.dp, colors.border)
        shape(RoundedCornerShape(AppShapes.ChipRadius))
        disabled {
            background(colors.surface)
            contentColor(colors.inkMuted.copy(alpha = 0.44f))
            border(0.5.dp, colors.hairline)
        }
    }

    val action: Style = Style {
        background(colors.ink04)
        contentColor(colors.ink)
        border(0.5.dp, colors.hairline)
        shape(RoundedCornerShape(AppShapes.ChipRadius))
        disabled {
            background(colors.ink04)
            contentColor(colors.inkMuted.copy(alpha = 0.44f))
            border(0.5.dp, colors.hairline)
        }
    }

    val selected: Style = Style {
        background(colors.accent12)
        contentColor(colors.accent)
        border(0.5.dp, colors.accent)
        shape(RoundedCornerShape(AppShapes.ChipRadius))
        disabled {
            background(colors.surface)
            contentColor(colors.inkMuted.copy(alpha = 0.44f))
            border(0.5.dp, colors.hairline)
        }
    }
}

object FeedbackStyles {
    val hintCard: Style = Style {
        background(colors.ink04)
        contentColor(colors.ink)
        border(1.dp, colors.hairline)
        shape(shapes.card)
    }

    val snackbar: Style = Style {
        background(colors.ink)
        contentColor(colors.background)
        shape(RoundedCornerShape(12.dp))
    }
}

object NavigationStyles {
    val floatingPill: Style = Style {
        background(colors.surface)
        contentColor(colors.ink)
        border(1.dp, colors.hairline)
        shape(RoundedCornerShape(AppShapes.PillRadius))
    }

    val listRow: Style = Style {
        background(Color.Transparent)
        contentColor(colors.ink)
        shape(shapes.card)
        disabled {
            background(Color.Transparent)
            contentColor(colors.inkMuted)
        }
    }
}
