package com.kou.uniclub.Activities


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kou.uniclub.Adapter.VpHomeAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Fragments.*
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    //navigation_menu stuff
    private var prevMenuItem: MenuItem? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //navigation_menu
        navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
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


            }

            override fun onPageSelected(position: Int) {

                prevMenuItem?.isChecked = false
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)

                when(position)
                {
                    1-> {if(PrefsManager.geToken(this@Home)==null)
                        BuilderAuth.showDialog(this@Home)}
                    3-> {if(PrefsManager.geToken(this@Home)==null)
                        BuilderAuth.showDialog(this@Home)}
                    4->  {if(PrefsManager.geToken(this@Home)==null)
                        BuilderAuth.showDialog(this@Home)}
                }
            }

        }
        )
        setupViewPager(vp_home)
          }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                vp_home.setCurrentItem(0,false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_calendar -> {
                vp_home.setCurrentItem(1,false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_clubs -> {
                vp_home.setCurrentItem(2,false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                vp_home.setCurrentItem(3,false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                vp_home.setCurrentItem(4,false)

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
        val messagerie=Messaging.newInstance()
        val profile=Profile.newInstance()


        adapter.addFragment(homeFeed)
        adapter.addFragment(calendar)
        adapter.addFragment(clubs)
        adapter.addFragment(messagerie)
        adapter.addFragment(profile)

        viewPager.adapter=adapter


    }

}
