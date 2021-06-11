package com.example.banking_app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banking_app.R

class CreateAccountActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarCreateAccount)
        toolbar.title = "Open account"
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

}