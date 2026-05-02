# Android App Template

A modern Android application template leveraging the latest Jetpack Compose features and architectural patterns.

## Features

- **Jetpack Compose**: Modern declarative UI.
- **Theme Engine**: Material 3 expressive theming with dark mode, dynamic color (Material You), color presets, and runtime-tunable variable typography.
- **Circuit**: A simple, reactive framework for building Compose-driven applications.
- **Metro DI**: Fast and lightweight dependency injection.
- **Ktor**: Multiplatform asynchronous HTTP client.
- **Paparazzi**: Screen-testing at your fingertips.
- **Kotlin Serialization**: Type-safe JSON parsing.
- **DataStore**: Modern data storage solution.

## Getting Started

### Prerequisites

- Android Studio Koala or newer.
- JDK 21.

### Quick Start

1. Clone the repository.
2. Run `./init.sh` and follow the prompts. This renames the package, sets the app
   display name, and sets up release signing. On success the script removes itself.
3. Open in Android Studio.
4. Build and run:

```bash
./gradlew assembleDebug
```

Debug and release variants install side-by-side: the debug build's
`applicationId` carries a `.debug` suffix. Use different launcher icons in the
debug source set (see [App icons](#app-icons)) to tell them apart on-device.

## Release builds

`./init.sh` is the easiest path to a signed release — it offers to generate a
keystore and writes the credentials to `local.properties`. The rest of this
section covers what to do if you skipped that step, lost your keystore, or want
to reconfigure signing manually.

### Generate a new keystore

```bash
mkdir -p app/signing
keytool -genkeypair -v \
  -keystore app/signing/release.keystore \
  -alias release -keyalg RSA -keysize 2048 -validity 9125 \
  -dname "CN=Your Name"
```

`keytool` will prompt for a keystore password and a key password — use the same
value for both, since `app/build.gradle.kts` resolves them from a single
property. `app/signing/` is gitignored — never commit a real keystore.

### Use an existing keystore

Copy your `.keystore` / `.jks` file to `app/signing/release.keystore` (or set
`RELEASE_KEYSTORE_PATH` to point elsewhere — see below). The signing block
expects the alias `release`; if your existing keystore uses a different alias,
update `keyAlias` in `app/build.gradle.kts`.

### Configure credentials

`signingConfigs.create("release")` in `app/build.gradle.kts` resolves both
the keystore path and password in this order:

1. Environment variables (preferred for CI):
   - `RELEASE_KEYSTORE_PATH` — absolute or relative to the `app/` module.
   - `RELEASE_KEY_PASSWORD` — keystore + key password.
2. `local.properties` keys with the same names. Example:

   ```properties
   RELEASE_KEYSTORE_PATH=signing/release.keystore
   RELEASE_KEY_PASSWORD=your-password
   ```

3. The default path `signing/release.keystore` if neither source provides one.

`local.properties` is gitignored. Don't commit it.

### Building

```bash
./gradlew assembleRelease   # signed APK at app/build/outputs/apk/release/
./gradlew bundleRelease     # signed AAB at app/build/outputs/bundle/release/
```

Release builds have `isMinifyEnabled = true` and `isShrinkResources = true`. If
R8 strips something it shouldn't, add a keep rule to `app/proguard-rules.pro`.

## App icons

The launcher icon is set to `@mipmap/ic_launcher` in
`app/src/main/AndroidManifest.xml`. There are two icon sets:

| Source set | Path | Used by |
| --- | --- | --- |
| `main` | `app/src/main/res/mipmap-*/` | Release builds |
| `debug` | `app/src/debug/res/mipmap-*/` | Debug builds (overrides `main`) |

Each set needs the same set of files: `ic_launcher.png` (legacy),
`ic_launcher_foreground.png`, `ic_launcher_background.png`,
`ic_launcher_monochrome.png` across `mdpi` / `hdpi` / `xhdpi` / `xxhdpi` /
`xxxhdpi`, plus `mipmap-anydpi-v26/ic_launcher.xml` describing the adaptive
icon.

The fastest way to regenerate the full set is Android Studio's
**File → New → Image Asset** wizard:

1. Choose **Launcher Icons (Adaptive and Legacy)**.
2. Pick your foreground/background art, then click **Next** and select
   `main` as the target source set. Finish.
3. Run the wizard again with a visually distinct asset (e.g. tinted to make the
   debug variant unmistakable) and select `debug` as the target source set.

You can also use https://icon.kitchen to create simple icons for the app.

Combined with the debug build's `.debug` `applicationId` suffix, the two icons
let you keep both variants on a device without confusion.

## Theming

The theme engine lives in `app/src/main/java/com/template/ui/theme/` and is driven by user
preferences persisted in DataStore. The settings screen at `screens/settings/SettingsScreen.kt`
exposes every option to the user out of the box. Defaults are defined alongside the option
configuration in `ThemeConfig.kt`, so you can change them in one place.

### Architecture

| File | Responsibility |
| --- | --- |
| `Theme.kt` | `TemplateTheme` — root composable. Reads settings, builds `ColorScheme` + `Typography`, and applies `MaterialExpressiveTheme`. Exposes `darkThemeFromSettings()` and the `Padding` tokens. |
| `ThemeConfig.kt` | Enums and presets: `BaseSize`, `AppFontFamily`, `FontAxis`/`FontAxisConfig`, `ColorPreset`, plus default font-axis constants. |
| `ThemeEngine.kt` | Pure font/typography construction: ratio-based size scaling, variable-font axis wiring, and Material 3 `Typography` assembly. |
| `AppShapes.kt` | `AppShape` corner-radius tokens and `appMaterialShapes()` for Material 3 `Shapes`. Includes `listItemShape(index, total)` for grouped-list rendering. |
| `Color.kt` | `Color` alpha-extension utilities (`hairline`, `lightest`, `lighter`, `light`). |
| `DynamicTheme.kt` | `DynamicTheme(model)` — extracts a seed color from any image (Coil + Palette) and re-themes a subtree using Material Kolor. Cached via an `LruCache`. |

The keys backing these settings are declared in `data/settings/SettingKeys.kt` and read in
Composables via `rememberPreference` / `rememberEnumPreference` from `SettingsDataStore.kt`.

### What users can customize

All of the following are wired into the settings UI and live-update the running app:

- **Dark mode** — `System` / `Light` / `Dark` (`DarkModeKey`).
- **Colors** — Toggle Material You **Dynamic Color** (Android 12+) or pick a built-in preset
  (Teal, Cyan, Gold, Pink, Indigo, Forest). Off-dynamic schemes are generated by
  [Material Kolor](https://github.com/jordond/MaterialKolor) using `PaletteStyle.TonalSpot`.
- **Base type size** — `Small (14sp)` / `Medium (16sp)` / `Large (18sp)`. Drives every Material
  text style via a 1.2× modular scale.
- **Display font** — Used for `display*`, `headline*`, and `title*` styles.
- **Body font** — Used for `body*` and `label*` styles.

Bundled fonts: Google Sans Flex, Hanken Grotesk, Space Grotesk, Fredoka, Fascinate, Lobster,
Roboto Serif. Variable axes (`wdth`, `GRAD`, `ROND`) are auto-applied for fonts that declare
them via `AppFontFamily.supportedAxes`; tweak the per-axis defaults in `ThemeConfig.kt`.

### Customizing the theme engine

**Add a color preset**

```kotlin
// ui/theme/ThemeConfig.kt
ColorPreset(id = "ocean", nameRes = R.string.color_preset_ocean, color = Color(0xFF0077B6)),
```

Append to `ColorPreset.OPTIONS` and add the matching string in `res/values/strings.xml`.

**Add a font**

1. Drop the `.ttf` (or variable `.ttf`) into `app/src/main/res/font/`.
2. Add a new entry to `AppFontFamily` in `ThemeConfig.kt` with its `@FontRes`,
   `@StringRes` display name, and any `FontAxisConfig` axes the font supports.
3. Add the display name string in `strings.xml`. The font automatically appears in the
   Display Font and Body Font selectors.

**Change the default theme**

Defaults live in `ThemeConfig.kt`:

- `BaseSize.DEFAULT` (e.g. switch to `LARGE` for larger default text).
- `AppFontFamily.DEFAULT` (e.g. switch to `ROBOTO_SERIF`).
- `ColorPreset.DEFAULT` (the seed used when dynamic color is disabled and no preset is set).
- `DefaultDisplayFontWidth/Grade/Rond` and `DefaultBodyFontWidth/Grade/Rond` for variable-font
  axis defaults.

**Adjust the type scale**

`ThemeEngine.TYPOGRAPHY_RATIO` (default `1.200f`) controls the ratio between successive type
sizes. Lower it for a tighter scale, raise it for a more dramatic one.

**Tune corner radii / shapes**

Edit the `AppShape` tokens in `AppShapes.kt`. `appMaterialShapes()` maps them onto Material 3's
`Shapes` (`extraSmall` → chip, `small` → input, `medium` → card, `large` → dialog,
`extraLarge` → sheet).

**Apply image-driven theming to a subtree**

Wrap any composable in `DynamicTheme(model = imageUrlOrAny)` to retheme it from the dominant
color of an image (e.g. now-playing artwork, hero banners). Falls back to
`MaterialTheme.colorScheme.primary` while loading.

**Bypass user settings programmatically**

`TemplateTheme(typography = ..., colorScheme = ..., darkTheme = ...)` accepts overrides for
any axis — useful for previews, screenshot tests, or marketing surfaces that need a fixed look.

## Development

See [CLAUDE.md](CLAUDE.md) for detailed build commands, testing instructions, and coding conventions.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
