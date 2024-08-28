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

