import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.nodeGradle)
    `maven-publish`
}

// Published as com.chocosawse:design (group and version come from gradle.properties / -Pversion)
publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name = "cs-design"
            description = "Compose Multiplatform design system: palette-driven themes for Android and iOS"
            url = "https://github.com/Chocosawse/cs-design"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Chocosawse/cs-design")
            // CI: GITHUB_ACTOR / GITHUB_TOKEN. Locally: gpr.user / gpr.key in ~/.gradle/gradle.properties
            credentials {
                username = providers.gradleProperty("gpr.user").orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                password = providers.gradleProperty("gpr.key").orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
            }
        }
    }
}

// Design tokens: Style Dictionary turns design-tokens/tokens/**/*.json into Kotlin before compilation.
// Node is downloaded by Gradle, so no local Node install is needed.
val designTokensDir = layout.projectDirectory.dir("design-tokens")
val generatedTokensDir = layout.buildDirectory.dir("generated/designTokens/commonMain/kotlin")

node {
    download = true
    version = libs.versions.node.get()
    distBaseUrl = null // the Node.js download repo is declared in settings.gradle.kts
    nodeProjectDir = designTokensDir
}

val generateDesignTokens = tasks.register<NpmTask>("generateDesignTokens") {
    description = "Generates Colors.kt from design-tokens/ with Style Dictionary"
    group = "build"
    dependsOn(tasks.npmInstall)
    npmCommand = listOf("run", "build")
    environment = generatedTokensDir.map { mapOf("SD_BUILD_PATH" to it.asFile.absolutePath) }
    inputs.dir(designTokensDir.dir("tokens"))
    inputs.file(designTokensDir.file("config.mjs"))
    outputs.dir(generatedTokensDir)
    val outputDir = generatedTokensDir
    doFirst { outputDir.get().asFile.deleteRecursively() }
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    android {
        namespace = "com.chocosawse.design"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        withHostTest {}
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateDesignTokens.map { generatedTokensDir })
        }
        commonMain.dependencies {
            // api: these types appear in this module's public API (Color, @Composable, ColorScheme)
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.material3)
            implementation(libs.compose.foundation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
