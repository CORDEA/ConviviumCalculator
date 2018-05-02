package jp.cordea.conviviumcalculator

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ListItem : RealmObject() {

    @PrimaryKey
    open var name: String? = null

    open var price: Int = 0

    open var isChecked: Boolean = false
}
