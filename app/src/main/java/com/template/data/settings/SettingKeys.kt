package com.template.data.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.template.ui.theme.AppFontFamily
import com.template.ui.theme.BaseSize

val DarkModeKey = enumPreferencesKey("darkMode", DarkMode.SYSTEM)
val HapticFeedbackEnabledKey = booleanPreferencesKey("hapticFeedbackEnabled")

val ColorPresetIdKey = stringPreferencesKey("colorPresetId")

val BaseSizeKey = enumPreferencesKey("baseSize", BaseSize.DEFAULT)
val DisplayFontFamilyKey = enumPreferencesKey("displayFontFamily", AppFontFamily.DEFAULT_DISPLAY)
val BodyFontFamilyKey = enumPreferencesKey("bodyFontFamily", AppFontFamily.DEFAULT_BODY)
val DisplayFontWidthKey = intPreferencesKey("displayFontWidth")
val DisplayFontGradeKey = intPreferencesKey("displayFontGrade")
val DisplayFontRondKey = intPreferencesKey("displayFontRond")
val BodyFontWidthKey = intPreferencesKey("bodyFontWidth")
val BodyFontGradeKey = intPreferencesKey("bodyFontGrade")
val BodyFontRondKey = intPreferencesKey("bodyFontRond")

enum class DarkMode {
    SYSTEM,
    LIGHT,
    DARK,
}

class EnumPreferencesKey<T : Enum<T>>(val key: Preferences.Key<String>, val defaultValue: T)

inline fun <reified T : Enum<T>> enumPreferencesKey(
    name: String,
    defaultValue: T,
): EnumPreferencesKey<T> = EnumPreferencesKey(key = stringPreferencesKey(name), defaultValue = defaultValue)
