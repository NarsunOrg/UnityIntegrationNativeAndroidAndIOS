# UnityIntegrationNativeAndroidAndIOS

## Prerequisites:
- **Android Studio Koala | 2024.1.1 Patch 2** 
- **Kotlin** 1.9.0

Make sure the Android device is connected and USB Debugging is enabled.

## Steps to Integrate Unity .aar file for Android App using kotlin:

1. Create libs folder `/app/libs` Extract and copy the `SaudiMetaverseUnity.aar` file into the `libs` folder.

2. Add the following dependencies to your app level `build.gradle.kts` file `/app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("libs/SaudiMetaverseUnity.aar"))
}
```
3. In `/app/build.gradle`, ensure your `minSdkVersion` at least 22 and aaptOptions is addded

```kotlin
android {
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

    aaptOptions {
        noCompress("unity3d", "ress", "resource", "obb")
        ignoreAssetsPattern = "!.svn:!.git:!.ds_store:!*.scc:.*:!CVS:!thumbs.db:!picasa.ini:!*~"
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

import com.unity3d.player.IUnityMessageListener
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity

```
```kotlin
// IUnityMessageListener interface used to share messages between UnityPlayerActivity to MainActivity
class MainActivity : ComponentActivity(), IUnityMessageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // initialize interface
        UnityPlayerActivity.setiUnityMessageListener(this)
        enableEdgeToEdge()
        setContent {
            UnityKotlinDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { callUnityPlayerActivity() }
                        ) {
                            Text(text = "Start UnityPlayer")
                        }
                    }
                }
            }
        }

    }
    //star Unity Player
    private fun callUnityPlayerActivity(){
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent);
    }
    // Message received from unity
    override fun onUnityMessageReceived(route: String?) {
        Log.e("abc:", "MainActivity onUnityMessageReceived: $route")
        sendMessageToUnity(route)
    }
}
// send message to Unity
fun sendMessageToUnity(route: String?) {
    Log.e("abc:", "MainActivity sendMessageToUnity: ", )
    if(route == AndroidRouts.AUTH_TOKEN){
        UnityPlayer.UnitySendMessage("AndroidNativeBridge", "ReceiveMessageFromAndroid", "Paste auth token here...")
    }
}
class AndroidRouts {
    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        const val HOME_PAGE = "HOME_PAGE"
        const val UNITY_INITIALIZED = "UNITY_INITIALIZED"
    }
}
```
7. Connect your Android device and run .

### Video link For Step by Step Integration:
https://drive.google.com/file/d/1GhtkqbqdxgOb5heTPRBjbjAUsqgMLk8o/view?usp=sharing

