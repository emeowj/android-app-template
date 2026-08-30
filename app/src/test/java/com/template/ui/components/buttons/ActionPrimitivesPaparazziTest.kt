package com.template.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.R
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ActionPrimitivesPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppButtonVariantsLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ButtonVariantsSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppButtonVariantsDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ButtonVariantsSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppButtonStatesLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ButtonStatesSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppButtonStatesDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ButtonStatesSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppIconButtonLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                IconButtonSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppIconButtonDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                IconButtonSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppAffordanceLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                AffordanceSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppAffordanceDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                AffordanceSpecimen()
            }
        }
    }
}

@Composable
private fun ButtonVariantsSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppButton Variants (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppButton(
                text = "Primary Action",
                onClick = {},
                variant = AppButtonVariant.Primary,
                block = true,
            )

            AppButton(
                text = "Secondary Action",
                onClick = {},
                variant = AppButtonVariant.Secondary,
                block = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppButton(
                    text = "Text Button",
                    onClick = {},
                    variant = AppButtonVariant.Text,
                )
                AppButton(
                    text = "Destructive",
                    onClick = {},
                    variant = AppButtonVariant.TextDanger,
                )
            }
        }
    }
}

@Composable
private fun ButtonStatesSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppButton States (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppButton(
                text = "With Leading Icon",
                onClick = {},
                variant = AppButtonVariant.Primary,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_auto_awesome),
                        contentDescription = null,
                        modifier = Modifier.size(AppButtonDefaults.IconSize),
                    )
                },
                block = true,
            )

            AppButton(
                text = "With Trailing Chevron",
                onClick = {},
                variant = AppButtonVariant.Secondary,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(AppButtonDefaults.IconSize),
                    )
                },
                block = true,
            )

            AppButton(
                text = "Loading Button",
                onClick = {},
                variant = AppButtonVariant.Primary,
                isLoading = true,
                block = true,
            )

            AppButton(
                text = "Disabled Primary",
                onClick = {},
                variant = AppButtonVariant.Primary,
                enabled = false,
                block = true,
            )

            AppButton(
                text = "Disabled Secondary",
                onClick = {},
                variant = AppButtonVariant.Secondary,
                enabled = false,
                block = true,
            )
        }
    }
}

@Composable
private fun IconButtonSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppIconButton (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Text(
                text = "Standard on Canvas",
                style = typography.overline,
                color = colors.inkMuted,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIconButton(
                    iconRes = R.drawable.ic_share,
                    onClick = {},
                    contentDescription = "Share",
                    tone = AppIconButtonTone.Default,
                )
                AppIconButton(
                    iconRes = R.drawable.ic_close,
                    onClick = {},
                    contentDescription = "Close",
                    tone = AppIconButtonTone.Muted,
                )
                AppIconButton(
                    iconRes = R.drawable.ic_star,
                    onClick = {},
                    contentDescription = "Favorite",
                    tone = AppIconButtonTone.Accent,
                )
                AppIconButton(
                    iconRes = R.drawable.ic_share,
                    onClick = {},
                    contentDescription = "Disabled",
                    enabled = false,
                )
            }

            Text(
                text = "Overlay on Wallpaper Artwork Scrim",
                style = typography.overline,
                color = colors.inkMuted,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2C3E50),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppIconButton(
                        iconRes = R.drawable.ic_arrow_back,
                        onClick = {},
                        contentDescription = "Back",
                        overlay = true,
                    )
                    AppIconButton(
                        iconRes = R.drawable.ic_share,
                        onClick = {},
                        contentDescription = "Share",
                        overlay = true,
                    )
                    AppIconButton(
                        iconRes = R.drawable.ic_star,
                        onClick = {},
                        contentDescription = "Star",
                        overlay = true,
                    )
                    AppIconButton(
                        iconRes = R.drawable.ic_close,
                        onClick = {},
                        contentDescription = "Disabled Overlay",
                        overlay = true,
                        enabled = false,
                    )
                }
            }
        }
    }
}

@Composable
private fun AffordanceSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppAffordance (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Text(
                text = "Tinted Inline Affordances",
                style = typography.overline,
                color = colors.inkMuted,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // chip-add
                AppAffordance(
                    onClick = {},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = "Add",
                            modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                        )
                    },
                )

                // see-all
                AppAffordance(
                    label = "See all",
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null,
                            modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                        )
                    },
                )

                // reroll-all
                AppAffordance(
                    label = "Reroll all",
                    onClick = {},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_auto_awesome),
                            contentDescription = null,
                            modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                        )
                    },
                )
            }

            Text(
                text = "OnScrim Floating & Artwork Affordances",
                style = typography.overline,
                color = colors.inkMuted,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF34495E),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // more overflow on scrim
                    AppAffordance(
                        onClick = {},
                        style = AffordanceStyle.OnScrim,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = "Close",
                                modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                            )
                        },
                    )

                    // overlay-reroll with label
                    AppAffordance(
                        label = "Shuffle",
                        onClick = {},
                        style = AffordanceStyle.OnScrim,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_auto_awesome),
                                contentDescription = null,
                                modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                            )
                        },
                    )
                }
            }
        }
    }
}
