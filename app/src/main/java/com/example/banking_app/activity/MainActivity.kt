package com.example.banking_app.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.banking_app.R
import com.example.banking_app.fragments.OverviewFragment
import com.example.banking_app.fragments.PaymentsFragment
import com.example.banking_app.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var userMail:String? =""
    var PROFILE_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMail= intent.getStringExtra("extra_mail")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<BottomNavigationView>(R.id.navigationView)
        navigationView.setOnNavigationItemSelectedListener(this)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, OverviewFragment()).commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_overview -> {
                val fragment = OverviewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                return true
            }
            R.id.navigation_payments -> {
                val fragment = PaymentsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                return true
            }
            R.id.navigation_profile -> {
                val fragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                return true
            }
        }
        return false
    }

//    fun onEditProfile(view: View?) {                                ///EditProfilebutton
//        val intent1 = Intent(this, EditProfileActivity::class.java)
//        intent1.putExtra("extra_mail", userMail)
//        startActivityForResult(intent1, PROFILE_CODE)
//    }
//
    fun onAbout(view: View?) {
        val intent = Intent(this, AboutActivity::class.java)
        intent.putExtra("extra_mail", userMail)
        startActivity(intent)
    }
    fun onChange(view: View?) {
        val intent = Intent(this, ChangePwdActivity::class.java)
        intent.putExtra("extra_mail", userMail)
        startActivity(intent)
    }
}