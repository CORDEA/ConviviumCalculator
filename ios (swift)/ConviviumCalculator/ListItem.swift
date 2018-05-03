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
    @objc dynamic var name: String? = nil
    @objc dynamic var price: Int = 0
    @objc dynamic var isChecked: Bool = false
    
    override static func primaryKey() -> String? {
        return "name"
    }
}
