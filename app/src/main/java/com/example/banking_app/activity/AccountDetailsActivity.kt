package com.example.banking_app.activity

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.banking_app.MApplication
import com.example.banking_app.R
import com.example.banking_app.adapters.AccountsPagerAdapter
import com.example.banking_app.config.DatabaseConnection
import com.example.banking_app.models.Account
import com.example.banking_app.models.Currency
import com.google.android.material.tabs.TabLayout
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class AccountDetailsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details);

        val bundle: Bundle? = intent.extras;
        val accountIban: String? = bundle?.getString("iban");
        val account: Account? = getAccount(accountIban?: "null")
        val accountType: String? = bundle?.getString("type")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarAccountDetails)
        toolbar.title = "$accountType: $accountIban";
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener{
            finish();
        }

        println(accountIban)
        val sectionsPagerAdapter = AccountsPagerAdapter(this, supportFragmentManager, account!!, accountType?: "")
        val viewPager : ViewPager = findViewById(R.id.tab_container)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    private fun getAccount(iban: String): Account? {
        try {
            val sql = "select * from ACCOUNT a, CURRENCY c where a.currency_id = c.currency_id and iban = ?"
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: Connection = DatabaseConnection.getConnection()
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setString(1, iban)
            val results: ResultSet = statement.executeQuery()

            if (results.next()) {
                return Account(
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
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return null
    }
}