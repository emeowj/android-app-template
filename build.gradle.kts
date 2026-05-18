import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.spotless)
}

val ktlintEditorConfig = rootProject.file(".editorconfig")
val ktlintEditorConfigOverride = mapOf(
    "ij_kotlin_allow_trailing_comma" to "true",
    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
    "ktlint_standard_function-naming" to "disabled",
    "ktlint_standard_max-line-length" to "disabled",
    "ktlint_standard_property-naming" to "disabled",
)

spotless {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
            .setEditorConfigPath(ktlintEditorConfig)
            .editorConfigOverride(ktlintEditorConfigOverride)
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("build/**/*.kt")
            ktlint()
                .setEditorConfigPath(ktlintEditorConfig)
                .editorConfigOverride(ktlintEditorConfigOverride)
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
                .setEditorConfigPath(ktlintEditorConfig)
                .editorConfigOverride(ktlintEditorConfigOverride)
        }
        format("xml") {
            target("src/**/*.xml")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
