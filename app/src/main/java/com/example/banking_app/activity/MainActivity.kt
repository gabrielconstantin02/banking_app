package com.example.banking_app.activity

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.banking_app.R
import com.example.banking_app.fragments.OverviewFragment
import com.example.banking_app.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        // val viewPager: ViewPager = findViewById(R.id.container)
        // viewPager.adapter = sectionsPagerAdapter
        // val tabs: TabLayout = findViewById(R.id.tabs)
        // tabs.setupWithViewPager(viewPager)
        // val fab: FloatingActionButton = findViewById(R.id.fab)

        // fab.setOnClickListener { view ->
        //     Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //             .setAction("Action", null).show()
        // }

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, OverviewFragment()).commit()
        }
    }
}