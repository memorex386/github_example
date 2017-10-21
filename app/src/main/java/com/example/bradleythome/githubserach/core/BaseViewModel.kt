package com.example.bradleythome.githubserach.core

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.example.bradleythome.githubserach.uitl.subscribe
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by bradley.thome on 10/17/17.
 */
abstract class BaseViewModel constructor(val app: Application) : AndroidViewModel(app) {

    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun <T> subscribe(observableField: ObservableField<T>, subscription: (T) -> Unit) {
        compositeDisposable.subscribe(observableField, subscription)
    }
}
