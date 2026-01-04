# AGENTS.md

Android app template built with Jetpack Compose, Circuit (navigation/state), and Metro (DI).

## Build & Test

```bash
./gradlew :app:assembleDebug          # Build debug APK
./gradlew test                        # Run all unit tests
./gradlew :app:testDebugUnitTest      # Run app unit tests
./gradlew lint                        # Run linting
```

## Paparazzi (Screenshot Testing)

```bash
./gradlew recordPaparazziDebug        # Record/update screenshots
./gradlew verifyPaparazziDebug        # Verify screenshots
```

## Conventions

- **UI Text**: Always use string resources for localization. Do not hardcode strings in Composables.
- **Padding**: Use `com.template.ui.theme.Padding` values instead of hardcoded `dp` values for spacing and padding.
  - Example: `Modifier.padding(Padding.medium)`
- **Test Names**: Use backtick-quoted names for better readability (e.g., `` fun `should return true when...`() ``).
- **Commit Messages**: Use prefixes like `feature:`, `fix:`, `chore:`, `refactor:`.
- **Composable Previews**: Always add `@Preview` annotated functions for new composables.
- **DI**: Use Metro for dependency injection.
