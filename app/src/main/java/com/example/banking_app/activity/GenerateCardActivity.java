package com.example.banking_app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.banking_app.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class GenerateCardActivity extends Activity {

    String type;
    String iban;
    String cardNumber;
    Boolean ok=false;
    Date date=new Date();
    Spinner spinner;

    public String genCardNumber(String iban, Date date, String type){
        String t;
        if (type.equals("Visa Classic")){
            t="1";
        }else if (type.equals("Visa Electron")){
            t="2";
        }  else{
          t="3";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return (t + iban.substring(0, 3) + formatter.format(date) + iban.substring(iban.length()-4));
    }


    public Date addYears(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_card);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        iban= this.getIntent().getStringExtra("extra_iban");
        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        spinner = findViewById(R.id.type);
        List<String> cardTypes = new ArrayList<>();
        cardTypes.add(0, "Select card type");
        cardTypes.add("Visa Classic");
        cardTypes.add("Visa Electron");
        cardTypes.add("Mastercard");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cardTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select card type")){
                }else {
                    type = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public class setUpSQL implements Runnable{
        public void run () {
            date = addYears(date, 3);
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
            EditText cvv2View = (EditText) findViewById(R.id.cvv2);
            String cvv2 = cvv2View.getText().toString();

            cardNumber = genCardNumber(iban, date, type);
            Properties databaseProp = new Properties();
            try {
                databaseProp.load(getClass().getClassLoader().getResourceAsStream("JDBCcredentials.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty("databasePort") +
                        "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty("databaseUsername") + "&password=" + databaseProp.getProperty("databasePassword"));
                Statement stmt = con.createStatement();
                stmt.executeUpdate("insert into CARD(card_number, iban, CCV2, type, valid_thru) values ('" + cardNumber + "' , '" + iban + "' , '" + cvv2 + "' , '" + type + "' ,'" + formatter.format(date) + "');");
                ok=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }

    public void onGenPop(View view){
        Thread sqlThread = new Thread(new GenerateCardActivity.setUpSQL());
        sqlThread.start();
        while (ok!=true){}
        finish();
    }
    public void onClose(View view)
    {
        finish();

    }
}
