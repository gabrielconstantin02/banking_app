package com.example.banking_app.models

import java.util.*

data class Transaction (
    val transaction_id: Int = 0,
    val sender_id: String = "",
    val receiver_id: String = "",
    val date: Date = Date(),
    val amount: Double = 0.0
)