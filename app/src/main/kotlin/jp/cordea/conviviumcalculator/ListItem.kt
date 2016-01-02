package jp.cordea.conviviumcalculator

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by CORDEA on 2016/01/01.
 */
public open class ListItem : RealmObject() {
    @PrimaryKey
    public open var name: String? = null

    public open var price: Int = 0
    public open var switch: Boolean = false
}