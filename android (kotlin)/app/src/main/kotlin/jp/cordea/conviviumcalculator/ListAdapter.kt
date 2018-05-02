package jp.cordea.conviviumcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.realm.Realm
import jp.cordea.conviviumcalculator.databinding.ListItemBinding

class ListAdapter(context: Context) : ArrayAdapter<ListItem>(context, R.layout.list_item) {

    var items: Array<ListItem> = arrayOf()

    override fun getItem(position: Int): ListItem? =
            items[position]

    override fun getCount(): Int =
            items.size

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

        viewHolder.binding.run {
            name.text = item.name
            price.text = "¥ %,d".format(item.price)

            itemSwitch.setOnCheckedChangeListener(null)

            itemSwitch.isChecked = item.switch

            itemSwitch.setOnCheckedChangeListener { _, b ->
                val realm = Realm.getDefaultInstance()
                val model = realm.where(ListItem::class.java)
                        .equalTo("name", items[position].name)
                        .findFirst()
                realm.beginTransaction()
                model!!.switch = b
                realm.commitTransaction()
                realm.close()
            }
        }

        return view
    }

    class ViewHolder(view: View) {

        val binding = ListItemBinding.bind(view)
    }
}
