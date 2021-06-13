package com.example.banking_app.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.activity.AccountDetailsActivity
import com.example.banking_app.activity.CreateAccountActivity
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.Account
import com.example.banking_app.models.Currency
import java.sql.*


class OverviewFragment : Fragment() {
    // val getAccountsFromDatabase = object : Runnable {
    //     override fun run() {
    //         val connection: Connection = DatabaseConnection.getConnection();
    //         val stmt: Statement = connection.createStatement();
    //     }
    // }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_overview, container, false)
        activity?.title = getString(R.string.nav_overview)
        val accountsLayout : LinearLayout = view.findViewById(R.id.overviewHolder)
        val accountData : MutableList<Pair<Account, Currency>> = mutableListOf()

        getAccountsFromDatabase(accountData)
        println(accountData.size)
        addAccounts(inflater, accountsLayout, accountData)

        // Open account button
        val accountButton: Button = view.findViewById(R.id.openAccount)
        accountButton.setOnClickListener { view -> onCreateAccount(view) }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun addAccounts(inflater: LayoutInflater, location: LinearLayout, dataAccounts: List<Pair<Account, Currency>>) {
        for (data: Pair<Account, Currency> in dataAccounts) {
            val hold: View = inflater.inflate(R.layout.log_accounts, location, false)
            val account: Account = data.first
            val currency: Currency = data.second

            val ibanText: TextView = hold.findViewById(R.id.accountLogIban)
            ibanText.text = account.iban
            val currencyText: TextView = hold.findViewById(R.id.accountLogCurrency)
            currencyText.text = currency.name
            val balanceText: TextView = hold.findViewById(R.id.accountLogBalance)
            balanceText.text = account.balance.toString() + " " + currency.name

            hold.setOnClickListener {
                val accountDetailsIntent = Intent(context, AccountDetailsActivity::class.java)

                val bundle = Bundle()
                bundle.putString("iban", account.iban)
                accountDetailsIntent.putExtras(bundle)

                startActivity(accountDetailsIntent)
            }

            location.addView(hold)
        }
    }

    private fun getAccountsFromDatabase(dataList: MutableList<Pair<Account, Currency>>) {
        try {
            val sql = "select * from ACCOUNT a, USER u, CURRENCY c where a.user_id = u.user_id and c.currency_id = a.currency_id and u.user_id = ?";
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setInt(1, MApplication.currentUser?.userId!!)
            val results: ResultSet = statement.executeQuery()

            while (results.next()) {
                dataList.add(Pair(
                    Account(
                        results.getString("iban"),
                        results.getInt("user_id"),
                        results.getInt("currency_id"),
                        results.getString("bic"),
                        results.getDouble("balance")
                    ),
                    Currency(
                        results.getInt("currency_id"),
                        results.getString("name")
                    )
                ))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    fun onCreateAccount(view: View?) {
        startActivity(Intent(context, CreateAccountActivity::class.java))
    }

    private fun getMockData(dataList: MutableList<Pair<Account, Currency>>) {
        dataList.clear()
        dataList.add(Pair(
            Account(
                "12312AA2313Siban",
                1,
                1,
                "BIC",
                5000.0
            ),
            Currency(
                1,
                "RON"
            )
        ))
        dataList.add(Pair(
            Account(
                "23124124214IBAN",
                1,
                1,
                "BIC",
                200.0
            ),
            Currency(
                2,
                "EUR"
            )
        ))
        dataList.add(Pair(
            Account(
                "15436645IBANmonkaW",
                1,
                1,
                "BIC",
                3333.0
            ),
            Currency(
                3,
                "USD"
            )
        ))
    }

}
