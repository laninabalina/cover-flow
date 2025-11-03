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
