plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.spotless)
}

val ktfmtVersion = libs.versions.ktfmt.get()

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktfmt(ktfmtVersion).kotlinlangStyle()
            trimTrailingWhitespace()
            endWithNewline()
            targetExclude("**/spotless.kt")
        }
        format("kotlinGradle") {
            target("**/*.gradle.kts")
            targetExclude("**/build/**/*.gradle.kts")
            // Spotless's Gradle support usually handles KTS via kotlin or a generic format
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
            trimTrailingWhitespace()
            leadingTabsToSpaces(4)
            endWithNewline()
        }
    }
}
