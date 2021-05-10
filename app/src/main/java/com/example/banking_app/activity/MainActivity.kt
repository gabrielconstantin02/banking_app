package com.example.banking_app.activity

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.banking_app.R
import com.example.banking_app.fragments.OverviewFragment
import com.example.banking_app.fragments.PaymentsFragment
import com.example.banking_app.fragments.ProfileFragment
import com.example.banking_app.ui.main.SectionsPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar);

        val navigationView = findViewById<BottomNavigationView>(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        //val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_overview -> {
                val fragment = OverviewFragment();
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
            R.id.navigation_payments -> {
                val fragment = PaymentsFragment();
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
            R.id.navigation_profile -> {
                val fragment = ProfileFragment();
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
        }
        return false;
    }
}