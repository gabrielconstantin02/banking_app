package com.example.banking_app;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.banking_app.config.DatabaseConnection;
import com.example.banking_app.models.User;
import com.example.banking_app.activity.MainActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    //check validity of email
    public int EmailCheck() {
        EditText emailView = (EditText)findViewById(R.id.email);
        String email = emailView.getText().toString();
        if(!email.contains("@"))
            return 0;
        return 1;
    }
    //check validity of CNP
    public int CNPCheck() {
        EditText cnpView = (EditText) findViewById(R.id.cnp);
        String cnp = cnpView.getText().toString();
        if(cnp.length() != 13)
            return 0;
        return 1;
    }
    public class setUpSQL implements Runnable {
        public void run () {
            // get the email EditText

            EditText emailView = (EditText) findViewById(R.id.email);
            String email = emailView.getText().toString();
            EditText cnpView = (EditText) findViewById(R.id.cnp);
            String cnp = cnpView.getText().toString();
            EditText lnView = (EditText) findViewById(R.id.last_name);
            String last_name = lnView.getText().toString();
            EditText fnView = (EditText) findViewById(R.id.first_name);
            String first_name = fnView.getText().toString();
            // get the password EditText
            EditText pass1View = (EditText) findViewById(R.id.password);
            String password = pass1View.getText().toString();
            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println(encryptedPassword);
            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                //add the new account to db

                String sql = "insert into USER(user_id, email, password, last_name, first_name, cnp) values (null, ?, ?, ?, ?, ?);";
                Connection con=DatabaseConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, last_name);
                stmt.setString(4, first_name);
                stmt.setString(5, cnp);
                stmt.executeUpdate();
                System.out.println("HEREEEE");
                Log.d("SQLTag", "USR: ");
                try (ResultSet generatedKey = stmt.getGeneratedKeys()) {
                    if (generatedKey.next()) {
                        int userId = generatedKey.getInt(1);
                        System.out.println("NOt HERE");
                        Log.d("SQLTag", "USEER: " + userId);
                        MApplication.currentUser = new User(
                                userId,
                                email,
                                last_name,
                                first_name,
                                cnp
                        );
                    } else {
                        throw new SQLException("Failed to get the user id");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    //confirm the password
    public int PasswordCheck() {
        EditText pass1View = (EditText)findViewById(R.id.password);
        String pass1 = pass1View.getText().toString();
        EditText pass2View = (EditText)findViewById(R.id.password2);
        String pass2 = pass2View.getText().toString();
        if(!pass1.equals(pass2)){
            return 0;
        }
        if(pass1.length()<5)
            return 2;
        return 1;
    }
    public void onCreateAccount(View view) throws InterruptedException {
        boolean ok = true;
        if(EmailCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Invalid email address");
            ok = false;
        }
        if(CNPCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Invalid CNP");
            ok = false;
        }
        if(PasswordCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Password doesn't match!");
            ok = false;
        }
        if(PasswordCheck()==2) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Password should have at least 5 characters!");
            ok = false;
        }
        if(ok) {
            Thread sqlThread = new Thread(new setUpSQL());
            sqlThread.start();
            sqlThread.join(0);
            EditText emailViewsaved= (EditText) findViewById(R.id.email);
            String emailsaved = emailViewsaved.getText().toString();
            SharedPreferences sharedPreferences= getSharedPreferences("myPrefs",0);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("emailUser",emailsaved);
            editor.commit();
            //EditText userView = (EditText)findViewById(R.id.username);
            //String username =userView.getText().toString();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("extra_mail", emailsaved);
            startActivity(intent);
        }
    }
}
