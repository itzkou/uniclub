package com.kou.uniclub.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kou.uniclub.Activities.EditProfile
import com.kou.uniclub.Adapter.VpProfileAdapter
import com.kou.uniclub.Fragments.UserBehaviour.Clubs
import com.kou.uniclub.Fragments.UserBehaviour.Events
import com.kou.uniclub.Model.User.UserX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.Network.UniclubApi.Factory.imageURL
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import jp.wasabeef.blurry.Blurry
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
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_profile, container, false)
        val vpProfile = v.findViewById<ViewPager>(com.kou.uniclub.R.id.vpProfile)
        val imProfile = v.findViewById<ImageView>(com.kou.uniclub.R.id.imProfile)
        val tabLikes = v.findViewById<TabLayout>(com.kou.uniclub.R.id.tabLikes)
        val col=v.findViewById<ConstraintLayout>(R.id.collapso)
        val appBar=v.findViewById<AppBarLayout>(R.id.appBar)
        val token = PrefsManager.geToken(activity!!)
        val edit = v.findViewById<ImageView>(com.kou.uniclub.R.id.imEdit)
        val nested = v.findViewById<NestedScrollView>(com.kou.uniclub.R.id.nestedVprofile)
        val progress=v.findViewById<ProgressBar>(com.kou.uniclub.R.id.progressp)
        val myName=v.findViewById<TextView>(R.id.tvMyname)
        nested.isFillViewport = true
        setupViewPager(vpProfile, tabLikes)
        for (i in 0 until tabLikes.tabCount) {

            when(i)
            {0->tabLikes.getTabAt(i)!!.setIcon(com.kou.uniclub.R.drawable.selector_tab_event)
            1->tabLikes.getTabAt(i)!!.setIcon(com.kou.uniclub.R.drawable.selector_tab_club)}

        }
        edit.setOnClickListener {
            startActivity(Intent(activity!!, EditProfile::class.java))
        }

        if (token!=null)
            getUser(imProfile,progress,myName)
        else
            progress.visibility=View.INVISIBLE

        blurAppBar(appBar,col)


        edit.setOnClickListener {
            startActivity(Intent(activity, EditProfile::class.java))
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

    private fun getUser(im: ImageView,pro:ProgressBar,tv:TextView) {
        val service = UniclubApi.create()
        service.getUser("Bearer " + PrefsManager.geToken(activity!!)).enqueue(object : Callback<UserX> {
            override fun onFailure(call: Call<UserX>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {

                if (response.isSuccessful && isAdded) {
                    val user=response.body()
                    tv.text="${user!!.firstName} ${user.lastName}"
                    picture = user.image
                    if (picture.equals("/storage/Student/Profile_Picture/"))
                        Glide.with(activity!!).load(PrefsManager.getPicture(activity!!))
                            .into(
                                im
                            )
                    else
                        Glide.with(activity!!).load(imageURL + response.body()!!.image) .into(
                                im
                            )

                    pro.visibility=View.GONE

                }
            }

        })
    }
    private fun blurAppBar(appBar: AppBarLayout,col:ConstraintLayout) {
        var blurred = false

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val alpha = (p0.totalScrollRange + p1).toFloat() / p0.totalScrollRange
            if ((alpha == 0f || alpha == 1f)) {
                Blurry.delete(col as ViewGroup)
                blurred = false
            } else if ((alpha > 0 && alpha < 1) && !blurred) {
                blurred = true
                Blurry.with(activity!!)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(250)
                    .onto(col as ViewGroup)

            }

        })
    }

}
