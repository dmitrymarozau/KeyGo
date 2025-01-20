# Setup and Check your environment

1. Open the KeyGo app project [build.gradle.kts](../build.gradle.kts) in Android Studio
1. Build the `github-debug`(_default_) variant of the app (Build > Select Variant > githubDebug(_default_) >> Build > Make Project)
1. Run an android emulator from Android Studio (Tools > Device Manager > Run an emulator)
1. Run the [example test spec](./spec/example.spec.js) `npm run test -- --spec spec/example.spec.js` in the terminal

**Note:** The wdio config file is located at [wdio.conf.js](./wdio.conf.js). It is configured to start the appium server automatically before running the tests. UiAutomator2 is included automatically as a dependency.

# Connecting Appium Inspector

1. Run the appium server `npm run appium`
1. Open Appium Inspector, use the following desired capabilities to connect to the app, _make sure the app is running on the emulator_:
```json
{
  "appium:deviceName": "Android",
  "appium:automationName": "UiAutomator2",
  "appium:app": "<Path to your repo>/KeyGo/app/build/outputs/apk/github/debug/app-github-debug.apk",
  "platformName": "Android"
}
```
