package com.template.ui.theme

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import com.materialkolor.score.Score

private const val SeedColorImageSizePx = 96

@Composable
fun DynamicTheme(
    model: Any?,
    fallback: Color = MaterialTheme.colorScheme.primary,
    isDark: Boolean = darkThemeFromSettings(),
    style: PaletteStyle = PaletteStyle.TonalSpot,
    content: @Composable () -> Unit,
) {
    val seed = rememberSeedColorFromImage(model = model, fallback = fallback)
    val scheme = rememberDynamicColorScheme(seedColor = seed, isDark = isDark, style = style)
    MuralTheme(colorScheme = scheme, darkTheme = isDark, content = content)
}

@Composable
fun rememberSeedColorFromImage(model: Any?, fallback: Color): Color {
    val context = LocalContext.current
    val cachedArgb = model?.let { SeedColorCache[it] }
    val initial = cachedArgb?.let(::Color) ?: fallback
    val color by produceState(initialValue = initial, model) {
        if (model == null) {
            value = fallback
            return@produceState
        }
        SeedColorCache[model]?.let { argb ->
            value = Color(argb)
            return@produceState
        }
        val extracted = extractSeedColor(context, model)
        if (extracted != null) {
            SeedColorCache.put(model, extracted.toArgb())
            value = extracted
        } else {
            value = fallback
        }
    }
    return color
}

private suspend fun extractSeedColor(context: Context, model: Any): Color? {
    val request = ImageRequest.Builder(context)
        .data(model)
        .size(SeedColorImageSizePx)
        .allowHardware(false)
        .build()
    val bitmap = when (val result = context.imageLoader.execute(request)) {
        is SuccessResult -> result.image.toBitmap()
        else -> null
    } ?: return null
    return bitmap.extractSeedColor()
}

private fun Bitmap.extractSeedColor(): Color? {
    val swatches = Palette.from(this)
        .maximumColorCount(16)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    if (swatches.isEmpty()) return null
    return Score.score(swatches).firstOrNull()?.let(::Color)
}

private object SeedColorCache {
    private val cache = LruCache<Any, Int>(64)
    operator fun get(model: Any): Int? = synchronized(cache) { cache.get(model) }
    fun put(model: Any, argb: Int) {
        synchronized(cache) { cache.put(model, argb) }
    }
}
