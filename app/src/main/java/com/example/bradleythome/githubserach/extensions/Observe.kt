package com.example.bradleythome.githubserach.extensions

import android.arch.lifecycle.LifecycleOwner
import android.databinding.BaseObservable
import android.databinding.Observable
import android.databinding.ObservableField
import com.example.bradleythome.githubserach.uitl.ObserveActionItem
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import kotlin.reflect.KProperty

/**
 * Custom ObservableField that gives more flexibility about getting and setting values and attaching the observable to a parent
 *
 * Created by bradthome on 11/17/17.
 */

open class Observe<T>(value: T? = null) : BaseObserve<T>(value) {
    val propertyChanged = ObserveActionItem<PropertyChanged>()

    val finalUpdate = ObserveActionItem<T>()

    /**
     * Set Custom Setter and return this
     */
    override fun preSetter(setter: ((T) -> T)?) = super.preSetter(setter) as Observe<T>

    override fun customGetter(getter: ((T) -> T)?) = super.customGetter(getter) as Observe<T>

    /**
     * Set Custom Getter and return this
     */
    override fun preGetter(getter: ((T) -> T)?) = super.preGetter(getter) as Observe<T>

    override fun configParent(parent: BaseObservable?) = super.configParent(parent) as Observe<T>

    override fun acceptChange(acceptChangeItem: (currentItem: T, newItem: T) -> Boolean) = super.acceptChange(acceptChangeItem) as Observe<T>

    override val allowAllChanges: Observe<T>
        get() = super.allowAllChanges as Observe<T>

    override fun syncWithObserve(observe: Observe<T>) = super.syncWithObserve(observe) as Observe<T>

    override fun addOnPropertyChangedCallback(action: (T) -> Unit) = super.addOnPropertyChangedCallback(action) as Observe<T>


    override fun onChanged(compositeDisposable: CompositeDisposable, action: (T) -> Unit) = super.onChanged(compositeDisposable, action) as Observe<T>

    override fun updateFrom(observe: Observe<T>) = super.updateFrom(observe) as Observe<T>

    override fun propertyChanged(newItem: T, oldItem: T) {
        propertyChanged.actionOccurred(PropertyChanged(item, oldItem))
    }

    override fun finalUpdate() {
        finalUpdate.actionOccurred(get())
    }


    fun onFinalUpdate(compositeDisposableInterface: CompositeDisposableInterface, action: (T) -> Unit): Observe<T> {
        finalUpdate.observe(compositeDisposableInterface.compositeDisposable, action)
        return this
    }

    fun onFinalUpdate(lifecycleOwner: LifecycleOwner, action: (T) -> Unit): Observe<T> {
        finalUpdate.observe(lifecycleOwner, action)
        return this
    }

    fun onFinalUpdate(action: (T) -> Unit): Observe<T> {
        finalUpdate.observeForever(action)
        return this
    }

    /**
     * Set Custom Post Setter and return this
     */
    fun onPropertyChanged(compositeDisposable: CompositeDisposable, propertyChanged: ((PropertyChanged) -> Unit)): Observe<T> {
        this.propertyChanged.observe(compositeDisposable, propertyChanged)
        return this
    }
}

open class BaseObserve<T>(value: T? = null) : ObservableField<T>(value) {

    /**
     * notify a parent Observable when changes are made to this Observable
     */
    var parentObserve: BaseObservable? = null

    var kProp: KProperty<*>? = null

    /**
     * Easy access to get() and set()
     */
    var item: T
        get() = this.get()
        set(value) = this.set(value)

    var acceptChange: (currentItem: T, newItem: T) -> Boolean = { currentItem, newItem -> currentItem != newItem }

    /**
     * Gives flexibility to define logic that is applied before the actually setter is ran
     */
    var customSetter: ((T) -> T)? = null

    /**
     * Gives flexibility to define logic that is applied before the actually setter is ran
     */

    /**
     * Gives flexibility to define logic that is applied before the actually getter is ran
     */
    var customGetter: ((T) -> T)? = null


