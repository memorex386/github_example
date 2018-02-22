package com.example.bradleythome.githubserach.extensions

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.bradleythome.githubserach.core.app
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

object CustomExtensionsObject {}

enum class JSONDefaultEnum(val default: String) {
    JSON_OBJECT(JSONDefaults.OBJECT),
    JSON_ARRAY(JSONDefaults.ARRAY)

}


val Any.toJsonString
    get() = toJson(JSONDefaultEnum.JSON_OBJECT)

/**
 * Convert Any object to a Json Sring, return null if not possible
 */
fun Any.toJson(default: String? = null): String? = this.let {
    app.moshi.adapter(it.getClass()).toJson(it)?.let {
        it
    } ?: default
} ?: default

/**
 * Convert Any object to a Json Sring, return null if not possible
 */
fun Any.toJson(default: JSONDefaultEnum): String = this.let {
    App.instance.moshi.adapter(it.getClass()).toJson(it)?.let {
        it
    } ?: default.default
}

/**
 * Attempt Json String to Class, return null if fails
 * @param jsonString
 */
fun <T : Any> Class<T>.fromJson(jsonString: String?): T? = jsonString?.let {
    App.instance.moshi.adapter(this).fromJson(jsonString)
}


/**
 * Attempt Json String to Class, return null if fails
 * @param clazzType

fun <T : Any> String?.fromJson(clazzType: Class<T>): T? = this?.let {
App.instance.moshi.adapter(clazzType).fromJson(it)
}

inline fun <reified T> String?.toObject() = this?.let { App.instance.moshi.adapter(T::class.java).fromJson(it) }
 */
/**
 * Use Moshi to convert json string to object.  Try/Catch catches any failuers and a default value is declared
 * @param clazzType - the class type to use
 * @param default - default value to use
 * @return the object
 */
inline fun <reified T> String?.toSafeObject(clazzType: Class<T> = T::class.java, default: () -> T): T = this?.let {
    try {
        "toSafeObject attempt for $clazzType = $this".timber.d
        return@let App.instance.moshi.adapter(clazzType).fromJson(it) ?: default()
    } catch (e: Exception) {
        e.timber.d("[toSafeObject failed for $clazzType]")
    }
    return default()
} ?: default()

inline fun <reified T> String?.toSafeObjectElseNull(clazzType: Class<T> = T::class.java): T? = toSafeObject<T?> { null }

val Boolean.isFalse
    get() = !this


/**
 * Attempt Json String to Class, return null if fails
 * @param jsonString
 */
inline fun <reified T : Any> Class<T>.fromJsonArray(jsonString: String?): List<T>? = jsonString?.let {
    val type = Types.newParameterizedType(List::class.java, this)
    val adapter: JsonAdapter<List<T>> = App.instance.moshi.adapter(type)
    adapter.fromJson(jsonString)
}

/**
 * Attempt Json String to Class, return null if fails
 * @param clazzType
 */
inline fun <reified T : Any> String?.fromJsonArray(clazzType: Class<T> = T::class.java): List<T>? = this?.let {
    val type = Types.newParameterizedType(List::class.java, clazzType)
    val adapter: JsonAdapter<List<T>> = App.instance.moshi.adapter(type)
    adapter.fromJson(this)
}

fun <T : BaseViewModel> View.getViewModel(clazzType: Class<T>) = ViewModelProviders.of(context as BaseLifecycleActivity).get(clazzType)

val Int.color
    get() = ContextCompat.getColor(App.instance, this)

val Int.string
    get() = app.getString(this)

inline fun <reified T> Intent.getMoshi(name: String = T::class.java.canonicalName, clazz: Class<T> = T::class.java) = getStringExtra(name).toSafeObjectElseNull(clazz)

inline fun <reified T> Intent.putMoshi(item: T, name: String = T::class.java.canonicalName) = putExtra(name, item?.toJson())


val Network.wifi
    get() = Network.getWifi(app)