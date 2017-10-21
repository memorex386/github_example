package com.example.bradleythome.githubserach.results

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bradleythome.githubserach.models.ResultsItem
import com.example.bradleythome.githubserach.search.SearchViewContainer

/**
 * Created by bradley.thome on 10/17/17.
 *
 * A lifecycle aware adapter
 */

class ResultsAdapter<T : ResultsItem>(val searchViewContainer: SearchViewContainer<T>) : RecyclerView.Adapter<ResultsItemViewHolder<T>>(), LifecycleOwner {

    internal var mLifecycleRegistry = LifecycleRegistry(this)

    protected lateinit var recyclerView: RecyclerView

    protected var results: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun swapData(resultsLocal: List<T>) {
        results = resultsLocal
        notifyDataSetChanged()
    }

    override fun getItemCount() = results?.size ?: 0

    override fun onBindViewHolder(holder: ResultsItemViewHolder<T>?, position: Int) {
        results?.let { items ->
            holder?.let {
                it.bind(items.get(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultsItemViewHolder<T>? {
        return parent?.let { view ->
            results?.let {
                return searchViewContainer.resultsItemViewHolder(parent, it.get(viewType))
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView?.let { this.recyclerView = recyclerView }
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    override fun getItemViewType(position: Int): Int = position
}

class ResultsItemViewHolder<T : ResultsItem> private constructor(itemView: View, val binding: ViewDataBinding, val bindUnit: (item: T) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bind(resultsItem: T) {
        bindUnit(resultsItem)
        binding.executePendingBindings()
    }

    companion object {
        fun <R : ResultsItem, V : ViewDataBinding> config(clazz: Class<V>, @LayoutRes layoutRes: Int, parent: ViewGroup?, resultsItem: R, bind: (V, R) -> Unit): ResultsItemViewHolder<R> {
            val inflater = LayoutInflater.from(parent?.context)
            val binding = DataBindingUtil.inflate<V>(inflater, layoutRes, parent, false)
            return ResultsItemViewHolder(binding.root, binding) { item ->
                bind(binding, item)
            }
        }
    }
}




