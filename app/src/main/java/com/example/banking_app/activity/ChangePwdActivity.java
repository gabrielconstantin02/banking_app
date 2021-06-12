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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ChangePwdActivity extends Activity {
    Button btnClose;
    View view;
    String s="user";
    Boolean check=false;
    //    SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
    //SharedPreferences sharedPreferences= getSharedPreferences("myPrefs",0);
    String userEmail= "ceva";
    Boolean ok=false;
    EditText txtPwd1;
    EditText txtPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        txtPwd1=findViewById(R.id.pwd1);
        txtPwd2=findViewById(R.id.pwd2);

        userEmail= this.getIntent().getStringExtra("extra_mail");
        getWindow().setLayout((int)(width*.8),(int)(height*.8));


    }

    public class setUpSQL implements Runnable{
        public void run () {
            // get the email EditText
            EditText pwd1View = (EditText) findViewById(R.id.pwd1);
            String pwd1 = pwd1View.getText().toString();

            // get the password EditText
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
                //add the new account to db
                Connection con = DriverManager.getConnection("jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty("databasePort") +
                        "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty("databaseUsername") + "&password=" + databaseProp.getProperty("databasePassword"));
                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE USER SET password=\"" + pwd1 +  "\" where email=\""+userEmail+"\"");
                ok=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }

    public void onEditPop(View view){
        //userEmail=getIntent().getStringExtra("test@test.com");
        boolean ok2 = true;
        if(PasswordCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Password doesn't match!");
            ok2 = false;
        }
        if(PasswordCheck()==2) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Password should have at least 5 characters!");
            ok2 = false;
        }
        if(ok2) {
            Thread sqlThread = new Thread(new setUpSQL());
            sqlThread.start();
            while (ok != true) {
            }
            finish();
        }
    }

    public int PasswordCheck() {
        EditText pass1View = (EditText)findViewById(R.id.pwd1);
        String pass1 = pass1View.getText().toString();
        EditText pass2View = (EditText)findViewById(R.id.pwd2);
        String pass2 = pass2View.getText().toString();
        if(!pass1.equals(pass2)){
            return 0;
        }
        if(pass1.length()<5)
            return 2;
        return 1;
    }

    public void onClose(View view)
    {
        finish();

    }
}