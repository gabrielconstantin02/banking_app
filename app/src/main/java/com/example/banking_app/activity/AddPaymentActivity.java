package com.example.banking_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.banking_app.MApplication;
import com.example.banking_app.R;
import com.example.banking_app.config.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class AddPaymentActivity extends AppCompatActivity {

    boolean ok = false;
    int newAmountfrom=0;
    int newAmountsent=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.8));
    }
    public class setUpSQL implements Runnable{
        public void run () {


            // get the DATA EditText
            TextView errorsView = (TextView) findViewById(R.id.errorsTransaction);
            EditText fromView = (EditText) findViewById(R.id.from);
            String from = fromView.getText().toString();
            EditText sendView = (EditText) findViewById(R.id.send);
            String send = sendView.getText().toString();
            EditText amountView = (EditText) findViewById(R.id.amount);
            int amount = Integer.parseInt(amountView.getText().toString());
            Date date=new Date();


            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
            //formatter.format(date)
            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                Statement stmt2 = con.createStatement();
                ResultSet rsFrom = stmt.executeQuery("select * from ACCOUNT where iban=\""+from+"\" and user_id = " + MApplication.currentUser.getUserId());
                ResultSet rsSend = stmt2.executeQuery("select * from ACCOUNT where iban=\""+send+"\"");
                try {
                    rsFrom.next();
                    rsSend.next();
                    System.out.println(amount + " " + (newAmountfrom) + " " +  String.valueOf(newAmountsent));
                    if (rsFrom.getInt("currency_id") != rsSend.getInt("currency_id")) {
                        errorsView.setText("Different currency");
                        return;
                    }
                    if(amount<=rsFrom.getInt("balance"))
                    {
                        newAmountfrom=rsFrom.getInt("balance")-amount;
                        newAmountsent=rsSend.getInt("balance")+amount;
                        System.out.println(String.valueOf(newAmountfrom) + " " +  String.valueOf(newAmountsent) + " " + rsFrom.getInt("balance"));
                    }
                    else {
                        errorsView.setText("Insuficient balance");
                        return;
                    }
                }
                catch (Exception ignored){
                    errorsView.setText("You don't have an account with this iban");
                    return;
                }
                stmt.executeUpdate("insert into TRANSACTION(sender_id, receiver_id, date, amount  ) values ('" + from + "' , '" + send + "' ,'" + formatter.format(date) + "' ,'" + amount + "');");
                stmt.executeUpdate("UPDATE ACCOUNT SET balance=" + newAmountfrom +  " where iban=\""+from+"\"");
                stmt.executeUpdate("UPDATE ACCOUNT SET balance=" + newAmountsent +  " where iban=\""+send+"\"");
                ok = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }


    public void onAddPay(View view) throws InterruptedException {
        // Thread sqlThread = new Thread(new setUpSQL());
        // sqlThread.start();
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        sqlThread.join(0);
        if (ok) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
    public void onClose(View view)
    {
        finish();
    }
}