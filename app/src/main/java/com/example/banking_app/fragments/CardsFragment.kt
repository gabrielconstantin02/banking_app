package com.example.banking_app.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.banking_app.R
import com.example.banking_app.activity.GenerateCardActivity
import com.mysql.jdbc.Connection
import java.io.IOException
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


class CardsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_cards, container, false);
        val btn_gen_card = view.findViewById(R.id.btnGenCard) as Button
        btn_gen_card.setOnClickListener {
            val intent = Intent(this.activity, GenerateCardActivity::class.java)
            val iban = "12312AA2313"
            intent.putExtra("extra_iban", iban)
            startActivity(intent)
        }
        val cardsLayout : LinearLayout = view.findViewById(R.id.overviewHolder);

        addCards(inflater, cardsLayout)

        return view;
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
            Log.d("ClassTag", databaseProp.getProperty("databaseIP"))
            Log.d("ClassTag", databaseProp.getProperty("databaseName"))
            Log.d("ClassTag", databaseProp.getProperty("databasePassword"))
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
                "select * from CARD;"
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
                cvv2Text.text = cvv2
                val typeText: TextView = hold.findViewById(R.id.cardLogType)
                typeText.text = type
                val validityText: TextView = hold.findViewById(R.id.cardLogValidity)
                validityText.text = validity

                hold.setOnClickListener {
                    //val accountDetailsIntent = Intent(context, AccountDetailsActivity::class.java);
                    val popupMenu: PopupMenu = PopupMenu(activity, hold)
                    popupMenu.menuInflater.inflate(R.layout.card_popup_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.renew_card ->
                                Toast.makeText(
                                    activity,
                                    "You Clicked : " + item.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            R.id.close_card ->
                                Toast.makeText(
                                    activity,
                                    "You Clicked : " + item.title,
                                    Toast.LENGTH_SHORT
                                ).show()
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