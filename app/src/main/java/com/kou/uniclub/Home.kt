package com.kou.uniclub

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_calendar -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_clubs -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
