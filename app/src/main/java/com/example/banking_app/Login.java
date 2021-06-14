package com.example.banking_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.banking_app.config.DatabaseConnection;
import com.example.banking_app.models.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Login extends AppCompatActivity {
    boolean ok=false;
    boolean check;
    String name;

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
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from USER where email=\""+email+"\" and "+"password=\""+password+"\"");
                try {
                    if (rs.next()) {
                        ok = true;
                        int userId = rs.getInt("user_id");
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String cnp = rs.getString("cnp");
                        MApplication.currentUser = new User(
                                userId,
                                email,
                                firstName,
                                lastName,
                                cnp
                        );
                    }
                }
                catch (Exception ignored){

                }
                //con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public void onLogin(View view) throws InterruptedException {///Login button
            Thread sqlThread = new Thread(new checkSQL());
            sqlThread.start();
            //wait until the sql statement is executed
            sqlThread.join(0);
            if (ok) {
                EditText nameView = (EditText) findViewById(R.id.email);
                name = nameView.getText().toString();
                ok = false;
                EditText emailViewsaved = (EditText) findViewById(R.id.email);
                String emailsaved = emailViewsaved.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("emailUser", emailsaved);
                editor.commit();

                Intent intent = new Intent(this, MainActivity.class);
                finish();
                intent.putExtra("extra_mail", emailsaved);
                startActivity(intent);



            } else {
                TextView errorView = (TextView) findViewById(R.id.error);
                errorView.setText("Wrong email or password");
                ok = false;
            }
    }
    public void onSignup(View view) {///Signup button
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
