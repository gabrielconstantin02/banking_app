package com.example.banking_app.activity

import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.Account
import com.example.banking_app.models.Currency
import com.google.android.material.snackbar.Snackbar
import java.sql.*

class CreateAccountActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val currencyList: MutableList<Currency> = mutableListOf()

        getCurrenciesFromDatabase(currencyList)
        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarCreateAccount)
        toolbar.title = "Open account"
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val currencySpinner: Spinner = findViewById(R.id.currencySpinner)
        currencySpinner.onItemSelectedListener = this
        val spinnerAdapter: ArrayAdapter<Currency> = ArrayAdapter<Currency>(
            this,
            R.layout.spinner_item,
            currencyList
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        currencySpinner.adapter = spinnerAdapter

        val createAccountButton: Button = findViewById(R.id.openAccountButton)
        createAccountButton.setOnClickListener{ view -> onOpenAccount(view)}

        val monthsSpinner: Spinner = findViewById(R.id.monthsSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.months_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            monthsSpinner.adapter = adapter
        }

        val radioAccount: RadioButton = findViewById(R.id.radioAccount)
        val radioDeposit: RadioButton = findViewById(R.id.radioDeposit)
        radioDeposit.setOnClickListener { view ->
            setExtraVisibility("visible")
        }
        radioAccount.setOnClickListener { view ->
            setExtraVisibility("gone")
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCurrency: Currency = parent?.getItemAtPosition(position) as Currency
        Toast.makeText(parent.context, "Selected: $selectedCurrency", Toast.LENGTH_SHORT).show();
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun getCurrenciesFromDatabase(dataList: MutableList<Currency>) {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: Statement = connection.createStatement()
            val results: ResultSet = statement.executeQuery("select * from CURRENCY;")
            while (results.next()) {
                dataList.add(
                    Currency(
                        results.getInt("currency_id"),
                        results.getString("name")
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    private fun onOpenAccount(view: View?) {
        val spinnerView = findViewById<Spinner>(R.id.currencySpinner)
        val balanceView = findViewById<TextView>(R.id.balanceInput)
        val typeView = findViewById<RadioGroup>(R.id.accountType)
        if (spinnerView.selectedItem == null) {
            Toast.makeText(applicationContext, "No currency selected!", Toast.LENGTH_SHORT).show()
            return
        }
        val currency: Currency = spinnerView.selectedItem as Currency
        val balance: Double = balanceView.text.toString().toDouble()
        val type = findViewById<RadioButton>(typeView.checkedRadioButtonId).text.toString()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val connection: Connection = DatabaseConnection.getConnection()
//        val preparedStatement: PreparedStatement =
    }

    private fun setExtraVisibility(type: String) {
        var value : Int = 0
        val monthsLabel: TextView = findViewById(R.id.monthsLabel)
        val monthsSpinner: Spinner = findViewById(R.id.monthsSpinner)
        val renewalLabel: TextView = findViewById(R.id.renewalLabel)
        val renewalCheckbox: CheckBox = findViewById(R.id.renewalCheckbox)
        when (type) {
            "gone" -> value = View.GONE
            "visible" -> value = View.VISIBLE
            "invisible" -> value = View.INVISIBLE
        }
        monthsLabel.visibility = value
        monthsSpinner.visibility = value
        renewalLabel.visibility = value
        renewalCheckbox.visibility = value
    }
}