package com.example.bradleythome.githubserach.results

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.example.bradleythome.githubserach.results.fragment.BaseResultsFragment
import com.example.bradleythome.githubserach.search.SearchEnum
import com.example.bradleythome.githubserach.search.fragment


/**
 * Created by bradley.thome on 10/18/17.
 */

class ResultsPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var mCurrentFragment: BaseResultsFragment<*>? = null

    fun getCurrentFragment(): BaseResultsFragment<*>? {
        return mCurrentFragment
    }

    val searchEnumArray = SearchEnum.values()

    override fun getPageTitle(position: Int): CharSequence {
        return searchEnumArray[position].title
    }

    override fun getItem(position: Int): Fragment = (searchEnumArray[position]).fragment()

    override fun getCount(): Int {
        return searchEnumArray.size
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any) {
        if (getCurrentFragment() !== `object`) {
            mCurrentFragment = `object` as BaseResultsFragment<*>
        }
        super.setPrimaryItem(container, position, `object`)
    }

}
