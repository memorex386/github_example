package com.example.bradleythome.githubserach.results

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.base.BaseActivity
import com.example.bradleythome.githubserach.databinding.ResultsActivityBinding
import com.example.bradleythome.githubserach.results.fragment.BaseResultsFragment
import com.example.bradleythome.githubserach.results.sort.SortOrderViewModel
import kotlinx.android.synthetic.main.results_activity.*


class ResultsActivity : BaseActivity<ResultsActivityViewModel, ResultsActivityBinding>(), BaseResultsFragment.ResultsFragmentInterface {


    private val sortViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SortOrderViewModel::class.java) }

    companion object {
        val SORT_RESULT = 1234
    }

    override val layoutRes: Int
        get() = R.layout.results_activity

    override fun preDataBinded(savedInstanceState: Bundle?) {
        super.preDataBinded(savedInstanceState)


        val adapter = ResultsPagerAdapter(supportFragmentManager)

        viewModel.adapter.set(adapter)
    }

    override fun onCreateDataBinded(savedInstanceState: Bundle?) {
        setTitle("")



        setSupportActionBar(toolbar)


        viewModel.sortAction.observe(this@ResultsActivity) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {

            }

    }

    override fun activityViewModel(): ResultsActivityViewModel = resultsViewModel


}
