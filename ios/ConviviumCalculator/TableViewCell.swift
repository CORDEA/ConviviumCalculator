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

    @IBAction func switched(sender: UISwitch) {
        guard let realm = try? Realm() else {
            return
        }
        
        if let text = nameLabel.text {
            guard let item = realm.objects(ListItem).filter("name = '" + text + "'").first else {
                return
            }
            _ = try? realm.write({
                item.isSwitch = priceSwitch.on
            })
        }
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
