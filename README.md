# cs-design

Compose Multiplatform design system for Android and iOS: palette-driven light/dark themes that feed Material 3.

- `ColorPalette`: a brand palette (built-in Pantone palettes, or your own)
- `AppTheme { }`: provides `AppColors` and a matching `MaterialTheme`; read colors with `AppTheme.colors`
- Every built-in palette is tested for WCAG AA contrast in light and dark mode

## Using it in a project

Packages are private on GitHub Packages, so downloading needs a GitHub token with `read:packages`.

1. Add credentials to `~/.gradle/gradle.properties` (never commit these):
   ```properties
   gpr.user=Chocosawse
   gpr.key=<token with read:packages>
   ```
   With the GitHub CLI: `gh auth refresh -s read:packages`, then use `gh auth token` as the key.

2. Add the repository in `settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           // ...google(), mavenCentral()
           maven {
               name = "csDesign"
               url = uri("https://maven.pkg.github.com/Chocosawse/cs-design")
               credentials {
                   username = providers.gradleProperty("gpr.user").orNull
                   password = providers.gradleProperty("gpr.key").orNull
               }
               content { includeGroup("com.chocosawse") }
           }
       }
   }
   ```

3. Depend on it from `commonMain`:
   ```kotlin
   implementation("com.chocosawse:design:<version>")
   ```

4. Wrap your UI:
   ```kotlin
   AppTheme(activeToken = ColorPalette.PantoneEmerald) {
       Text("Hello", color = AppTheme.colors.textPrimary)
   }
   ```

## Developing

- Colors live in `design/design-tokens/tokens/`. Style Dictionary generates `com.chocosawse.design.Colors`
  during the build (Gradle downloads Node; no local install needed).
- `./gradlew :design:allTests` runs the contrast tests on Android and iOS.
- `./gradlew publishToMavenLocal` publishes `0.1.0-SNAPSHOT` to `~/.m2` for trying changes in an app
  (add `mavenLocal()` to the app's repositories).

## Releasing

Push a version tag; the Publish workflow tests and publishes it from a macOS runner:

```bash
git tag v0.1.0 && git push origin v0.1.0
```
