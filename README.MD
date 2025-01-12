# Compose Multiplatform Application

## Before running!
 - install JDK 17 on your machine
 - add `local.properties` file to the project root and set a path to Android SDK there

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration

To build the application bundle:
 - run `./gradlew :launcherApp:assembleDebug`
 - find `.apk` file in `launcherApp/build/outputs/apk/debug/launcherApp-debug.apk`

### Desktop
Run the desktop application: `./gradlew :launcherApp:run`

