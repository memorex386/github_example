package com.example.bradleythome.githubserach.uitl

import android.databinding.BaseObservable
import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bradley.thome on 10/17/17.
 */

fun <T> CompositeDisposable.asyncMain(single: Single<T>, subscription: (T) -> Unit, failed: (Throwable) -> Unit) {
    add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscription, failed))
}

fun <T> CompositeDisposable.subscribe(observableField: ObservableField<T>, subscription: (T) -> Unit) {
    add(observableField.toObservable().subscribe(subscription))
}

fun <T> ObservableField<T>.subscribe(compositeDisposable: CompositeDisposable, subscription: (T) -> Unit) {
    compositeDisposable.add(toObservable().subscribe(subscription))
}

fun <T> ObservableField<T>.toObservable(): Observable<T> {
    return Observable.create({ emitter ->

        val callback = object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(dataBindingObservable: android.databinding.Observable, propertyId: Int) {
                if (dataBindingObservable == this@toObservable) {
                    emitter.onNext(this@toObservable.get())
                }
            }
        }

        this@toObservable.addOnPropertyChangedCallback(callback)

        emitter.setCancellable({ this@toObservable.removeOnPropertyChangedCallback(callback) })
    })
}

fun BaseObservable.onChange(action: () -> Unit) {
    this.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
            action()
        }
    })
}
