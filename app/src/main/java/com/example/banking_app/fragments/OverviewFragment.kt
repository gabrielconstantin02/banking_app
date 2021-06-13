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
import com.example.banking_app.models.Deposit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.*


class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_overview, container, false)
        activity?.title = getString(R.string.nav_overview)
        val accountsLayout : LinearLayout = view.findViewById(R.id.overviewHolder)

        // Getting accounts
        val accountData : MutableList<Account> = mutableListOf()

        getAccountsFromDatabase(accountData)
        if (accountData.size > 0) {
            addAccounts(inflater, accountsLayout, accountData)
        }

        // Getting deposits
        val depositData : MutableList<Deposit> = mutableListOf()

        getDepositsFromDatabase(depositData)
        if (depositData.size > 0) {
            addDeposits(inflater, accountsLayout, depositData)
        }

        // Open account button
        val accountButton: FloatingActionButton = view.findViewById(R.id.openAccount)
        accountButton.setOnClickListener { viewParam -> onCreateAccount(viewParam) }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun addAccounts(inflater: LayoutInflater, location: LinearLayout, dataAccounts: List<Account>) {
        val holder: View = inflater.inflate(R.layout.logs_holder, location, false)
        holder.findViewById<TextView>(R.id.holderTitle).text = "Accounts"
        val holderData: LinearLayout = holder.findViewById(R.id.logDataHolder)
        for (account: Account in dataAccounts) {
            val hold: View = inflater.inflate(R.layout.log_accounts, holderData, false)
            val currency: Currency? = account.currency

            val ibanText: TextView = hold.findViewById(R.id.accountLogIban)
            ibanText.text = account.iban
            val currencyText: TextView = hold.findViewById(R.id.accountLogCurrency)
            currencyText.text = currency?.name ?: ""
            val balanceText: TextView = hold.findViewById(R.id.accountLogBalance)
            balanceText.text = account.balance.toString() + " " + currency?.name ?: ""

            hold.setOnClickListener {
                val accountDetailsIntent = Intent(context, AccountDetailsActivity::class.java)

                val bundle = Bundle()
                bundle.putString("iban", account.iban)
                bundle.putString("type", "Account")
                accountDetailsIntent.putExtras(bundle)

                startActivity(accountDetailsIntent)
            }
            holderData.addView(hold)
        }
        location.addView(holder)
    }

    @SuppressLint("SetTextI18n")
    private fun addDeposits(inflater: LayoutInflater, location: LinearLayout, dataDeposit: List<Deposit>) {
        val holder: View = inflater.inflate(R.layout.logs_holder, location, false)
        holder.findViewById<TextView>(R.id.holderTitle).text = "Deposits"
        val holderData: LinearLayout = holder.findViewById(R.id.logDataHolder)
        for (deposit: Deposit in dataDeposit) {
            val hold: View = inflater.inflate(R.layout.log_accounts, holderData, false)
            val account = deposit.account
            val currency: Currency? = account?.currency

            val ibanText: TextView = hold.findViewById(R.id.accountLogIban)
            ibanText.text = account?.iban ?: ""
            val currencyText: TextView = hold.findViewById(R.id.accountLogCurrency)
            currencyText.text = currency?.name ?: ""
            val balanceText: TextView = hold.findViewById(R.id.accountLogBalance)
            balanceText.text = account?.balance.toString() + " " + currency?.name ?: ""

            hold.setOnClickListener {
                val accountDetailsIntent = Intent(context, AccountDetailsActivity::class.java)

                val bundle = Bundle()
                bundle.putString("iban", account?.iban)
                bundle.putString("type", "Deposit")
                accountDetailsIntent.putExtras(bundle)

                startActivity(accountDetailsIntent)
            }
            holderData.addView(hold)
        }
        location.addView(holder)
    }

    private fun getAccountsFromDatabase(dataList: MutableList<Account>) {
        try {
            val sql = "select * from ACCOUNT a, USER u, CURRENCY c where a.user_id = u.user_id and c.currency_id = a.currency_id and u.user_id = ? and " +
                    "a.iban not in (select iban from DEPOSIT);";
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setInt(1, MApplication.currentUser?.userId!!)
            val results: ResultSet = statement.executeQuery()

            while (results.next()) {
                dataList.add(
                    Account(
                        results.getString("iban"),
                        results.getInt("user_id"),
                        results.getInt("currency_id"),
                        results.getString("bic"),
                        results.getDouble("balance"),
                        Currency(
                            results.getInt("currency_id"),
                            results.getString("name")
                        )
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    private fun getDepositsFromDatabase(dataList: MutableList<Deposit>) {
        try {
            val sql = "select * from ACCOUNT a, USER u, CURRENCY c, DEPOSIT d where a.iban = d.iban and a.user_id = u.user_id and c.currency_id = a.currency_id and u.user_id = ?;"
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setInt(1, MApplication.currentUser?.userId!!)
            val results: ResultSet = statement.executeQuery()

            while (results.next()) {
                dataList.add(
                    Deposit(
                        results.getString("iban"),
                        results.getInt("nr_months"),
                        results.getDate("due_date"),
                        results.getBoolean("renewal"),
                        Account(
                            results.getString("iban"),
                            results.getInt("user_id"),
                            results.getInt("currency_id"),
                            results.getString("bic"),
                            results.getDouble("balance"),
                            Currency(
                                results.getInt("currency_id"),
                                results.getString("name")
                            )
                        )
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    fun onCreateAccount(view: View?) {
        startActivity(Intent(context, CreateAccountActivity::class.java))
    }

    private fun getMockData(dataList: MutableList<Account>) {
        dataList.clear()
        dataList.add(
            Account(
                "12312AA2313Siban",
                1,
                1,
                "BIC",
                5000.0,
                Currency(
                    1,
                    "RON"
                )
            )
        )
        dataList.add(
            Account(
                "23124124214IBAN",
                1,
                1,
                "BIC",
                200.0,
                Currency(
                    2,
                    "EUR"
                )
            )
        )
        dataList.add(
            Account(
                "15436645IBANmonkaW",
                1,
                1,
                "BIC",
                3333.0,
                Currency(
                    3,
                    "USD"
                )
            )
        )
    }

}
