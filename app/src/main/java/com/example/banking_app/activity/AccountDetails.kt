package com.example.banking_app.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.banking_app.R
import com.example.banking_app.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class AccountDetails: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details);

        val bundle: Bundle? = intent.extras;
        val accountIban: String? = bundle?.getString("iban");

        println(accountIban)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager : ViewPager = findViewById(R.id.tab_container)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}