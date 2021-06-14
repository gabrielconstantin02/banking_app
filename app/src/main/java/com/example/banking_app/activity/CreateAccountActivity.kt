package com.example.banking_app.activity

import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.Currency
import com.example.banking_app.models.DepositType
import java.sql.*
import java.time.LocalDate
import java.util.*

class CreateAccountActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarCreateAccount)
        toolbar.title = "Open account"
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Getting the currency for the currency Spinner
        val currencyList: MutableList<Currency> = mutableListOf()

        getCurrenciesFromDatabase(currencyList)
        val currencySpinner: Spinner = findViewById(R.id.currencySpinner)
        currencySpinner.onItemSelectedListener = this
        val spinnerAdapter: ArrayAdapter<Currency> = ArrayAdapter<Currency>(
            this,
            R.layout.spinner_item,
            currencyList
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        currencySpinner.adapter = spinnerAdapter

        // Getting the Deposit Types for the months Spinner
        val depositTypeList: MutableList<DepositType> = mutableListOf()
        getDepositTypeFromDatabase(depositTypeList)

        val monthsSpinner: Spinner = findViewById(R.id.monthsSpinner)
        monthsSpinner.onItemSelectedListener = this
        val monthsAdapter: ArrayAdapter<DepositType> = ArrayAdapter(
            this,
            R.layout.spinner_item,
            depositTypeList
        )
        monthsAdapter.setDropDownViewResource(R.layout.spinner_item)
        monthsSpinner.adapter = monthsAdapter

        // radio buttons extra options
        val radioAccount: RadioButton = findViewById(R.id.radioAccount)
        val radioDeposit: RadioButton = findViewById(R.id.radioDeposit)
        radioDeposit.setOnClickListener { view ->
            setExtraVisibility("visible")
        }
        radioAccount.setOnClickListener { view ->
            setExtraVisibility("gone")
        }

        // Create account button
        val createAccountButton: Button = findViewById(R.id.openAccountButton)
        createAccountButton.setOnClickListener{ view -> onOpenAccount(view)}
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCurrency = parent?.getItemAtPosition(position)
        if (parent != null) {
            Toast.makeText(parent.context, "Selected: $selectedCurrency", Toast.LENGTH_SHORT).show()
        };
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

    private fun getDepositTypeFromDatabase(dataList: MutableList<DepositType>) {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: Statement = connection.createStatement()
            val results: ResultSet = statement.executeQuery("select * from DEPOSIT_TYPE;")
            while (results.next()) {
                dataList.add(
                    DepositType(
                        results.getInt("nr_months"),
                        results.getDouble("interest_rate")
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
        val user = MApplication.currentUser
        val currency: Currency = spinnerView.selectedItem as Currency
        val balance: Double = balanceView.text.toString().toDouble()
        val type = findViewById<RadioButton>(typeView.checkedRadioButtonId).text.toString()
        val iban = generateIban(type)
        var bic = "BANKRO01"
        if (type =="Deposit") {
            bic = "BANKRO02"
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val sql = "insert into ACCOUNT (iban, user_id, currency_id, bic, balance) values " +
                "(?, ?, ?, ?, ?)"
        val connection: Connection = DatabaseConnection.getConnection()
        val accountStatement: PreparedStatement = connection.prepareStatement(sql)
        accountStatement.setString(1, iban)
        accountStatement.setInt(2, user?.userId!!)
        accountStatement.setInt(3, currency.currencyId)
        accountStatement.setString(4, bic)
        accountStatement.setDouble(5, balance)
        accountStatement.executeUpdate()
        if (type == "Deposit") {
            val monthsSpinner: Spinner = findViewById(R.id.monthsSpinner)
            val renewalView: CheckBox = findViewById(R.id.renewalCheckbox)

            val depositType = monthsSpinner.selectedItem as DepositType
            val renewal = renewalView.isChecked
            val localDate: LocalDate = LocalDate.now().plusMonths(
                depositType.nr_months.toLong() * (if (renewal) 4 else 1)
            )
            val sqlDeposit = "insert into DEPOSIT (iban, nr_months, due_date, renewal) values " +
                    "(?, ?, ?, ?)"
            val depositStatement: PreparedStatement = connection.prepareStatement(sqlDeposit)
            depositStatement.setString(1, iban)
            depositStatement.setInt(2, depositType.nr_months)
            depositStatement.setDate(3, java.sql.Date.valueOf(localDate.toString()))
            depositStatement.setBoolean(4, renewal)
            depositStatement.executeUpdate()
        }

        setResult(RESULT_OK, intent)
        finish()
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

    private fun generateIban(type: String): String {
        var iban:StringBuilder = StringBuilder("RO01BANK")
        if (type == "Deposit") {
            iban.setCharAt(3, '2')
        }
        val rand: Random = Random()
        for (i in 0..15) {
            val number = rand.nextInt(10)
            iban.append(number.toString())
        }
        return iban.toString()
    }
}