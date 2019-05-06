package com.kou.uniclub.Activities


import android.os.Bundle
import android.support.design.widget.BottomNavigationView

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kou.uniclub.Adapter.VpHomeAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Fragments.*
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    //navigation_menu stuff
    private var prevMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //navigation_menu
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.enableAnimation(false)
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
        navigation.itemHeight = BottomNavigationViewEx.dp2px(this, 56f)
        navigation.setIconsMarginTop(BottomNavigationViewEx.dp2px(this, 16f))



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
                if(PrefsManager.geToken(this@Home)==null)
                    BuilderAuth.showDialog(this@Home)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_clubs -> {
                vp_home.currentItem=2

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                vp_home.currentItem=3
                if(PrefsManager.geToken(this@Home)==null)
                    BuilderAuth.showDialog(this@Home)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                vp_home.currentItem=4
                if(PrefsManager.geToken(this@Home)==null)
                    BuilderAuth.showDialog(this@Home)





                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter=VpHomeAdapter(supportFragmentManager)

        val homeFeed=HomeFeed.newInstance()
        val calendar=Calendar.newInstance()
        val clubs=Universities.newInstance()
        val messagerie=Notification.newInstance()
        val profile=Profile.newInstance()


        adapter.addFragment(homeFeed)
        adapter.addFragment(calendar)
        adapter.addFragment(clubs)
        adapter.addFragment(messagerie)
        adapter.addFragment(profile)

        viewPager.adapter=adapter


    }

}
