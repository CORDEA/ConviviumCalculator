package jp.cordea.conviviumcalculator

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm

class CollectionStateDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "CollectionStateDialogFragment"

        fun newInstance(): CollectionStateDialogFragment =
                CollectionStateDialogFragment()
    }

    private var disposable: Disposable? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context!!
        val dialog = AlertDialog
                .Builder(context)
                .setTitle(R.string.dialog_title)
                .setMessage(context.getString(R.string.dialog_message_format).format(0, 0))
                .create()
        disposable = calculateCurrentCollectionState()
                .subscribe({
                    dialog.setMessage(
                            context.getString(R.string.dialog_message_format)
                                    .format(it.sum, it.collected)
                    )
                }, {
                })
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun calculateCurrentCollectionState(): Single<CollectionState> =
            Single
                    .fromCallable {
                        Realm.getDefaultInstance()
                    }
                    .map { it.where(ListItem::class.java).findAll() }
                    .map {
                        CollectionState(
                                it.sumBy { it.price },
                                it.filter { it.isChecked }.sumBy { it.price }
                        )
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    data class CollectionState(
            val sum: Int,
            val collected: Int
    )
}
