# coverflow-template

## Android URL Downloader App

I added a minimal Android app that lets you paste a URL and download it to the device’s Downloads folder using Android’s DownloadManager (this is more reliable than trying to shell out to `wget`/`curl`, which Android devices don’t normally ship with).

### How to build and run

1. Open `android-app/` in Android Studio (2022.1 or newer).
2. Let it sync Gradle.
3. Run the `app` configuration on a device or emulator.
4. Paste any valid `https://` URL and tap “Download”. You’ll get a notification when it completes.

### Notes

- No extra storage permission is needed on Android 10+; for older devices the app requests `WRITE_EXTERNAL_STORAGE`.
- Files are saved to the public Downloads directory with a guessed filename.

Project path: `android-app/`

## CI: Build APK and Artifacts

A GitHub Actions workflow is added at `.github/workflows/build-apk.yml` to automatically build the debug APK and upload it as a workflow artifact.

How to use:
- Push to `main` or trigger manually via “Run workflow”.
- After the job completes, download `app-debug.apk` from the workflow’s Artifacts section.

The workflow:
- Sets up JDK 17 and Android SDK (API 34, Build Tools 34.0.0)
- Installs Gradle via apt
- Runs `gradle assembleDebug` in `android-app/`
- Uploads `android-app/app/build/outputs/apk/debug/app-debug.apk` as `app-debug`
