package com.example.banking_app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.banking_app.R;
import com.example.banking_app.config.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class AboutActivity extends Activity {
    Button btnClose;
    View view;
    String s="user";
    Boolean check=false;
    //    SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
    //SharedPreferences sharedPreferences= getSharedPreferences("myPrefs",0);
    String userEmail= "ceva";
    Boolean ok=false;
    TextView txtCNP;
    TextView txtEmail;
    TextView txtLn;
    TextView txtFn;
    TextView txtAcc;
    TextView txtTran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_profile);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        txtCNP=findViewById(R.id.cnp);
        txtEmail=findViewById(R.id.email);
        txtLn =findViewById(R.id.last_name);
        txtFn=findViewById(R.id.first_name);
        userEmail= this.getIntent().getStringExtra("extra_mail");
        setData();
        getWindow().setLayout((int)(width*.8),(int)(height*.8));


    }
    public class getDataSQL implements Runnable{
        public void run () {


            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            txtCNP=findViewById(R.id.cnp);
            txtEmail=findViewById(R.id.email);
            txtLn =findViewById(R.id.last_name);
            txtFn=findViewById(R.id.first_name);
            txtAcc=findViewById(R.id.total_accounts);
            txtTran=findViewById(R.id.total_transactions);
            try {
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("select * from USER where email=\""+userEmail+"\"");

                try {
                    rs.next();
                    //Log.d("UserID", "select COUNT(iban) from ACCOUNT where user_id="+ rs.getInt("user_id"));
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select COUNT(iban) AS count from ACCOUNT where user_id="+ rs.getInt("user_id"));
                    //ResultSet rs3 = stmt.executeQuery("select COUNT(*) from ACCOUNT where user_id=\""+rs.getInt("user_id")+"\"");
                    txtLn.setText("Last name: " + rs.getString("last_name"));
                    txtFn.setText("First name: " + rs.getString("first_name"));
                    txtCNP.setText("CNP: " + rs.getString("cnp"));
                    rs2.next();
                    int nrAcc = rs2.getInt("count");
                    //Log.d("UserID", String.valueOf(rs2.getInt("count")));
                    txtAcc.setText("Number of active banking accounts: " + nrAcc);
                    Statement stmt3 = con.createStatement();
                    ResultSet rs3 = stmt3.executeQuery("select iban from ACCOUNT where user_id="+ rs.getInt("user_id"));
                    int nrTran = 0;
                    while(rs3.next()){
                        Statement stmt4 = con.createStatement();
                        ResultSet rs4 = stmt4.executeQuery("select COUNT(transaction_id) AS counted from TRANSACTION where sender_id=\"" + rs3.getString("iban") + "\"");
                        rs4.next();
                        nrTran += rs4.getInt("counted");
                        stmt4.close();
                    }
                    txtTran.setText("Total number of transactions: " + nrTran);
                    ok=true;

                }
                catch (Exception ignored){

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }

    public void setData(){

        txtEmail.setText("E-mail: " + userEmail);
        Thread sqlThread = new Thread(new getDataSQL());
        sqlThread.start();
        while (ok!=true){}
        ok=false;
    }

    public void onClose(View view)
    {
        finish();

    }
}