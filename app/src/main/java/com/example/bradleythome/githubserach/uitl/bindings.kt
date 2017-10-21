package com.example.bradleythome.githubserach.uitl

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.os.Build
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.results.PageNavigationView
import com.example.bradleythome.githubserach.results.fragment.BaseResultsViewModel
import com.example.bradleythome.githubserach.search.Order
import com.example.bradleythome.githubserach.search.SearchViewContainer
import com.squareup.picasso.Picasso

/**
 * Created by bradley.thome on 10/18/17.
 */

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisible")
fun setViewModel(view: View, boolean: Boolean) {
    if (boolean) view.visibility = View.VISIBLE
    else view.visibility = View.INVISIBLE
}

@BindingAdapter("android:visibility")
fun setViewModel(view: View, string: String?) {
    if (string.isNullOrBlank()) view.visibility = View.GONE
    else view.visibility = View.VISIBLE
}

@BindingAdapter("viewModel")
fun setViewModel(pageNavigationView: PageNavigationView, baseResultsViewModel: BaseResultsViewModel<*>) {
    pageNavigationView.setViewModel(baseResultsViewModel)
}

@BindingAdapter("text")
fun setText(textView: TextView, int: Int?) {
    textView.setText(int.toString())
}

@BindingAdapter("url")
fun setUrl(webView: WebView, url: String?) {
    webView.settings.javaScriptEnabled = true
    webView.loadUrl(url)
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    Picasso.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("html")
fun setHtmlText(textView: TextView, htmlText: String?) {
    if (!htmlText.isNullOrBlank()) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT))
    } else {
        textView.setText(Html.fromHtml(htmlText))
    }
    else textView.setText(null)
}

@BindingAdapter("adapter")
fun loadImage(viewPager: ViewPager, fragmentStatePagerAdapter: FragmentStatePagerAdapter) {
    viewPager.adapter = fragmentStatePagerAdapter
}

@BindingAdapter("animations")
fun setAnimations(view: View, animation: Animation) {
    view.startAnimation(animation)
}

@BindingAdapter("viewPager")
fun setviewPager(tabLayout: TabLayout, viewPager: ViewPager) {
    tabLayout.setupWithViewPager(viewPager)
}

@BindingAdapter("adapter")
fun setAdapter(recyclerView: RecyclerView, searchViewContainer: SearchViewContainer<*>) {
    recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false) {
    }
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.adapter = searchViewContainer.recyclerAdapter.get()
}

@BindingAdapter("sortOptions")
fun setSortOptions(linearLayout: LinearLayout, baseResultsViewModel: BaseResultsViewModel<*>) {
    linearLayout.removeAllViews()
    val options = baseResultsViewModel.searchEnum.sortOptions
    val inflator = LayoutInflater.from(linearLayout.context)
    options.forEach() { item ->
        val textView = inflator.inflate(R.layout.sort_item, linearLayout, false) as TextView
        textView.text = item.value
        if (item.key == baseResultsViewModel.sort.get().first) textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(linearLayout.context, R.drawable.ic_check_black_24dp), null, null, null)
        textView.setOnClickListener {
            baseResultsViewModel.sort.set(Pair(item.key, item.value))
            baseResultsViewModel.closeDialog()
        }
        linearLayout.addView(textView)
    }
}

@BindingAdapter("orderOptions")
fun setOrderOptions(linearLayout: LinearLayout, baseResultsViewModel: BaseResultsViewModel<*>) {
    linearLayout.removeAllViews()
    val options = Order.values()
    val inflator = LayoutInflater.from(linearLayout.context)
    options.forEach { item ->
        val textView = inflator.inflate(R.layout.sort_item, linearLayout, false) as TextView
        textView.text = item.title
        if (item == baseResultsViewModel.order.get()) textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(linearLayout.context, R.drawable.ic_check_black_24dp), null, null, null)
        textView.setOnClickListener {
            baseResultsViewModel.order.set(item)
            baseResultsViewModel.closeDialog()
        }
        linearLayout.addView(textView)
    }
}
