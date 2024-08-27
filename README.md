# UnityIntegrationNativeAndroidAndIOS

## Prerequisites:
- **Android Studio Koala | 2024.1.1 Patch 2** 
- **Kotlin** 1.9.0
- **Unity** 2022.3.21f1

Make sure the Android device is connected and USB Debugging is enabled.

## Steps to Integrate Unity Framework  in Project for Android App using kotlin:

1. Extract and copy the `unityLibrary` folder into the `android` project folder.

2. Add the following dependencies to your app level `build.gradle.kts` file `/app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":unityLibrary"))
}
```
3. In `/app/build.gradle`, ensure your `minSdkVersion` is at least 22:

```kotlin
  defaultConfig {
    applicationId = "com.demo.unitykotlindemo2"
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


4. Add the exported Unity project to the Gradle settings in `<your project>/settings.gradle.kts`:

```kotlin
include(":app", ":unityLibrary")
```
5. Modify build.gradle in unityLibrary:

```groovy
android {
    ndkPath "/Applications/Unity/Hub/Editor/2022.3.21f1/PlaybackEngines/AndroidPlayer/NDK"// Your NDK path
    namespace = "com.unity3d.player"
    compileSdk 34
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
                targetCompatibility JavaVersion.VERSION_11
    }

    defaultConfig {
        minSdkVersion 22
        targetSdkVersion 34
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
        }
        versionCode 1
        versionName '0.1'
        consumerProguardFiles 'proguard-unity.txt'
    }

    lintOptions {
        abortOnError false
    }

    //Define unityStreamingAssets and convert it to a list of file names

    def unityStreamingAssetsDir = file("${rootProject.projectDir}/unityLibrary/src/main/assets")
    def unityStreamingAssets = unityStreamingAssetsDir.exists() ? unityStreamingAssetsDir.listFiles().collect { it.name } : []

    aaptOptions {
        noCompress = ['.unity3d', '.ress', '.resource', '.obb', '.bundle', '.unityexp'] + unityStreamingAssets
        ignoreAssetsPattern = "!.svn:!.git:!.ds_store:!*.scc:!CVS:!thumbs.db:!picasa.ini:!*~"
    }

    packagingOptions {
        doNotStrip '*/armeabi-v7a/*.so'
        doNotStrip '*/arm64-v8a/*.so'
    }
}
```


6. Verify strings.xml file exist in unityLibrary. This is usually located at `<Android project>//unityLibrary/src/main/res/values/strings.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">UnityKotlinDemo</string>
    <string name="unity_player_activity_name">Unity Player</string>
    <string name="game_view_content_description">Game view</string>
</resources>

```
7. Commit the intent-filter code from AndroidManifest. This is usually located at `<Android project>//unityLibrary/src/main/AndroidManifest.xml`:

```xml
    <activity android:name="com.unity3d.player.UnityPlayerActivity" android:theme="@style/UnityThemeSelector" android:screenOrientation="landscape" android:launchMode="singleTask" android:configChanges="mcc|mnc|locale|touchscreen|keyboard|keyboardHidden|navigation|orientation|screenLayout|uiMode|screenSize|smallestScreenSize|fontScale|layoutDirection|density" android:resizeableActivity="false" android:hardwareAccelerated="false" android:exported="true">
    <!--  <intent-filter>
        <category android:name="android.intent.category.LAUNCHER" />
        <action android:name="android.intent.action.MAIN" />
      </intent-filter>-->
    <meta-data android:name="unityplayer.UnityActivity" android:value="true" />
    <meta-data android:name="notch_support" android:value="true" />
</activity>

```





8. Clean and Rebuild
```bash
./gradlew clean

```
9. Modify your `MainActivity` class. This is usually located at `<Android project>//app/src/main/kotlin/com/<your org>/MainActivity.kt`:

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

11. Connect your Android device and run .

### Video link For Step by Step Integration:
https://drive.google.com/file/d/1KVJbazHxo42VOzNaxPdK11_BIE5XwPht/view?usp=sharing

