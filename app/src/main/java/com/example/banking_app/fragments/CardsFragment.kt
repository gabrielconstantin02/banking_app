package com.example.banking_app.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.banking_app.R
import com.example.banking_app.activity.GenerateCardActivity
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*


class CardsFragment: Fragment() {

    val iban = "12312AA2313"
    val LAUNCH_SECOND_ACTIVITY = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_cards, container, false);
        val btn_gen_card = view.findViewById(R.id.btnGenCard) as Button
        btn_gen_card.setOnClickListener {
            val intent = Intent(this.activity, GenerateCardActivity::class.java)
            intent.putExtra("extra_iban", iban)
            //startActivity(intent)

            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);

        }
        val cardsLayout : LinearLayout = view.findViewById(R.id.overviewHolder);

        addCards(inflater, cardsLayout)

        return view;
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val ft = requireFragmentManager().beginTransaction()
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false)
                }
                ft.detach(this).attach(this).commit()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    } //onActivityResult


    fun addYears(date: Date?, years: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, years)
        return cal.time
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    private fun addCards(
        inflater: LayoutInflater,
        location: LinearLayout
    ) {

        val databaseProp = Properties()
        try {
            databaseProp.load(javaClass.classLoader.getResourceAsStream("JDBCcredentials.properties"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            //lookup the mysql module
            Class.forName("com.mysql.jdbc.Driver").newInstance()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.d("ClassTag", "Failed1")
        }
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val con: Connection = DriverManager.getConnection(
                "jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty(
                    "databasePort"
                ) +
                        "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty(
                    "databaseUsername"
                ) + "&password=" + databaseProp.getProperty("databasePassword")
            ) as Connection
            Log.d("ClassTag", databaseProp.getProperty("databaseIP"))
            Log.d("ClassTag", databaseProp.getProperty("databaseName"))
            Log.d("ClassTag", databaseProp.getProperty("databasePassword"))

            val stmt = con.createStatement()
            val result: ResultSet = stmt.executeQuery(
                "select * from CARD where iban = \"" + iban + "\";"
            )
            while(result.next()){
                val hold: View = inflater.inflate(R.layout.log_cards, location, false);
                val id = result.getString(1)
                val cvv2 = result.getString(3)
                val type = result.getString(4)
                val validity = result.getString(5)


                val idText: TextView = hold.findViewById(R.id.cardLogId)
                idText.text = id
                val cvv2Text: TextView = hold.findViewById(R.id.cardLogCVV2)
                cvv2Text.text = "CVV2: " + cvv2
                val typeText: TextView = hold.findViewById(R.id.cardLogType)
                typeText.text = type
                val validityText: TextView = hold.findViewById(R.id.cardLogValidity)
                validityText.text = "Valid thru: " + validity.substring(0, 11)

                hold.setOnClickListener {
                    //val accountDetailsIntent = Intent(context, AccountDetailsActivity::class.java);
                    val popupMenu: PopupMenu = PopupMenu(activity, hold)
                    popupMenu.menuInflater.inflate(R.layout.card_popup_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.renew_card -> {
                                var date = Date()
                                date = addYears(date, 3)!!
                                val formatter = SimpleDateFormat("yyyy-MM-dd")
                                val sql =
                                    "update CARD set valid_thru = ? where card_number = ? and iban = ?"
                                try {
                                    val pstmt = con.prepareStatement(sql)
                                    pstmt.setString(3, iban)
                                    pstmt.setString(2, id)
                                    pstmt.setString(1, formatter.format(date))
                                    pstmt.executeUpdate()
                                    val ft = requireFragmentManager().beginTransaction()
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        ft.setReorderingAllowed(false)
                                    }
                                    ft.detach(this).attach(this).commit()
                                } catch (e: SQLException) {
                                    e.printStackTrace()
                                }
                            }
                            R.id.close_card -> {
                                val sql = "delete from CARD where card_number = ? and iban = ?"
                                try {
                                    val pstmt = con.prepareStatement(sql)
                                    pstmt.setString(2, iban)
                                    pstmt.setString(1, id)
                                    pstmt.executeUpdate()
                                    val ft = requireFragmentManager().beginTransaction()
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        ft.setReorderingAllowed(false)
                                    }
                                    ft.detach(this).attach(this).commit()
                                } catch (e: SQLException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        true
                    })
                    popupMenu.show()
                //val bundle = Bundle();
                    //bundle.putString("iban", account.getIban());
                    //accountDetailsIntent.putExtras(bundle);

                    //startActivity(accountDetailsIntent);
                }

                location.addView(hold);
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
            Log.d("SQLTag", "Failed to execute")
        }
    }

}