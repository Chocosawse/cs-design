# cs-design

Compose Multiplatform design system for Android and iOS: palette-driven light/dark themes that feed Material 3.

- `ColorPalette`: a brand palette (built-in palettes, or your own); `ColorPalette.builtIns` lists the built-ins
- `AppTheme { }`: provides `AppColors` and a matching `MaterialTheme`, with every Material colour role derived
  from the palette; read colors with `AppTheme.colors`
- `palette.contrastIssues()`: WCAG AA (4.5:1) problems in a palette's light and dark themes; every built-in
  palette returns an empty list

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
   `AppTheme` also takes `typography` and `shapes`, passed to its `MaterialTheme`, so there's no need to
   nest a second one:
   ```kotlin
   AppTheme(activeToken = palette, typography = MyTypography, shapes = MyShapes) { ... }
   ```

## Custom palettes

```kotlin
val Sand = ColorPalette(
    name = "Sand",
    primary = ..., primaryNight = ...,
    color100 = ..., color300 = ..., color500 = ..., color700 = ..., color900 = ...,
    complement = ...,
    // Optional; leave out for a white light surface, a dark surface blended from color900 and color700,
    // and Material's baseline error colours
    surface = ..., surfaceNight = ...,
    error = ..., errorNight = ...
)
```

Test it the same way the built-ins are tested:

```kotlin
@Test
fun `Sand meets WCAG AA`() = assertEquals(emptyList(), Sand.contrastIssues())
```

`contrastIssues()` checks text and secondary text on background, surface, secondaryContainer and
surfaceContainerHighest; error on background and surface; and onPrimary and onError on their colours.

## Developing

- Colors live in `design/design-tokens/tokens/`. Style Dictionary generates `com.chocosawse.design.Colors`
  during the build (Gradle downloads Node; no local install needed).
- `./gradlew :design:allTests` runs the contrast tests on Android and iOS.
- `./gradlew publishToMavenLocal` publishes the `-SNAPSHOT` version in `gradle.properties` to `~/.m2` for trying changes in an app
  (add `mavenLocal()` to the app's repositories).

## Releasing

Push a version tag; the Publish workflow tests and publishes it from a macOS runner:

```bash
git tag v0.4.0 && git push origin v0.4.0
```
