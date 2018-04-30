package jp.cordea.conviviumcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import io.realm.Realm

class ListAdapter(context: Context) : ArrayAdapter<ListItem>(context, R.layout.list_item) {

    var items: Array<ListItem> = arrayOf()

    override fun getItem(position: Int): ListItem? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)

        item ?: return convertView

        viewHolder.nameTextView.text = item.name
        viewHolder.priceTextView.text = "¥ %,d".format(item.price)

        viewHolder.switch.setOnCheckedChangeListener(null)

        viewHolder.switch.isChecked = item.switch

        viewHolder.switch.setOnCheckedChangeListener { _, b ->
            val realm = Realm.getDefaultInstance()
            val model = realm.where(ListItem::class.java)
                    .equalTo("name", items[position].name)
                    .findFirst()
            realm.beginTransaction()
            model.switch = b
            realm.commitTransaction()
            realm.close()
        }

        return view
    }

    class ViewHolder(view: View) {

        val nameTextView: TextView = view.findViewById<TextView>(R.id.name)

        val priceTextView: TextView = view.findViewById<TextView>(R.id.price)

        val switch: Switch = view.findViewById<Switch>(R.id.item_switch)
    }
}
