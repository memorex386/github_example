package com.example.bradleythome.githubserach.extensions

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by bradthome on 12/4/17.
 */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app), CompositeDisposableInterface {

    override val compositeDisposable = CompositeDisposable()

    lateinit var lifecycleOwner: LifecycleOwner

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun <T> ActionItem<T>.observe(observer: (T) -> Unit) {
        observe(lifecycleOwner, observer)
    }

    infix fun <T> ActionItem<T>.attach(liveDataItemAction: ActionItem<T>) {
        observe(lifecycleOwner) {
            liveDataItemAction.actionOccurred(it)
        }
    }

    val app
        get() = App.instance

}

