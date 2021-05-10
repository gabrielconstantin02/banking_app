package com.example.banking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));
    }
    public class setUpSQL implements Runnable{
        public void run () {
            // get the DATA EditText
            EditText fromView = (EditText) findViewById(R.id.from);
            String from = fromView.getText().toString();
            EditText sendView = (EditText) findViewById(R.id.send);
            String send = sendView.getText().toString();
            EditText textView = (EditText) findViewById(R.id.text);
            String text = textView.getText().toString();
            EditText amountView = (EditText) findViewById(R.id.amount);
            float amount = Float.parseFloat(amountView.getText().toString());
            Date date=new Date();
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
            //formatter.format(date)
            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                //add the new account to db
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.245:3306/bank_db","monty","some123");
                Statement stmt = con.createStatement();
                stmt.executeUpdate("insert into TRANSACTION(sender_id, receiver_id, date, amount  ) values ('" + from + "' , '" + send + "' ,'" + formatter.format(date) + "' ,'" + amount + "' );");
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }


    public void onAddPay(View view){
        // Thread sqlThread = new Thread(new setUpSQL());
        // sqlThread.start();

        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        finish();
    }
    public void onClose(View view)
    {
        finish();

    }
}