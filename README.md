## Prerequisites:

- \*\*Version 15.3 (15E204a)
- **Unity** 2022.3.16f1

Make sure the iPhone real device is connected and USB Debugging is enabled.

## Steps to Integrate Unity freamework in Project for iOS app:

## Creat iOS Project:

First, create an empty iOS project inside a workspace. A simple way to do this is to first create an empty Xcode project, and then go to `File → Save As Workspace`.
Let’s name both the project and the workspace SwiftUnity.
Important: Make sure you select Storyboard as the Interface, these steps are needed even if you want to use SwiftUI as they are essential to get the Unity integration working.

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_1.png).

Note that if you wanna go for the SwiftUI project, then don’t forget to set the minimum iOS deployment target to 13.0.

## Setting up Unity Project:

Since we’re focusing majorly on iOS here so I won’t dive deep into explaining Unity and would focus on only the part where we will be unloading the Unity module from the iOS app.

However, one significant step which we need to follow is to call `Application.Unload`() in the section where we want to go back to our iOS app from Unity.

So basically what this function does is that it doesn’t entirely destroy the unity instance rather it retains some space within the memory so that if the user wants to open the Unity module multiple times then it wouldn’t require loading the Unity module entirely rather it would utilize the existing retained memory.

exit point where you want to close it and this exit point is linked with some button within the Unity module. So simply call `Application.Unload` as shown below:
Now once you are done with above step open the Unity project, go to File → Build Settings. Here you will need to change the Platform from PC, Mac & Linux Standalone to iOS, then click on the Switch Platform button. You’ll need to wait a bit for Unity to recompile.

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_2.png).

Time to export our Unity project as an iOS project. You don’t need to change any of the settings, just click Build. You will export the Unity project in a folder called UnityExportIOS. So now your folder structure should look like this

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_3.png).

## Setting up Unity Project:

1. Now the fun part begins when we connect the exported Unity project with our iOS app. Open the swiftUnity.xcworkspace, which currently only contains the SwiftUnity project.

2. In Finder, locate the UnityExport project, and drag the
   Unity-iPhone.xcodeproj file to the workspace.

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_4.png).

Now both swiftUnity.xcodeproj and Unity-iPhone.xcodeproj belong to the same workspace.

3. Next, click on swiftUnity project and select the swiftUnity target.

4. From the General menu, scroll down to the Frameworks, Libraries and Embedded Content section. Click on the + button to add a new framework.

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_5.png).

5. Select UnityFramework.framework from the list and add it to the project.
   ![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_6.png).

6. Next, select the Data folder in Unity-iPhone project. In the right panel, you will see a “Target Membership” section. You need to check the box next to UnityFramework.

![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_7.png).

7. Next, open Info.plist and remove the Application Scene Manifest entry. This will temporarily break the app but we’ll resolve it later in the AppDelegate. If you skip this step then you will not be able to go back to your iOS app (upon calling `Application.Unload`) once the unity instance launches.
   ![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_8.png).

8. Now create a swift file in `swiftUnity` and name it Unity. Add the following code to that file.

```swift
//
//  Unity.swift
//  NativeiOS
//
//  Created by Apple on 28/08/2024.
//

import Foundation
import UnityFramework

class Unity: UIResponder, UIApplicationDelegate {

    static let shared = Unity()

    private let dataBundleId: String = "com.unity3d.framework"
    private let frameworkPath: String = "/Frameworks/UnityFramework.framework"

    private var ufw : UnityFramework?
    private var hostMainWindow : UIWindow?

    private var isInitialized: Bool {
        ufw?.appController() != nil
    }

    func show() {
        if isInitialized {
            showWindow()
        } else {
            initWindow()
        }
    }

    func setHostMainWindow(_ hostMainWindow: UIWindow?) {
        self.hostMainWindow = hostMainWindow
    }

    private func initWindow() {
        if isInitialized {
            showWindow()
            return
        }

        guard let ufw = loadUnityFramework() else {
            print("ERROR: Was not able to load Unity")
            return unloadWindow()
        }

        self.ufw = ufw
        ufw.setDataBundleId(dataBundleId)
        ufw.register(self)
        ufw.runEmbedded(
            withArgc: CommandLine.argc,
            argv: CommandLine.unsafeArgv,
            appLaunchOpts: nil
        )
    }

    private func showWindow() {
        if isInitialized {
            ufw?.showUnityWindow()
        }
    }

    private func unloadWindow() {
        if isInitialized {
            ufw?.unloadApplication()
        }
    }

    private func loadUnityFramework() -> UnityFramework? {
        let bundlePath: String = Bundle.main.bundlePath + frameworkPath

        let bundle = Bundle(path: bundlePath)
        if bundle?.isLoaded == false {
            bundle?.load()
        }

        let ufw = bundle?.principalClass?.getInstance()
        if ufw?.appController() == nil {
            let machineHeader = UnsafeMutablePointer<MachHeader>.allocate(capacity: 1)
            machineHeader.pointee = _mh_execute_header

            ufw?.setExecuteHeader(machineHeader)
        }
        return ufw
    }
}

extension Unity: UnityFrameworkListener {

    func unityDidUnload(_ notification: Notification!) {
        ufw?.unregisterFrameworkListener(self)
        ufw = nil
        hostMainWindow?.makeKeyAndVisible()
    }
}


```

9. Open `AppDelegate.swift` and remove all scene-related functions. We will also need to pass the main app window reference to Unity here since that’s the window we’ll return to once we unload the Unity game.
   After these modifications, your `AppDelegate` should look like this:

```swift
//
//  AppDelegate.swift
//  NativeiOS
//
//  Created by Apple on 28/08/2024.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.

        Unity.shared.setHostMainWindow(window)

        return true
    }



}

```

Now in your existing Xcode project go the section where you want to open the unity module, in my case I’ll simply put a button on the storyboard and open the unity module to open its `IBAction`.
That’s it, now it’s time to run and test the app.

## Common issues:

If Xcode is unable to build the project after following all of the steps above and gives an error saying “No such module UnityFramework”, then simply select `UnityFramework` from above and press `⌘+B`:
![Alt text](https://github.com/NarsunOrg/UnityIntegrationNativeAndroidAndIOS/blob/main/blob/ios_9.png).

Once it's done then select Unity-iPhone and do the same and in the end select back your iOS project i.e swiftUnity in our case and run the project.

Another important note to keep in mind is to run the project on a real device rather than on a simulator.

### Video link For Step by Step Integration:

https://drive.google.com/file/d/13Yfole8G1RYmE31W2kFlsWqEpVrlyPaw/view?usp=sharing
