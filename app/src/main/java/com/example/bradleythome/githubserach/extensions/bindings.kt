package com.example.bradleythome.githubserach.extensions

import android.databinding.BindingAdapter
import android.support.v7.widget.Toolbar

/**
 * Created by bradthome on 1/27/18.
 */
@BindingAdapter("default")
fun toolbarDefault(toolbar: Toolbar, set: Boolean) {
    val baseLifecycleActivity = toolbar.context as? BaseLifecycleActivity
    baseLifecycleActivity?.apply {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}