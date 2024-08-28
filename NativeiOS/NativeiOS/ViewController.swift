//
//  ViewController.swift
//  NativeiOS
//
//  Created by Apple on 28/08/2024.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    @IBAction func btnOpen(_ sender: UIButton) {
            Unity.shared.show()
    }


}

