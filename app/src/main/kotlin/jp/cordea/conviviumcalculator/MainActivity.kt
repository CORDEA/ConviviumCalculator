package jp.cordea.conviviumcalculator

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import butterknife.bindView
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.startActivity
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val listView: ListView by bindView(R.id.listview)

    val fab: FloatingActionButton by bindView(R.id.fab)
    val sumFab: FloatingActionButton by bindView(R.id.sum_fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView.adapter = ListAdapter(this)

        fab.setOnClickListener {
            startActivity<InputDataActivity>()
        }
        sumFab.setOnClickListener {
            val res = calc()
            alert {
                title(R.string.dialog_title)
                message("all: %d\nrecovered: %d".format(res[0], res[1]))
            }.show()
        }
    }

    private fun calc(): IntArray {
        Realm.getInstance(this).let {
            val items = it.allObjects(ListItem::class.java)
            var all = 0
            var recovered = 0
            for (item in items) {
                all += item.price
                recovered += if (item.switch) item.price else 0
            }
            return intArrayOf(all, recovered)
        }
    }

    override fun onStart() {
        super.onStart()
        Realm.getInstance(this).let {
            val items = it.allObjects(ListItem::class.java).toTypedArray()
            listView.adapter?.let{
                val i = it as ListAdapter
                i.items = items
                i.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
