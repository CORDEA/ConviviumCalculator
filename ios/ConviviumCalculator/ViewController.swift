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
        
        let button = UIBarButtonItem(title: "Sum", style: .Plain, target: self, action: "onSumPressed:")
        self.navigationItem.leftBarButtonItem = button
    }
    
    override func viewWillAppear(animated: Bool) {
        if let realm = try? Realm() {
            items = realm.objects(ListItem)
            tableView.reloadData()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func onSumPressed(sender: UIBarButtonItem) {
        let message = String(format: "all: ¥ %@\nrecovered: ¥ %@", arguments: calc())
        let ac = UIAlertController(
            title: "Summary",
            message: message,
            preferredStyle: .Alert
        )
        
        let da = UIAlertAction(
            title: "OK",
            style: .Default,
            handler: nil
        )
        ac.addAction(da)
        
        presentViewController(ac, animated: true, completion: nil)
    }
    
    private func getNumberFormatter() -> NSNumberFormatter {
        let fmt = NSNumberFormatter()
        fmt.numberStyle = .DecimalStyle
        fmt.groupingSeparator = ","
        fmt.groupingSize = 3
        return fmt
    }
    
    private func calc() -> [CVarArgType] {
        let fmt = getNumberFormatter()
        if let realm = try? Realm() {
            items = realm.objects(ListItem)
            var all = 0, recovered = 0
            
            for li in items {
                all += li.price
                recovered += li.isSwitch ? li.price : 0
            }
            if let all = fmt.stringFromNumber(all), recovered = fmt.stringFromNumber(recovered) {
                return [all, recovered]
            }
        }
        return ["0", "0"]
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseCell", forIndexPath: indexPath) as! TableViewCell
        
        if let num = getNumberFormatter()
            .stringFromNumber(items[indexPath.row].price) {
                cell.nameLabel.text = items[indexPath.row].name
                
                cell.priceLabel.text = String(format: "¥ %@", num)
                cell.priceSwitch.setOn(items[indexPath.row].isSwitch, animated: false)
        }
        
        return cell
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
    }
}

