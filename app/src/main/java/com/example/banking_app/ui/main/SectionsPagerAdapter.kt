package com.example.banking_app.ui.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.banking_app.*
import com.example.banking_app.fragments.OverviewFragment
import com.example.banking_app.fragments.PaymentsFragment
import com.example.banking_app.fragments.ProfileFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        0 -> OverviewFragment()
        1 -> PaymentsFragment()
        2 -> ProfileFragment()
        else -> error("Unknown")
    }

    override fun getPageTitle(position: Int) = context.resources.getString(TAB_TITLES[position])

    override fun getCount() = 3
}