package com.example.bradleythome.githubserach.uitl

import android.arch.lifecycle.*
import android.support.annotation.MainThread
import com.example.bradleythome.githubserach.extensions.Observe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by bradley.thome on 10/19/17.
 */
class ResultsHolder<out T : Any?>(val item: T)

interface ActionInterface<T> {

    fun observe(compositeDisposable: CompositeDisposable, observer: (T) -> Unit): Any?

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit): Any?

    fun observeForever(observer: (T) -> Unit): Any?

    fun actionOccurred(item: T): Any?
}

open class ActionItem<T> : ActionInterface<T> {
    //LiveData that backs up our LiveDataAction
    val liveData = MutableLiveData<ResultsHolder<T>?>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {

        // Observe the internal MutableLiveData
        liveData.observe(owner, Observer<ResultsHolder<T>?> { t ->
            t?.let {
                observer(it.item)
                liveData.value = null
            }
        })
    }

    @MainThread
    override fun observe(compositeDisposable: CompositeDisposable, observer: (T) -> Unit) {
        // Observe the internal MutableLiveData

        val observe = Observable.create<ResultsHolder<T>?> {

            val observerItem = Observer<ResultsHolder<T>?> { t ->
                t?.let {
                    observer(it.item)
                    liveData.value = null
                }
            }

            it.setCancellable {
                liveData.removeObserver(observerItem)
            }

            liveData.observeForever(observerItem)
        }.subscribe({}, {}, {}, {})

        compositeDisposable.add(observe)

    }

    /**
     * This function allows easy testing without needing a LifecycleOwner.
     */
    @MainThread
    override fun observeForever(observer: (T) -> Unit) {
        // Observe the internal MutableLiveData
        liveData.observeForever({ t ->
            t?.let {
                observer(it.item)
                liveData.value = null
            }
        })
    }

    @MainThread
    override fun actionOccurred(item: T) {
        //set backing liveData to true
        //   if (Looper.myLooper() == Looper.getMainLooper())
        liveData.value = ResultsHolder(item)
    }
}

class Action : ActionItem<Boolean>() {
    @MainThread
    fun actionOccurred() {
        //set backing liveData to true
        liveData.value = ResultsHolder(true)
    }
}


open class ObserveActionItem<T> : ActionInterface<T> {
    //LiveData that backs up our LiveDataAction
    val liveData = Observe<ResultsHolder<T>?>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: (T) -> Unit): ObserveActionItem<T> {
        // Observe the internal MutableLiveData
        val compositeDisposable = CompositeDisposable()
        val lifeCycleObserver = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                compositeDisposable.clear()
            }
        }
        observe(compositeDisposable, observer)
        owner.lifecycle.addObserver(lifeCycleObserver)
        return this
    }

    @MainThread
    override fun observe(compositeDisposable: CompositeDisposable, observer: (T) -> Unit): ObserveActionItem<T> {
        // Observe the internal MutableLiveData

        liveData.onChanged(compositeDisposable) { t ->
            t?.let {
                observer(it.item)
                liveData set null
            }
        }
        return this
    }

    /**
     * This function allows easy testing without needing a LifecycleOwner.
     */
    @MainThread
    override fun observeForever(observer: (T) -> Unit): ObserveActionItem<T> {
        // Observe the internal MutableLiveData
        observe(CompositeDisposable(), observer)
        return this
    }

    @MainThread
    override fun actionOccurred(item: T) {
        //set backing liveData to true
        //   if (Looper.myLooper() == Looper.getMainLooper())
        liveData set ResultsHolder(item)
    }
}

class ObserveAction : ObserveActionItem<Boolean>() {
    @MainThread
    fun actionOccurred() {
        //set backing liveData to true
        liveData set ResultsHolder(true)
    }
}
