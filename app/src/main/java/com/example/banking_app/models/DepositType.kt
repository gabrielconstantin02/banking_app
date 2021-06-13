package com.example.banking_app.models

data class DepositType (
    val nr_months: Int,
    val interest_rate: Double
) {
    override fun toString(): String {
        return nr_months.toString()
    }
}