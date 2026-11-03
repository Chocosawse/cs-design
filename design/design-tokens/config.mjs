// Style Dictionary config: turns tokens/**/*.json into platform code.
// Gradle passes SD_BUILD_PATH (see core/designsystem/build.gradle.kts); running `npm run build` by hand writes to ./build.
const buildPath = (process.env.SD_BUILD_PATH ?? 'build').replace(/\/?$/, '/');

export default {
  source: ['tokens/**/*.json'],
  platforms: {
    // Compose Multiplatform: one Kotlin object used by both Android and iOS
    compose: {
      transformGroup: 'compose',
      buildPath,
      files: [
        {
          destination: 'com/chocosawse/design/Colors.kt',
          format: 'compose/object',
          filter: (token) => token.$type === 'color',
          options: {
            className: 'Colors',
            packageName: 'com.chocosawse.design',
          },
        },
      ],
    },
  },
};
