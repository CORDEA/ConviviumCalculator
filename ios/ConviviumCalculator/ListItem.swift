//
//  ListItem.swift
//  ConviviumCalculator
//
//  Created by Yoshihiro Tanaka on 2016/01/04.
//
//

import Foundation
import RealmSwift
import Realm

class ListItem: Object {
    dynamic var name: String? = nil
    dynamic var price: Int = 0
    dynamic var isSwitch: Bool = false
    
    override static func primaryKey() -> String? {
        return "name"
    }
}