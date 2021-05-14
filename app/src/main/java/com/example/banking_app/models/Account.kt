package com.example.banking_app.models

class Account(iban: String, user_id: Int, currency_id: Int, bic: String, balance: Int) {
    private var mIban : String = iban;
    private var mUserId : Int = user_id;
    private var mCurrencyId : Int = currency_id;
    private var mBic : String = bic;
    private var mBalance : Int = balance;

    fun getIban () : String {
        return mIban;
    }

    fun getUserId () : Int {
        return mUserId;
    }

    fun getCurrencyId () : Int {
        return mCurrencyId;
    }

    fun getBic () : String {
        return mBic;
    }

    fun getBalance () : Int {
        return mBalance;
    }
}