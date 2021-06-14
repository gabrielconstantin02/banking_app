package com.example.banking_app.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.banking_app.*
import com.example.banking_app.fragments.*
import com.example.banking_app.models.Account
import java.util.*

private val TAB_TITLES_ACCOUNT = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

private val TAB_TITLES_DEPOSIT = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_3
)

class AccountsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    account: Account,
    type: String,
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mAccount = account
    private val mType = type

    override fun getItem(position: Int): Fragment {
        if (mType == "Deposit")  {
            return when (position) {
                0 -> TransactionsFragment(mAccount.iban, mAccount.currency!!)
                1 -> DetailsFragment(mAccount, mType)
                else -> error("Unknown")
            }
        }
        return when (position) {
            0 -> TransactionsFragment(mAccount.iban, mAccount.currency!!)
            1 -> CardsFragment()
            2 -> DetailsFragment(mAccount, mType)
            else -> error("Unknown")
        }

    }

    override fun getPageTitle(position: Int): String {
        if (mType == "Deposit") {
            return context.resources.getString(TAB_TITLES_DEPOSIT[position])
        }

        return context.resources.getString(TAB_TITLES_ACCOUNT[position])
    }

    override fun getCount(): Int {
        if (mType == "Deposit") {
            return 2
        }
        return 3
    }
}