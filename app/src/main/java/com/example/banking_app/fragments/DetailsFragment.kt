package com.example.banking_app.fragments

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DetailsFragment(account: Account, type: String): Fragment() {
    private val mType = type
    private val mAccount = account
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_details, container, false)
        val user: User = MApplication.currentUser!!
        showAccount(view, mAccount, user)

        if (mType == "Deposit") {
            val mDeposit = getDeposit(mAccount)!!
            showDeposit(view, mDeposit)
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private fun showAccount(view: View, account: Account, user: User) {
        val ibanText: TextView = view.findViewById(R.id.detailsIban)
        ibanText.text = account.iban
        val ownerNameText: TextView = view.findViewById(R.id.detailsOwnerName)
        ownerNameText.text = "${user.firstName} ${user.lastName}"
        val ownerEmailText: TextView = view.findViewById(R.id.detailsOwnerEmail)
        ownerEmailText.text = user.email
        val currencyText: TextView = view.findViewById(R.id.detailsCurrency)
        currencyText.text = account.currency?.name ?: ""
        val bicText: TextView = view.findViewById(R.id.detailsBIC)
        bicText.text = account.bic
        val balanceText: TextView = view.findViewById(R.id.detailsBalance)
        balanceText.text = account.balance.toString() + " " + account.currency?.name
    }

    @SuppressLint("SetTextI18n")
    private fun showDeposit(view: View, deposit: Deposit) {
        val depositGrid = view.findViewById<GridLayout>(R.id.depositGrid)
        depositGrid.visibility = View.VISIBLE
        val nrMonthsText: TextView = view.findViewById(R.id.nrMonthsDetails)
        nrMonthsText.text = deposit.depositType?.nr_months.toString()
        val interestRateText: TextView = view.findViewById(R.id.interestRateDetails)
        interestRateText.text = deposit.depositType?.interest_rate.toString() + "%"
        val dueDateText: TextView = view.findViewById(R.id.dueDateDetails)
        dueDateText.text = deposit.due_date.toString()
        val renewalText: TextView = view.findViewById(R.id.renewalDetails)
        renewalText.text = deposit.renewal.toString()
    }

    private fun getDeposit(account: Account): Deposit? {
        try {
            val sql = "select * from DEPOSIT d, DEPOSIT_TYPE dt where d.nr_months = dt.nr_months and d.iban = ?"
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setString(1, account.iban)
            val results: ResultSet = statement.executeQuery()

            if (results.next()) {
                return Deposit(
                    results.getString("iban"),
                    results.getInt("nr_months"),
                    results.getDate("due_date"),
                    results.getBoolean("renewal"),
                    account,
                    DepositType(
                        results.getInt("nr_months"),
                        results.getDouble("interest_rate")
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return null
    }

}