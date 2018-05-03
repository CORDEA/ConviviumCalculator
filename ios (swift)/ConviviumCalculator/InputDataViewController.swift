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
        
        CsvIo.output()
            .subscribe(onSuccess: { [unowned self] (csv) in
                self.textView.text = csv
            }, onError: { (_) in

            })
            .disposed(by: disposeBag)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func onRegister(_ sender: Any) {
        if let text = textView.text {
            CsvIo.input(text)
                .subscribe(onCompleted: {

                }, onError: { (_) in

                })
                .disposed(by: disposeBag)
        }
    }
}
