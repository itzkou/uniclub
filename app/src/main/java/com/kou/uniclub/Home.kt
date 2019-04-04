package com.kou.uniclub

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.kou.uniclub.Adapter.HomeAdapter
import com.kou.uniclub.Adapter.HomeFeedAdapter.Companion.PERMIS_REQUEST
import com.kou.uniclub.Authentication.Auth
import com.kou.uniclub.Fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    //navigation stuff
    private var prevMenuItem: MenuItem? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //access uniclub functionnalities



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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMIS_REQUEST)
        { val permisResults=HashMap<String,Int>()
            var deniedCount=0


            // gather granted results
            for(i in 0 until grantResults.size)
            {
                if (grantResults[i]== PackageManager.PERMISSION_DENIED)
                {
                    permisResults[permissions[i]] = grantResults[i]
                    deniedCount++
                }


                if(deniedCount==0) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Home, Auth::class.java))
                }
                else
                {
                    for (j in permisResults.entries)
                    {  val key=j.key


                        if(ActivityCompat.shouldShowRequestPermissionRationale(this,key))
                        {
                            Toast.makeText(this, "Enable all permissions in order to use our services", Toast.LENGTH_SHORT).show()


                        }
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show()


                        }
                    }

                }

            }






        }


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
                vp_home.currentItem=4

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
        vp_home.adapter=adapter


    }
}
