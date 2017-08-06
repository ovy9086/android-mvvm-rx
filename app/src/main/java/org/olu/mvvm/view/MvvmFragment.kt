package org.olu.mvvm.view

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

open class MvvmFragment : Fragment() {

    val subscriptions = CompositeDisposable()

    fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    override fun onStop() {
        super.onStop()
        subscriptions.dispose()
    }
}
