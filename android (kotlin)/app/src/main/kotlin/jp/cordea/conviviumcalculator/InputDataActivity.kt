package jp.cordea.conviviumcalculator

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.widget.EditText
import butterknife.bindView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm

class InputDataActivity : AppCompatActivity() {

    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private val editText: EditText by bindView(R.id.edittext)

    private val fab: FloatingActionButton by bindView(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)
        setSupportActionBar(toolbar)

        editText.text = SpannableStringBuilder(objToString())
        fab.setOnClickListener {
            val text = editText.text.toString()
            stringToObj(text)
        }
    }

    override fun onPause() {
        super.onPause()
        compositeSubscription.clear()
    }

    private val compositeSubscription = CompositeDisposable()

    private fun stringToObj(csv: String) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(ListItem::class.java).findAll().deleteAllFromRealm()
        compositeSubscription.add(
                Observable
                        .just(csv)
                        .flatMap { Observable.fromIterable(it.split('\n')) }
                        .map { it.split(',').toTypedArray() }
                        .filter { it.size > 1 }
                        .map {
                            it[1].toIntOrNull()?.let { price ->
                                val item = realm.createObject(ListItem::class.java, it[0])
                                item.price = price
                                item.switch = if (it.size > 2) it[2].toBoolean() else false
                            }
                        }
                        .doOnComplete {
                            realm.commitTransaction()
                            realm.close()
                            finish()
                        }
                        .doOnError { finish() }
                        .subscribe()
        )
    }

    private fun objToString(): String {
        Realm.getDefaultInstance().let {
            return Observable
                    .fromIterable(it.where(ListItem::class.java).findAll())
                    .map { "%s,%d,%s".format(it.name, it.price, it.switch.toString()) }
                    .toList()
                    .blockingGet()
                    .joinToString("\n")
        }
    }
}
