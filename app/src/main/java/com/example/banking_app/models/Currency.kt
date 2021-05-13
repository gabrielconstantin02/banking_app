package com.example.banking_app.models;

class Currency(currencyId: Int, name: String) {
    private var mCurrencyId : Int = currencyId;
    private var mName : String = name;

    fun getCurrencyId () : Int {
        return mCurrencyId;
    }

    fun getName () : String {
        return mName;
    }

}
