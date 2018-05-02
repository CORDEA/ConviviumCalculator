package jp.cordea.conviviumcalculator

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import io.realm.Realm
import jp.cordea.conviviumcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
        )
        setSupportActionBar(binding.toolbar)

        Realm.init(this)

        binding.content!!.listView.adapter = ListAdapter(this)

        val context: Context = this
        binding.fab.setOnClickListener {
            val intent = Intent(context, InputDataActivity::class.java)
            startActivity(intent)
        }
        binding.sumFab.setOnClickListener {
            CollectionStateDialogFragment.newInstance()
                    .show(supportFragmentManager, CollectionStateDialogFragment.TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        Realm.getDefaultInstance().let {
            val items = it.where(ListItem::class.java).findAll().toTypedArray()
            binding.content!!.listView.adapter?.let {
                val i = it as ListAdapter
                i.items = items
                i.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }
}
