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
import com.kou.uniclub.Fragments.UserBehaviour.Clubs
import com.kou.uniclub.Fragments.UserBehaviour.Events
import com.kou.uniclub.Model.User.User
import com.kou.uniclub.Model.User.UserX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Profile : Fragment() {
    companion object {

        fun newInstance(): Profile = Profile()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        val vpProfile = v.findViewById<ViewPager>(R.id.vpProfile)
        val imProfile = v.findViewById<ImageView>(R.id.imProfile)
        val tabLikes = v.findViewById<TabLayout>(R.id.tabLikes)

        val edit = v.findViewById<ImageView>(R.id.editProfile)
        val nested = v.findViewById<NestedScrollView>(R.id.nestedVprofile)
        nested.isFillViewport = true
        setupViewPager(vpProfile, tabLikes)
        edit.setOnClickListener {
            startActivity(Intent(activity!!, EditProfile::class.java))
        }
        if (PrefsManager.geToken(activity!!) != null)
            userInfos(PrefsManager.geToken(activity!!)!!)

        return v
    }


    private fun setupViewPager(viewPager: ViewPager, tab: TabLayout) {
        val adapter = VpProfileAdapter(childFragmentManager)

        val likedEvents = Events.newInstance()
        val followedClubs = Clubs.newInstance()
        adapter.addFragment(likedEvents)
        adapter.addFragment(followedClubs)
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)


    }

    private fun userInfos(token: String) {
        val service = UniclubApi.create()
        service.getUser(token).enqueue(object : Callback<UserX> {
            override fun onFailure(call: Call<UserX>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {
                if (response.isSuccessful && isAdded) {
                    val u = response.body()!!
                    Picasso.get().load(u.image).into(imProfile)


                }
            }

        })
    }


}
