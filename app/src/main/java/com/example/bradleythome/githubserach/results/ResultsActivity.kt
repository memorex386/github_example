package com.example.bradleythome.githubserach.results

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.SearchView
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.BaseActivity
import com.example.bradleythome.githubserach.databinding.ResultsActivityBinding
import com.example.bradleythome.githubserach.results.fragment.BaseResultsFragment
import com.example.bradleythome.githubserach.results.sort.SortOrderViewModel
import kotlinx.android.synthetic.main.results_activity.*
import javax.inject.Inject


class ResultsActivity : BaseActivity(), BaseResultsFragment.ResultsFragmentInterface {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val resultsViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ResultsActivityViewModel::class.java) }
    private val sortViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SortOrderViewModel::class.java) }

    companion object {
        val SORT_RESULT = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("")

        val adapter = ResultsPagerAdapter(supportFragmentManager)

        resultsViewModel.adapter.set(adapter)

        val binding = DataBindingUtil.setContentView<ResultsActivityBinding>(this, R.layout.results_activity)
        binding.viewModel = resultsViewModel

        setSupportActionBar(toolbar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(string: String?): Boolean {
                resultsViewModel.querySearch.set(string)
                return false
            }

            override fun onQueryTextChange(string: String?): Boolean {
                if (string.isNullOrBlank()) resultsViewModel.querySearch.set("")
                return true
            }

        })

        with(binding) {

            val slide_down = AnimationUtils.loadAnimation(applicationContext,
                    R.anim.slide_down)

            imageview_logo.startAnimation(slide_down)

            tabLayout.setupWithViewPager(view_pager)

            resultsViewModel.sortAction.observe(this@ResultsActivity) {
                adapter.getCurrentFragment()?.viewModel?.let {
                    it.chooseSort(this@ResultsActivity, sortViewModel)
                }

            }

            resultsViewModel.orderAction.observe(this@ResultsActivity) {
                adapter.getCurrentFragment()?.viewModel?.let {
                    it.chooseOrder(this@ResultsActivity, sortViewModel)
                }

            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {

            }

    }

    override fun activityViewModel(): ResultsActivityViewModel = resultsViewModel


}
