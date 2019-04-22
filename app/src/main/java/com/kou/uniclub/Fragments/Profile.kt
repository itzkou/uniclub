package com.kou.uniclub.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kou.uniclub.Activities.EditProfile
import com.kou.uniclub.Adapter.VpProfileAdapter
import com.kou.uniclub.Fragments.Likes.Clubs
import com.kou.uniclub.Fragments.Likes.Events
import com.kou.uniclub.R


class Profile:Fragment() {
    companion object {

        fun newInstance():Profile=Profile()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_profile,container,false)
        val vpProfile=v.findViewById<ViewPager>(R.id.vpProfile)
        val tabLikes=v.findViewById<TabLayout>(R.id.tabLikes)
        //TODO("hide appBar divider")
        //val appBar=v.findViewById<AppBarLayout>(R.id.appBar)
        //val collapsingBar=appBar.findViewById<CollapsingToolbarLayout>(R.id.collapse)
        val edit=v.findViewById<ImageView>(R.id.editProfile)
        val nested=v.findViewById<NestedScrollView>(R.id.nestedVprofile)
        nested.isFillViewport=true
        setupViewPager(vpProfile,tabLikes)
        edit.setOnClickListener {
            startActivity(Intent(activity!!,EditProfile::class.java))
        }



        return v
    }


    private fun setupViewPager(viewPager: ViewPager,tab:TabLayout) {
        val adapter= VpProfileAdapter(childFragmentManager)

        val likedEvents= Events.newInstance()
        val followedClubs= Clubs.newInstance()
        adapter.addFragment(likedEvents)
        adapter.addFragment(followedClubs)
        viewPager.adapter=adapter
        tab.setupWithViewPager(viewPager)



    }


    }
