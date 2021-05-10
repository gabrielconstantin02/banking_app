package com.example.banking_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.banking_app.classes.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfileActivity extends Activity {
    Button btnClose;
    View view;
    String s="user";
    Boolean check=false;
    //SharedPreferences sharedPreferences= getSharedPreferences("myPrefs",0);
    String userEmail="user";
            //sharedPreferences.getString("emailUser","");
            //PreferenceManager.getDefaultSharedPreferences().getString("emailUser","user");
    EditText txtCNP;
    EditText txtEmail;
    EditText txtLn;
    EditText txtFn;
    EditText txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        txtCNP=findViewById(R.id.cnp);
        txtEmail=findViewById(R.id.email);
        txtLn =findViewById(R.id.last_name);
        txtFn=findViewById(R.id.first_name);

        setData();
        getWindow().setLayout((int)(width*.8),(int)(height*.7));


    }
    public class getDataSQL implements Runnable{
        public void run () {


            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.6:3306/bank_db","root","");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select top 1 * from USER where email=\""+userEmail+"\"");
                //rs.next();

                txtEmail.setText("Ceva");
                txtLn.setText("test");
                txtFn.setText("test");
                txtCNP.setText("test");
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
            check=true;
        }
    }
    public class setUpSQL implements Runnable{
        public void run () {
            // get the email EditText
            EditText emailView = (EditText) findViewById(R.id.email);
            String email1 = emailView.getText().toString();
            EditText cnpView = (EditText) findViewById(R.id.cnp);
            String cnp1 = cnpView.getText().toString();
            EditText lnView = (EditText) findViewById(R.id.last_name);
            String last_name1 = lnView.getText().toString();
            EditText fnView = (EditText) findViewById(R.id.first_name);
            String first_name1 = fnView.getText().toString();
            // get the password EditText
            EditText pass1View = (EditText) findViewById(R.id.password);
            String password1 = pass1View.getText().toString();

            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                //add the new account to db
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.6:3306/bank_db","root","");
                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE USER SET email=" + email1 +", password="+password1+", last_name="+last_name1+", first_name="+first_name1+", cnp="+cnp1 +  " where email=\""+userEmail+"\"");
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public void setData(){

        txtEmail.setText("Ceva");
        txtLn.setText("test");
        txtFn.setText("test");
        txtCNP.setText("test");
    }
    public void onEditPop(View view){
        userEmail=getIntent().getStringExtra("extra_email");
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        finish();
    }
    public void onClose(View view)
    {
        finish();

    }
}