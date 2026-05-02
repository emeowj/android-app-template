# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project shape

Android app *template* — a starting point that gets renamed/rebranded per project. Before any real work, the template is meant to be initialized via `./init.sh` (interactive: package rename, app display name, optional release keystore generation). The script deletes itself on success, so if it's still present the project hasn't been initialized yet. Both the template package (`com.template`) and the README's release-signing flow assume this.

## Build, test, format

```bash
./gradlew :app:assembleDebug          # debug APK
./gradlew :app:assembleRelease        # signed release APK (needs keystore — see README)
./gradlew :app:bundleRelease          # signed AAB

./gradlew test                        # all unit tests
./gradlew :app:testDebugUnitTest      # app unit tests only
./gradlew :app:testDebugUnitTest --tests "com.template.screens.search.SearchPresenterTest"  # single test class

./gradlew lint
```

### Paparazzi (screenshot tests)

Paparazzi runs as part of `testDebugUnitTest`. Snapshots live in `app/src/test/snapshots/`.

```bash
./gradlew recordPaparazziDebug   # regenerate baselines after intentional UI changes
./gradlew verifyPaparazziDebug   # compare against baselines
```

When you change a Composable that has a Paparazzi test (currently `HomeScreen`, `SearchUi`), expect `verifyPaparazziDebug` to fail until you run `recordPaparazziDebug` and commit the new PNGs.

## Architecture

### Circuit + Metro: how a screen is wired

The app uses [Slack's Circuit](https://slackhq.github.io/circuit/) for navigation and unidirectional state, with [Metro](https://github.com/ZacSweers/metro) (Compiler plugin based DI) supplying presenters/UIs. Circuit's codegen is configured to emit Metro contributions (`circuit.codegen.mode=metro` in `app/build.gradle.kts`).

A screen is a triple of files (see `screens/search/` for the canonical example):

1. **`*Screen.kt`** — a `@Parcelize` `Screen` (route key) plus its `State` (`CircuitUiState`) and `Event` (`CircuitUiEvent`) types.
2. **`*Presenter.kt`** — a `Presenter<State>` annotated with `@CircuitInject(screen = ..., scope = AppScope::class)`. Use `@AssistedInject` + an `@AssistedFactory` when the presenter needs the `Screen` instance (e.g. for initial query state); use a plain `class` when it doesn't (see `HomePresenter`).
3. **`*Ui.kt`** — a `@CircuitInject`-annotated `@Composable fun ...Ui(state, modifier)`.

`@CircuitInject` causes KSP to generate `Presenter.Factory` / `Ui.Factory` Metro contributions into `AppScope`. `AppGraph` collects them as `Set<Presenter.Factory>` / `Set<Ui.Factory>` and builds the `Circuit` instance — so **adding a new screen requires no manual DI wiring**, just the three files plus a tab/navigation entry if user-reachable.

### App entry & navigation

`TemplateApplication.onCreate` builds `AppGraph` via `createGraphFactory<AppGraph.Factory>().create(this)`. `MainActivity` reads `appGraph.circuit` and hands it to `AppScaffold`, which:

- Owns the bottom-tab `NavigationTab` enum (`Home`, `Search`) — adding a tab means adding an enum entry pointing at a `Screen`.
- Hosts the saveable backstack and `NavigableCircuitContent`.
- Wraps the navigator with `rememberInterceptingNavigator` to fire `HapticNavigationEventListener` on transitions.
- Auto-hides the bottom bar when the top of the backstack isn't a tab screen (e.g. settings).
- Exposes `LocalBottomBarPadding` so inner screens can pad list bottoms correctly.

### Coroutine scope inside presenters

`rememberRetainedCoroutineScope()` (in `ui/RetainedCoroutineScope.kt`) survives configuration changes alongside Circuit's `rememberRetained`. Use it (not `rememberCoroutineScope`) for presenter work that should outlive rotation, like the debounced search in `SearchPresenter`.

### Presenter unit tests

Build the system under test through Metro, not by `new`-ing the presenter with hand-rolled fakes. The pattern (canonical example: `app/src/test/java/com/template/screens/search/SearchPresenterTest.kt`):

1. **`com.template.di.BaseTestGraph`** is an `interface` (not annotated) that mirrors `AppGraph`'s providers, but with `MockEngine` swapped in for `OkHttp`. Per-test graphs extend it.
2. Each test file declares its own `@DependencyGraph(AppScope::class)` interface that **`: BaseTestGraph`** and exposes the specific factory it needs (e.g. `val presenterFactory: SearchPresenter.Factory`). The graph factory takes `@Provides application: Application` + `@Provides engine: MockEngine`.
3. The test runs under `@RunWith(RobolectricTestRunner::class)` and uses `testApplication()` (in `app/src/test/java/com/template/TestApplication.kt`) to obtain the Robolectric `Application`. `MainDispatcherRule` (top-level file in the test package) installs an `UnconfinedTestDispatcher` as `Dispatchers.Main`.
4. HTTP responses come from a single `MockEngine.Queue()` per test class; tests `engine.enqueue { respond(...) }` before driving the presenter.

Don't construct `HttpClient` / `ITunesClient` by hand inside a test — extend `BaseTestGraph` so providers like `Json` and `HttpClient` stay consistent with prod and so adding new graph-scoped collaborators doesn't ripple into every test.

### Data layer

- **`data/itunes/ITunesClient.kt`** — example Ktor client; injected into presenters by Metro. The shared `HttpClient`/`Json` instances are provided by `AppGraph` (`SingleIn(AppScope::class)`).
- **`data/settings/`** — `SettingsDataStore.kt` defines `Context.dataStore` plus the `rememberPreference` / `rememberEnumPreference` Composable helpers. Setting *keys* live in `SettingKeys.kt`; the theme engine and settings UI both read them through these helpers, which is what makes theme changes propagate live.

### Theming

The full theming story is in the **README** (color presets, fonts, variable-font axes, `DynamicTheme`). Key facts for editing UI:

- **Always pad with `com.template.ui.theme.Padding` tokens**, not raw `dp`. Example: `Modifier.padding(Padding.medium)`.
- `MainActivity` calls `darkThemeFromSettings()` and reapplies `enableEdgeToEdge` whenever the resolved dark-mode setting flips — don't call `enableEdgeToEdge` from elsewhere.
- For previews/screenshot tests that need a fixed look, pass overrides directly: `TemplateTheme(typography = ..., colorScheme = ..., darkTheme = ...)`.

## Conventions worth knowing

- **String resources only** in Composables — no hardcoded UI text.
- **Previews**: every Composable gets an `@Preview` (use `@Previews` / `@ThemePreviews` from `ui/previews/` for the standard light+dark+font-scale matrix; wrap in `AppPreview { ... }`).
- **Test names**: backtick-quoted, e.g. `` fun \`returns Empty when query is blank\`() ``.
- **Commit prefixes**: `feat:`, `fix:`, `chore:`, `refactor:` (match recent history).
- **Debug vs release on-device**: debug builds get `applicationIdSuffix = ".debug"` and override the launcher icon from `app/src/debug/res/mipmap-*`. Both variants install side-by-side.

## Where to look when

- Release signing, keystore env vars, icon regeneration, full theming guide → `README.md`.
- Adding a new screen → copy `screens/search/` (with assisted factory) or `screens/home/` (without).
- Adding a new Material symbol icon → `scripts/download_material_symbol.sh`.
