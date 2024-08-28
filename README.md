
## Prerequisites:
- **Version 15.3 (15E204a)
- **Unity** 2022.3.16f1

Make sure the Android device is connected and USB Debugging is enabled.

## Steps to Integrate Unity freamework in Project for iOS app:

1. First, create an empty iOS project inside a workspace. A simple way to do this is to first create an empty Xcode project, and then go to `File → Save As Workspace`.
   Let’s name both the project and the workspace SwiftUnity.
   Important: Make sure you select Storyboard as the Interface, these steps are needed even if you want to use SwiftUI as they are essential to get the Unity integration working.

![Alt text](https://github.com/NarsunOrg/FlutterUnityIntegration/blob/main/Error1.png).

```dart
import 'package:flutter/material.dart';
import 'package:flutter_embed_unity/flutter_embed_unity.dart';

```

5. In `<your flutter app>/android/app/build.gradle`, ensure your `minSdkVersion` is at least 22:

### Video link For Step by Step Integration:
https://drive.google.com/file/d/1i2fat5c2omwLlPAfJ6eR5hzJHvJFVrBH/view

