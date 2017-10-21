package com.example.bradleythome.githubserach.results

import android.content.Context
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.databinding.PageNavigationViewBinding
import com.example.bradleythome.githubserach.results.fragment.BaseResultsViewModel

/**
 * Created by bradley.thome on 10/19/17.
 *
 * a custom view for page navigation, although it implements the base results
 * view model associated with each fragment to handle life cycle aware data
 */
class PageNavigationView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val binding = DataBindingUtil.inflate<PageNavigationViewBinding>(LayoutInflater.from(context), R.layout.page_navigation_view, this, true)

    fun setViewModel(baseResultsViewModel: BaseResultsViewModel<*>) {
        binding.viewModel = baseResultsViewModel
    }
}
