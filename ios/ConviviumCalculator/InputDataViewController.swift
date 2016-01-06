//
//  InputDataViewController.swift
//  ConviviumCalculator
//
//  Created by Yoshihiro Tanaka on 2016/01/04.
//
//

import UIKit
import RealmSwift
import RxSwift
import RxCocoa
import RxBlocking

class InputDataViewController: UIViewController {

    @IBOutlet weak var textView: UITextView!
    
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        textView.text = objToString()
        
        let button = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Done, target: self, action: "buttonPressed:")
        self.navigationItem.rightBarButtonItem = button
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func buttonPressed(sender: UIBarButtonItem) {
        let text = textView.text
        stringToObj(text)
    }
    
    private func stringToObj(csv: String) {
        if let realm = try? Realm() {
            realm.beginWrite()
            realm.deleteAll()
           
            Observable
                .just(csv)
                .flatMap {
                    $0.componentsSeparatedByString("\n").toObservable()
                }
                .map {
                    $0.componentsSeparatedByString(",")
                }
                .filter {
                    return $0.count > 1
                }
                .map { it -> ListItem in
                    let item = ListItem()
                    item.name = it[0]
                    item.price = Int(it[1]) ?? 0
                    item.isSwitch = it.count > 2 ? NSString(string: it[2]).boolValue : false
                    return item
                }
                .map { it in
                    realm.add(it)
                }
                .subscribeCompleted({ _ in
                    _ = try? realm.commitWrite()
                })
                .addDisposableTo(disposeBag)
        }
    }
    
    private func objToString() -> String? {
        if let realm = try? Realm() {
            let lines = try?
                realm.objects(ListItem)
                    .toObservable()
                    .map { it -> String in
                        if let name = it.name {
                            return name + "," + String(it.price) + "," + String(it.isSwitch)
                        }
                        return ""
                    }
                    .toArray()
                    .toBlocking()
                    .first() ?? []
            if let lines = lines {
                return lines.joinWithSeparator("\n")
            }
        }
        return nil
    }
}
