package com.example.banking_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.banking_app.MApplication;
import com.example.banking_app.R;
import com.example.banking_app.config.DatabaseConnection;
import com.example.banking_app.models.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class EditProfileActivity extends Activity {
    Button btnClose;
    View view;
    String s="user";
    Boolean check=false;
//    SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
    //SharedPreferences sharedPreferences= getSharedPreferences("myPrefs",0);
    String userEmail= "ceva";
    Boolean ok=false;
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

            try {
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("select * from USER where email=\""+userEmail+"\"");
                try {
                    rs.next();

                        txtLn.setText(rs.getString("last_name"));
                        txtFn.setText(rs.getString("first_name"));
                        txtCNP.setText(rs.getString("cnp"));
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
                stmt.executeUpdate("UPDATE USER SET email=\"" + email1 +"\", last_name=\""+last_name1+"\", first_name=\""+first_name1+"\", cnp=\""+cnp1 +  "\" where email=\""+userEmail+"\"");
                Intent i = getIntent();
                setResult(Activity.RESULT_OK, i);
                ok=true;
                assert MApplication.currentUser != null;
                MApplication.currentUser = new User(
                       MApplication.currentUser.getUserId(),
                       email1,
                       last_name1,
                       first_name1,
                       cnp1
                );
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public void setData(){
        txtEmail.setText(userEmail);
        Thread sqlThread = new Thread(new getDataSQL());
        sqlThread.start();
        while (ok!=true){}
        ok=false;
    }
    public void onEditPop(View view){
        //userEmail=getIntent().getStringExtra("test@test.com");
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        while (ok!=true){}
        finish();
    }
    public void onClose(View view)
    {
        finish();
    }
}