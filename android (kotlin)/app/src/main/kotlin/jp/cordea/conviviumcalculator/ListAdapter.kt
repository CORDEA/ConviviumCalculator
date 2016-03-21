package jp.cordea.conviviumcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import io.realm.Realm

/**
 * Created by CORDEA on 2016/01/02.
 */
class ListAdapter(context : Context) : ArrayAdapter<ListItem>(context, R.layout.list_item) {
    var items: Array<ListItem> = arrayOf()

    override fun getItem(position: Int): ListItem? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent)

        val item = getItem(position);
        item ?: return convertView;
        var textView = view.findViewById(R.id.name) as TextView
        textView.text = item.name
        textView = view.findViewById(R.id.price) as TextView
        textView.text = "¥ %,d".format(item.price)
        val switch: Switch = view.findViewById(R.id.item_switch) as  Switch
        switch.isChecked = item.switch

        switch.setOnCheckedChangeListener { compoundButton, b ->
            val realm = Realm.getInstance(context)
            val model = realm.where(ListItem::class.java).equalTo("name", items[position].name).findFirst()
            realm.use {
                model.switch = b
            }
        }
        return view;
    }
}