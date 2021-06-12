package com.example.banking_app.models;

data class Currency(
    var currencyId: Int = 0,
    var name: String = ""
) {
    override fun toString(): String {
        return name
    }
}

//class Currency(currencyId: Int, name: String) {
//    private var mCurrencyId : Int = currencyId;
//    private var mName : String = name;
//
//    fun getCurrencyId () : Int {
//        return mCurrencyId;
//    }
//
//    fun getName () : String {
//        return mName;
//    }
//
//}

