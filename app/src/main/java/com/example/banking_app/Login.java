package com.example.banking_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banking_app.activity.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    boolean ok=false;
    boolean check=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // get the password EditText
        final EditText mEtPwd = (EditText) findViewById(R.id.password);
        // get the show/hide password Checkbox
        CheckBox mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
    public class checkSQL implements Runnable{
        public void run () {
            EditText emailView = (EditText) findViewById(R.id.email);
            String email = emailView.getText().toString();
            EditText passView = (EditText) findViewById(R.id.password);
            String password = passView.getText().toString();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.245:3306/bank_db","monty","some123");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select* from USER where email=\""+email+"\" and "+"password=\""+password+"\"");
                rs.next();
                if(rs.getString(1) != null)
                    ok = true;
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
            check=true;
        }
    }
    public void onLogin(View view) {///Login button
        Thread sqlThread = new Thread(new checkSQL());
        sqlThread.start();
        //wait until the sql statement is executed
        while(!check){

        }
        if (ok) {
            EditText nameView = (EditText) findViewById(R.id.email);
            String name = nameView.getText().toString();
            check=false;
            ok=false;
            Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra(MainActivity.EXTRA_MESSAGE, name);
            startActivity(intent);
        } else {
            TextView errorView = (TextView) findViewById(R.id.error);
            errorView.setText("Wrong email or password");
            check=false;
            ok=false;
        }
    }
    public void onSignup(View view) {                                ///Signup button
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
