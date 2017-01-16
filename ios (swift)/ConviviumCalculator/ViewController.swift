//
//  ViewController.swift
//  ConviviumCalculator
//
//  Created by Yoshihiro Tanaka on 2016/01/04.
//
//

import UIKit
import Realm
import RealmSwift

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    
    private var items: Results<ListItem>!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        tableView.dataSource = self
        tableView.delegate = self
        
        let button = UIBarButtonItem(
            title: "Sum",
            style: .plain,
            target: self,
            action: #selector(ViewController.onSumPressed(_:))
        )
        self.navigationItem.leftBarButtonItem = button
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if let realm = try? Realm() {
            items = realm.objects(ListItem.self)
            tableView.reloadData()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func onSumPressed(_ sender: UIBarButtonItem) {
        let message = String(format: "all: ¥ %@\nrecovered: ¥ %@", arguments: calc())
        let ac = UIAlertController(
            title: "Summary",
            message: message,
            preferredStyle: .alert
        )
        
        let da = UIAlertAction(
            title: "OK",
            style: .default,
            handler: nil
        )
        ac.addAction(da)
        
        present(ac, animated: true, completion: nil)
    }
    
    private func getNumberFormatter() -> NumberFormatter {
        let fmt = NumberFormatter()
        fmt.numberStyle = .decimal
        fmt.groupingSeparator = ","
        fmt.groupingSize = 3
        return fmt
    }
    
    private func calc() -> [CVarArg] {
        let fmt = getNumberFormatter()
        if let realm = try? Realm() {
            items = realm.objects(ListItem.self)
            var all = 0, recovered = 0
            
            for li in items {
                all += li.price
                recovered += li.isSwitch ? li.price : 0
            }
            if let all = fmt.string(from: NSNumber(value: all)),
                let recovered = fmt.string(from: NSNumber(value: recovered)) {
                return [all, recovered]
            }
        }
        return ["0", "0"]
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseCell", for: indexPath) as! TableViewCell
        
        if let num = getNumberFormatter()
            .string(from: NSNumber(value: items[indexPath.row].price)) {
                cell.nameLabel.text = items[indexPath.row].name
                
                cell.priceLabel.text = String(format: "¥ %@", num)
                cell.priceSwitch.setOn(items[indexPath.row].isSwitch, animated: false)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }
}

