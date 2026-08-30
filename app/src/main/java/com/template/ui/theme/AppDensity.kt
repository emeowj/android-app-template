package com.template.ui.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R

enum class AppDensity(
    val screenPadding: Dp,
    val cardPadding: Dp,
    @StringRes val nameRes: Int,
) {
    Compact(
        screenPadding = 18.dp,
        cardPadding = 12.dp,
        nameRes = R.string.density_compact,
    ),
    Comfortable(
        screenPadding = 24.dp,
        cardPadding = 16.dp,
        nameRes = R.string.density_comfortable,
    ),
    Spacious(
        screenPadding = 30.dp,
        cardPadding = 20.dp,
        nameRes = R.string.density_spacious,
    ),
}

@Immutable
data class AppDensityTokens(
    val density: AppDensity = AppDensity.Comfortable,
) {
    val screenPadding: Dp get() = density.screenPadding
    val cardPadding: Dp get() = density.cardPadding
}

val LocalAppDensity = staticCompositionLocalOf { AppDensityTokens() }
