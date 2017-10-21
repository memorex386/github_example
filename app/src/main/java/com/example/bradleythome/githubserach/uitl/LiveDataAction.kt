package com.example.bradleythome.githubserach.uitl

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import android.support.annotation.MainThread
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by bradley.thome on 10/19/17.
 */
class LiveDataActionComposit {
    //LiveData that backs up our LiveDataAction
    private val liveData = ObservableField<Boolean>(false)

    @MainThread
    fun observe(compositeDisposable: CompositeDisposable, observer: () -> Unit) {
        liveData.subscribe(compositeDisposable) { t ->
            if (t) {
                //call observer
                observer()
                //reset liveData
                liveData.set(false)
            }
        }
    }

    @MainThread
    fun actionOccurred() {
        //set backing liveData to true
        liveData.set(true)
    }
}

class ResultsHolder<T : Any?>(val item: T)

open class BaseLiveDataAction<T : Any?> {
    //LiveData that backs up our LiveDataAction
    val liveData = MutableLiveData<ResultsHolder<T>?>()

    @MainThread
    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {

        // Observe the internal MutableLiveData
        liveData.observe(owner, Observer<ResultsHolder<T>?> { t ->
            t?.let {
                observer(it.item)
                liveData.value = null
            }
        })
    }

    /**
     * This function allows easy testing without needing a LifecycleOwner.
     */
    @MainThread
    fun observeForever(observer: (T) -> Unit) {
        // Observe the internal MutableLiveData
        liveData.observeForever({ t ->
            t?.let {
                observer(it.item)
                liveData.value = null
            }
        })
    }

    @MainThread
    fun actionOccurred(item: T) {
        //set backing liveData to true
        liveData.value = ResultsHolder(item)
    }
}

class LiveDataAction : BaseLiveDataAction<Boolean>() {
    @MainThread
    fun actionOccurred() {
        //set backing liveData to true
        liveData.value = ResultsHolder(true)
    }
}
