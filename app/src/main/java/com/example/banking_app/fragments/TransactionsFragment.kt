package com.example.banking_app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.adapters.TransactionAdapter
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.Account
import com.example.banking_app.models.Currency
import com.example.banking_app.models.Transaction
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class TransactionsFragment(iban: String, currency: Currency): Fragment() {
    private val mIban: String = iban
    private val mCurrency: Currency = currency
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_transactions, container, false);

        // Getting transactions
        val transactionData: MutableList<Transaction> = mutableListOf()
        getAllTransactions(mIban, transactionData)

        val transactionRecyclerView: RecyclerView = view.findViewById(R.id.transactionRecyclerView)
        val adapterTransaction: TransactionAdapter = TransactionAdapter(transactionData, activity?.applicationContext!!, mIban, mCurrency)
        transactionRecyclerView.setHasFixedSize(true)
        transactionRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        transactionRecyclerView.adapter = adapterTransaction

        return view;
    }

    fun getAllTransactions(iban: String, dataList: MutableList<Transaction>) {
        try {
            val sql = "select * from TRANSACTION where sender_id = ? or receiver_id = ?"
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setString(1, iban)
            statement.setString(2, iban)
            val results: ResultSet = statement.executeQuery()

            while (results.next()) {
                dataList.add(
                    Transaction(
                        results.getInt("transaction_id"),
                        results.getString("sender_id"),
                        results.getString("receiver_id"),
                        results.getDate("date"),
                        results.getDouble("amount")
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }
}