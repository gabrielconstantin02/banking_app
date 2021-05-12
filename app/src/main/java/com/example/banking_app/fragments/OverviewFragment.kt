package com.example.banking_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.banking_app.R
import com.example.banking_app.models.Account

class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_overview, container, false);
        activity?.title = getString(R.string.nav_overview);
        return view;
    }


    @SuppressLint("SetTextI18n")
    private fun addAccounts(inflater: LayoutInflater, location: LinearLayout, dataAccounts: List<Account>) {
        for (account: Account in dataAccounts) {
            val hold: View = inflater.inflate(R.layout.log_accounts, location, false);

            val ibanText: TextView = hold.findViewById(R.id.ibanHolder);
            ibanText.text = account.getIban();
            val balanceText: TextView = hold.findViewById(R.id.ibanHolder);
            balanceText.text = account.getBalance().toString() + '$';
        }
    }

    private fun loadAccounts() {

    }

}
