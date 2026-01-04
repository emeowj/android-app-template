package com.template.data.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val DarkModeKey = enumPreferencesKey("darkMode", DarkMode.SYSTEM)
val HapticFeedbackEnabledKey = booleanPreferencesKey("hapticFeedbackEnabled")

enum class DarkMode {
    SYSTEM,
    LIGHT,
    DARK,
}

class EnumPreferencesKey<T : Enum<T>>(val key: Preferences.Key<String>, val defaultValue: T)

inline fun <reified T : Enum<T>> enumPreferencesKey(
    name: String,
    defaultValue: T,
): EnumPreferencesKey<T> =
    EnumPreferencesKey(key = stringPreferencesKey(name), defaultValue = defaultValue)
