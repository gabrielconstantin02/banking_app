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
import com.example.banking_app.models.Currency

class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_overview, container, false);
        activity?.title = getString(R.string.nav_overview);
        val accountsLayout : LinearLayout = view.findViewById(R.id.overviewHolder);

        val mockData : MutableList<Pair<Account, Currency>> = mutableListOf();
        getMockData(mockData);

        addAccounts(inflater, accountsLayout, mockData);

        return view;
    }

    private fun getMockData(dataList: MutableList<Pair<Account, Currency>>) {
        dataList.clear();
        dataList.add(Pair(
                Account(
                        "12312AA2313Siban",
                        1,
                        1,
                        "BIC",
                        5000
                ),
                Currency(
                        1,
                        "RON"
                )
        ));
        dataList.add(Pair(
                Account(
                        "23124124214IBAN",
                        1,
                        1,
                        "BIC",
                        200
                ),
                Currency(
                        2,
                        "EUR"
                )
        ));
        dataList.add(Pair(
                Account(
                        "15436645IBANmonkaW",
                        1,
                        1,
                        "BIC",
                        3333
                ),
                Currency(
                        3,
                        "USD"
                )
        ));
    }

    @SuppressLint("SetTextI18n")
    private fun addAccounts(inflater: LayoutInflater, location: LinearLayout, dataAccounts: List<Pair<Account, Currency>>) {
        for (data: Pair<Account, Currency> in dataAccounts) {
            val hold: View = inflater.inflate(R.layout.log_accounts, location, false);
            val account: Account = data.first;
            val currency: Currency = data.second;

            val ibanText: TextView = hold.findViewById(R.id.accountLogIban);
            ibanText.text = account.getIban();
            val currencyText: TextView = hold.findViewById(R.id.accountLogCurrency);
            currencyText.text = currency.getName();
            val balanceText: TextView = hold.findViewById(R.id.accountLogBalance);
            balanceText.text = account.getBalance().toString() + " " + currency.getName();

            location.addView(hold);
        }
    }

    private fun addCards(inflater: LayoutInflater, location: LinearLayout, dataAccounts: List<Pair<Account, Currency>>) {

    }

    // public class getAccountsFromDatabase: Runnable {
    //     public void run() {

    //     }
    // }

}
