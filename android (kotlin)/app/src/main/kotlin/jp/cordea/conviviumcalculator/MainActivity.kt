package jp.cordea.conviviumcalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import butterknife.bindView
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

        Realm.init(this)

        listView.adapter = ListAdapter(this)

        val context: Context = this
        fab.setOnClickListener {
            val intent = Intent(context, InputDataActivity::class.java)
            startActivity(intent)
        }
        val dialog: AlertDialog =
                AlertDialog
                        .Builder(context)
                        .setTitle(R.string.dialog_title)
                        .create()
        sumFab.setOnClickListener {
            val res = calc()
            dialog.setMessage(context.getString(R.string.dialog_message_format)
                    .format(res[0], res[1]))
            dialog.show()
        }
    }

    private fun calc(): IntArray {
        Realm.getDefaultInstance().let {
            val items = it.where(ListItem::class.java).findAll()
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
        Realm.getDefaultInstance().let {
            val items = it.where(ListItem::class.java).findAll().toTypedArray()
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
