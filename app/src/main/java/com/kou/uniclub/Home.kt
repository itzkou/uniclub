package com.kou.uniclub


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.kou.uniclub.Adapter.HomeAdapter
import com.kou.uniclub.Authentication.SignUP
import com.kou.uniclub.Fragments.*
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    //navigation stuff
    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //viewpager

        vp_home.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(position: Int, p1: Float, p2: Int) {
                Log.d("myTag","Page scrolled")
            }

            override fun onPageSelected(position: Int) {
                prevMenuItem?.isChecked = false
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)
            }

        }
        )
        setupViewPager(vp_home)
          }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                vp_home.currentItem=0

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_calendar -> {

                vp_home.currentItem=1

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_clubs -> {
                vp_home.currentItem=2

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                vp_home.currentItem=3

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                //TODO ALERT if User not loggedIN
                if(PrefsManager.geToken(this@Home)==null)
                    startActivity(Intent(this@Home,SignUP::class.java))
                else vp_home.currentItem=4



                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter=HomeAdapter(supportFragmentManager)

        val homeFeed=HomeFeed.newInstance()
        val calendar=Calendar.newInstance()
        val univs=Universities.newInstance()
        val notif=Notification.newInstance()
        val profile=Profile.newInstance()

        adapter.addFragment(homeFeed)
        adapter.addFragment(calendar)
        adapter.addFragment(univs)
        adapter.addFragment(notif)
        adapter.addFragment(profile)
        viewPager.adapter=adapter


    }

}
