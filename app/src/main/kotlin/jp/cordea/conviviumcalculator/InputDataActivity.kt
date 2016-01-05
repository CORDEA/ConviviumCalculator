package jp.cordea.conviviumcalculator

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.widget.EditText
import butterknife.bindView
import io.realm.Realm
import rx.Observable

class InputDataActivity : AppCompatActivity() {

    private val editText: EditText by bindView(R.id.edittext)
    private val fab: FloatingActionButton by bindView(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        editText.text = SpannableStringBuilder(objToString())
        fab.setOnClickListener {
            val text = editText.text.toString()
            Realm.getInstance(this).let {
                it.beginTransaction()
                it.allObjects(ListItem::class.java).clear()
                if (!text.isEmpty()) {
                    for (arr in stringToObj(text)) {
                        if (arr.size < 2) {
                            continue
                        }
                        var item = it.createObject(ListItem::class.java)

                        item.name = arr[0]
                        item.price = arr[1].toInt()
                        item.switch = if (arr.size > 2) arr[2].toBoolean() else false
                    }
                }
                it.commitTransaction()
                it.close()
                finish()
            }
        }
    }

    private fun stringToObj(csv: String): Array<Array<String>> {
        return Observable
                .just(csv)
                .flatMap { Observable.from(it.split('\n')) }
                .map { it.split(',').toTypedArray() }
                .toList()
                .toBlocking()
                .first()
                .toTypedArray();
    }

    private fun objToString(): String {
        Realm.getInstance(this).let {
            return Observable
                    .from(it.allObjects(ListItem::class.java))
                    .map { "%s,%d,%s".format(it.name, it.price, it.switch.toString()) }
                    .toList()
                    .toBlocking()
                    .first()
                    .joinToString("\n")
        }
    }
}
