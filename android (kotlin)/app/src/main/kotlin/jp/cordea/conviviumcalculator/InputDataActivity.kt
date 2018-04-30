package jp.cordea.conviviumcalculator

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
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
        CsvIo.output()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    content.editText.text = SpannableStringBuilder(it)
                }, {
                })
                .addTo(compositeDisposable)
        binding.fab.setOnClickListener {
            val text = content.editText.text.toString()
            CsvIo.input(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        finish()
                    }, {
                        finish()
                    })
                    .addTo(compositeDisposable)
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }
}
