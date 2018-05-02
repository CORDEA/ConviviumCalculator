package jp.cordea.conviviumcalculator

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm

object CsvIo {

    fun input(csv: String): Completable =
            Single
                    .fromCallable {
                        Realm.getDefaultInstance().apply {
                            beginTransaction()
                            where(ListItem::class.java).findAll().deleteAllFromRealm()
                        }
                    }
                    .flatMapObservable { realm ->
                        Single.just(csv)
                                .flatMapObservable { Observable.fromIterable(it.split('\n')) }
                                .map { it.split(',').toTypedArray() }
                                .filter { it.isNotEmpty() }
                                .doOnNext {
                                    it[1].toIntOrNull()?.let { price ->
                                        val item = realm.createObject(ListItem::class.java, it[0])
                                        item.price = price
                                        item.isChecked = if (it.size > 2) it[2].toBoolean() else false
                                    }
                                }
                                .doOnComplete {
                                    realm.commitTransaction()
                                    realm.close()
                                }
                    }
                    .ignoreElements()

    fun output(): Single<String> =
            Single
                    .fromCallable {
                        Realm.getDefaultInstance().run {
                            where(ListItem::class.java).findAll()
                        }
                    }
                    .flatMapObservable { Observable.fromIterable(it) }
                    .map { "%s,%d,%s".format(it.name, it.price, it.isChecked.toString()) }
                    .toList()
                    .map { it.joinToString("\n") }
}
