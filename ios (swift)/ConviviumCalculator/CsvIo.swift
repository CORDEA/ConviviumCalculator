//
//  CsvIo.swift
//  ConviviumCalculator
//
//  Created by Yoshihiro Tanaka on 2018/05/03.
//

import RxSwift
import Realm
import RealmSwift

class CsvIo {

    static func input(_ csv: String) -> Completable {
        return Single
            .create(subscribe: { (single) -> Disposable in
                do {
                    single(.success(try Realm()))
                } catch let error {
                    single(.error(error))
                }
                return Disposables.create()
            })
            .do(onSuccess: { (realm: Realm) in
                realm.beginWrite()
                realm.deleteAll()
            })
            .asObservable()
            .flatMap { (realm: Realm) -> Observable<ListItem> in
                return createListItemFromCsv(csv)
                    .do(onNext: { (item) in
                        realm.add(item)
                    }, onCompleted: {
                        _ = try? realm.commitWrite()
                    })
            }
            .ignoreElements()
    }

    static func output() -> Single<String> {
        return Single
            .create(subscribe: { (single) -> Disposable in
                do {
                    single(.success(try Realm()))
                } catch let error {
                    single(.error(error))
                }
                return Disposables.create()
            })
            .map { (realm: Realm) in
                return realm.objects(ListItem.self)
            }
            .asObservable()
            .flatMap { (items: Results<ListItem>) -> Observable<ListItem> in
                return Observable.from(items)
            }
            .map { (item: ListItem) in
                if let name = item.name {
                    return String(format: "%@,%d,%@", name, item.price, String(item.isSwitch))
                }
                return ""
            }
            .toArray()
            .asSingle()
            .map { (items: [String]) -> String in
                return items.joined(separator: "\n")
            }
    }

    private static func createListItemFromCsv(_ csv: String) -> Observable<ListItem> {
        return Single.just(csv)
            .asObservable()
            .flatMap { (lines: String) -> Observable<Array<Substring>> in
                return Observable.from(optional: lines.split(separator: "\n"))
            }
            .flatMap { (lines: Array<Substring>) -> Observable<Substring> in
                return Observable.from(lines)
            }
            .map { (line: Substring) in
                return line.split(separator: ",")
            }
            .map { (line: [Substring]) in
                return line.map { String($0) }
            }
            .filter { (line: [String]) -> Bool in
                return !line.isEmpty
            }
            .map { (line: [String]) -> ListItem in
                let item = ListItem()
                item.name = line[0]
                item.price = Int(line[1]) ?? 0
                item.isSwitch = line.count > 2 ? NSString(string: line[2]).boolValue : false
                return item
        }
    }
}
