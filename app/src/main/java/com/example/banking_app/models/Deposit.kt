package com.example.banking_app.models

import java.util.*

data class Deposit (
    var iban: String = "",
    var nr_months: Int = 0,
    var due_date: Date = Date(),
    var renewal: Boolean = false,
    var account: Account?,
    var depositType: DepositType?
)