package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.kou.uniclub.Adapter.VpClubDetailsAdapter
import com.kou.uniclub.Fragments.Timeline.Passed
import com.kou.uniclub.Fragments.Timeline.Sponsors
import com.kou.uniclub.Fragments.Timeline.Today
import com.kou.uniclub.Fragments.Timeline.Upcoming
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_club_details.*

class ClubDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_details)

        setupViewPager(vp_clubDetails)




    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter=VpClubDetailsAdapter(supportFragmentManager)
        val passed= Passed.newInstance()
        val today= Today.newInstance()
        val upcoming=Upcoming.newInstance()
        val sponsors=Sponsors.newInstance()


        adapter.addFragment(passed)
        adapter.addFragment(today)
        adapter.addFragment(upcoming)
        adapter.addFragment(sponsors)

        viewPager.adapter=adapter

       tabTimeline.setupWithViewPager(viewPager)


    }

}

