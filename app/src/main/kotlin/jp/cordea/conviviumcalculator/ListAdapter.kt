package jp.cordea.conviviumcalculator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import com.pawegio.kandroid.find
import com.pawegio.kandroid.inflateLayout
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
        val view = convertView ?: context.inflateLayout(R.layout.list_item, parent)

        val item = getItem(position);
        item ?: return convertView;
        var textView = view.find<TextView>(R.id.name)
        textView.text = item.name
        textView = view.find<TextView>(R.id.price)
        textView.text = "¥ %,d".format(item.price)
        val switch: Switch = view.find<Switch>(R.id.item_switch)
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