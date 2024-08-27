# UnityIntegrationNativeAndroidAndIOS

## Prerequisites:
- **Android Studio Koala | 2024.1.1 Patch 2** 
- **Kotlin** 1.9.0
- **Unity** 2022.3.21f1

Make sure the Android device is connected and USB Debugging is enabled.

## Steps to Integrate Unity .aar file for Android App using kotlin:

1. Create libs folder `/app/libs` Extract and copy the `unityLibrary.aar` file into the `libs` folder.

2. Add the following dependencies to your app level `build.gradle.kts` file `/app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("libs/unityLibrary.aar"))
}
```
3. In `/app/build.gradle`, ensure your `minSdkVersion` is at least 22:

```kotlin
  defaultConfig {
    applicationId = "com.demo.unitykotlindemo"
    minSdk = 22
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
        useSupportLibrary = true
    }
}
```
4. Add flatDir { dirs("libs")} to the Gradle settings in `<your project>/settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dirs("libs")
        }


    }
}
```

5. Clean and Rebuild
```bash
./gradlew clean

```
6. Modify your `MainActivity` class. This is usually located at `<Android project>//app/src/main/kotlin/com/<your org>/MainActivity.kt`:

```kotlin

import com.unity3d.player.UnityPlayerActivity

```
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnityKotlinDemo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { callGamePlay() }
                        ) {
                            Text(text = "Start UnityPlayer")
                        }
                    }
                }
            }
        }
    }


    private fun callGamePlay() {
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent);
    }
```
7. Connect your Android device and run .

### Video link For Step by Step Integration:
https://drive.google.com/file/d/1n-GDNSdlRpUclPxvpq3cwa4_cJg-XcK_/view?usp=sharing

