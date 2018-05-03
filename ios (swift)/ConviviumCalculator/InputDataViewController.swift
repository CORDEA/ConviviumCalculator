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
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func onRegister(_ sender: Any) {
        let text = textView.text
        stringToObj(text!)
    }
    
    private func stringToObj(_ csv: String) {
        if let realm = try? Realm() {
            realm.beginWrite()
            realm.deleteAll()
           
            Observable
                .just(csv)
                .flatMap {
                    Observable.from($0.components(separatedBy: "\n"))
                }
                .map {
                    $0.components(separatedBy: ",")
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
                .subscribe(onNext: nil, onError: nil, onCompleted: {
                    _ = try? realm.commitWrite()
                }, onDisposed: nil)
                .disposed(by: disposeBag)
        }
    }
    
    private func objToString() -> String? {
        if let realm = try? Realm() {
            let lines = try?
                Observable.from(realm.objects(ListItem.self))
                    .map { it -> String in
                        if let name = it.name {
                            return String(format: "%@,%d,%@", name, it.price, String(it.isSwitch))
                        }
                        return ""
                    }
                    .toArray()
                    .toBlocking()
                    .first() ?? []
            if let lines = lines {
                return lines.joined(separator: "\n")
            }
        }
        return nil
    }
}
