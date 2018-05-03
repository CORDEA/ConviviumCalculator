//
//  TableViewCell.swift
//  ConviviumCalculator
//
//  Created by Yoshihiro Tanaka on 2016/01/04.
//
//

import UIKit
import Realm
import RealmSwift

class TableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var priceSwitch: UISwitch!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @IBAction func switched(_ sender: UISwitch) {
        guard let realm = try? Realm() else {
            return
        }
        
        if let text = nameLabel.text {
            guard let item = realm
                .objects(ListItem.self)
                .filter(NSPredicate(format: "name = %@", text))
                .first else {
                return
            }
            _ = try? realm.write({
                item.isChecked = priceSwitch.isOn
            })
        }
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
