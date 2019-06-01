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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kou.uniclub.Activities.EditProfile
import com.kou.uniclub.Adapter.VpProfileAdapter
import com.kou.uniclub.Fragments.UserBehaviour.Clubs
import com.kou.uniclub.Fragments.UserBehaviour.Events
import com.kou.uniclub.Model.User.UserX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.Network.UniclubApi.Factory.imageURL
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO("Photos taken with camera not loading ")

class Profile : Fragment() {
    private var picture: String? = null

    companion object {

        fun newInstance(): Profile = Profile()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        val vpProfile = v.findViewById<ViewPager>(R.id.vpProfile)
        val imSettings = v.findViewById<ImageView>(R.id.imSettings)
        val tabLikes = v.findViewById<TabLayout>(R.id.tabLikes)
        val token = PrefsManager.geToken(activity!!)
        val edit = v.findViewById<ImageView>(R.id.imNotifs)
        val nested = v.findViewById<NestedScrollView>(R.id.nestedVprofile)
        nested.isFillViewport = true
        setupViewPager(vpProfile, tabLikes)
        edit.setOnClickListener {
            startActivity(Intent(activity!!, EditProfile::class.java))
        }








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

    private fun getUser(im: ImageView) {
        val service = UniclubApi.create()
        service.getUser("Bearer " + PrefsManager.geToken(activity!!)).enqueue(object : Callback<UserX> {
            override fun onFailure(call: Call<UserX>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {

                if (response.isSuccessful) {
                    picture = response.body()!!.image
                    if (picture.equals("/storage/Student/Profile_Picture/"))
                        Glide.with(activity!!).load(PrefsManager.getPicture(activity!!)).apply(RequestOptions.circleCropTransform())
                            .into(
                                im
                            )
                    else
                        Glide.with(activity!!).load(imageURL + response.body()!!.image).apply(
                            RequestOptions.circleCropTransform()
                        )
                            .into(
                                im
                            )
                }
            }

        })
    }


}