    fun set(value: T, forceUpdate: Boolean = false): Boolean {
        val oldItem = get()
        var item = value
        customSetter?.let {
            item = it(value)
        }
        if (!forceUpdate && !acceptChange(oldItem, item)) return false
        var changed = false
        val changedListener = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                changed = true
            }
        }
        super.addOnPropertyChangedCallback(changedListener)
        super.set(item)
        super.removeOnPropertyChangedCallback(changedListener)
        if (!changed) notifyChange()
        propertyChanged(item, oldItem)
        kProp?.let {
            Timber.d("[${it.name}] value changed to $item from $oldItem")
        }
        parentObserve?.apply {
            this.notifyChange()
        }
        finalUpdate()
        return true
    }

    infix fun forceUpdate(value: T) {
        set(value, true)
    }

    override infix fun set(value: T) {
        set(value, false)
    }

    override fun get(): T =
            customGetter?.let { it(super.get()) } ?: super.get()

    protected open fun propertyChanged(newItem: T, oldItem: T) {

    }

    protected open fun finalUpdate() {

    }

    /**
     * Set Custom Setter and return this
     */
    open fun preSetter(setter: ((T) -> T)?): BaseObserve<T> {
        customSetter = setter
        return this
    }

    open fun customGetter(getter: ((T) -> T)?): BaseObserve<T> {
        customGetter = getter
        return this
    }


    /**
     * Set Custom Getter and return this
     */
    open fun preGetter(getter: ((T) -> T)?): BaseObserve<T> {
        customGetter = getter
        return this
    }

    open fun configParent(parent: BaseObservable? = null): BaseObserve<T> {
        parentObserve = parent
        return this
    }

    open fun acceptChange(acceptChangeItem: (currentItem: T, newItem: T) -> Boolean): BaseObserve<T> {
        this.acceptChange = acceptChangeItem
        return this
    }

    open val allowAllChanges: BaseObserve<T>
        get() {
            this.acceptChange = { itemO, itemd -> true }
            return this
        }

    open fun syncWithObserve(observe: Observe<T>): BaseObserve<T> {
        this.addOnPropertyChangedCallback { if (observe.item != it) observe.set(it) }
        observe.addOnPropertyChangedCallback { if (observe.item != it) this.set(it) }
        return this
    }

    open fun addOnPropertyChangedCallback(action: (T) -> Unit): BaseObserve<T> {
        this.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
                action(this@BaseObserve.get())
            }
        })
        return this
    }


    open fun onChanged(compositeDisposable: CompositeDisposable, action: (T) -> Unit): BaseObserve<T> {
        compositeDisposable.subscribe(this, action)
        return this
    }

    open fun updateFrom(observe: Observe<T>): BaseObserve<T> {
        observe.onChanged {
            this set it
        }
        return this
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        kProp = property
        return get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        kProp = property
        set(value)
    }


    open fun <SETTER, GETTER> SETTER.formattedObserve(formattedGetter: (SETTER) -> GETTER, formattedSetter: (GETTER) -> SETTER): BaseObserve<SETTER> {
        return FormattedObserve<SETTER, GETTER>(this, formattedGetter, formattedSetter).configParent(this@BaseObserve)
    }


    val <T> T.baseObserve: BaseObserve<T>
        get() = BaseObserve(this).configParent(this@BaseObserve)

    override fun toString(): String {
        return get().toString()
    }

    val string
        get() = toString()

    inner class PropertyChanged(val newItem: T, val oldItem: T)
}

fun <T> Observe<T>.onChanged(action: (T) -> Unit): Observe<T> {
    this.addOnPropertyChangedCallback(action)
    return this
}

/**
 * Extends Observe and gives the flexibility to format the set and get of another type for this Observable
 *
 * Created by bradthome on 11/17/17.
 */
open class FormattedObserve<SETTER : Any?, GETTER : Any?>(value: SETTER? = null, private var formattedGetter: (SETTER) -> GETTER, private var formattedSetter: (GETTER) -> SETTER) : Observe<SETTER>(value) {
    var formatted: GETTER
        get() = formattedGetter(get())
        set(value) = set(formattedSetter(value))

    override fun toString(): String {
        return formatted.toString()
    }

}