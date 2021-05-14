package com.example.banking_app.models;

import android.app.Application;

public class User extends Application {

    private String userName="ceva";

    public String getSomeVariable() {
        return userName;
    }

    public void setSomeVariable(String someVariable) {
        this.userName = someVariable;
    }
}

