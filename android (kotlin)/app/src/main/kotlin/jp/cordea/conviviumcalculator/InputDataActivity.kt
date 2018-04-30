package jp.cordea.conviviumcalculator

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.realm.Realm
import jp.cordea.conviviumcalculator.databinding.ActivityInputDataBinding

class InputDataActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityInputDataBinding>(
                this,
                R.layout.activity_input_data
        )
        setSupportActionBar(binding.toolbar)

        val content = binding.content!!
        content.editText.text = SpannableStringBuilder(objToString())
        binding.fab.setOnClickListener {
            val text = content.editText.text.toString()
            stringToObj(text)
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun stringToObj(csv: String) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(ListItem::class.java).findAll().deleteAllFromRealm()
        Observable
                .just(csv)
                .flatMap { Observable.fromIterable(it.split('\n')) }
                .map { it.split(',').toTypedArray() }
                .filter { it.isNotEmpty() }
                .doOnNext {
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
                .addTo(compositeDisposable)
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
