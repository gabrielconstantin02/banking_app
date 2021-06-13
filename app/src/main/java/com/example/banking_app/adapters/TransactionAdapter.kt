package com.example.banking_app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banking_app.R
import com.example.banking_app.models.Currency
import com.example.banking_app.models.Transaction
import kotlinx.android.synthetic.main.log_transaction.view.*

class TransactionAdapter(private val dataSource: MutableList<Transaction>,
                         val context: Context, private val currentIban: String , private val currency: Currency
                          ) : RecyclerView.Adapter<TransactionViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.log_transaction, parent, false))
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransactionData = dataSource[position]

        holder.mTransactionIban.text = if (currentIban == currentTransactionData.receiver_id) currentTransactionData.sender_id else currentTransactionData.receiver_id
        holder.mDate.text = currentTransactionData.date.toString()
        holder.mAmount.text = (if (currentIban == currentTransactionData.receiver_id) "+ " else "- ") +  currentTransactionData.amount.toString() + " " + currency.name
    }
}

class TransactionViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    val mTransactionIban: TextView = view.transactionIban
    val mDate: TextView = view.transactionDate
    val mAmount: TextView = view.transactionAmount
}