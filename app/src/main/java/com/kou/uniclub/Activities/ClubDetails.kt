package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.kou.uniclub.Adapter.VpClubDetailsAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Fragments.ClubTimeline.Passed
import com.kou.uniclub.Fragments.ClubTimeline.Today
import com.kou.uniclub.Fragments.ClubTimeline.Upcoming
import com.kou.uniclub.Model.Club.NoPagination.ClubDetailsResponse
import com.kou.uniclub.Model.User.Behaviour.FollowResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_club_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClubDetails : AppCompatActivity() {
    companion object {
        var club_id: Int? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_details)

        setupViewPager(vpClubDetails)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = VpClubDetailsAdapter(supportFragmentManager)
        val passed = Passed.newInstance()
        val today = Today.newInstance()
        val upcoming = Upcoming.newInstance()


        adapter.addFragment(passed)
        adapter.addFragment(today)
        adapter.addFragment(upcoming)

        viewPager.adapter = adapter

        tabTimeline.setupWithViewPager(viewPager)
        getClubInfos(club_id!!)
        //
        cardFollow.setOnClickListener {

            if (PrefsManager.geToken(this@ClubDetails) != null)
                follow(club_id!!)
            else
                BuilderAuth.showDialog(this@ClubDetails)
        }
    }

    private fun follow(id: Int) {
        val service = UniclubApi.create()
        service.follow("Bearer ${PrefsManager.geToken(this@ClubDetails)}", id).enqueue(object : Callback<FollowResponse> {
            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<FollowResponse>, response: Response<FollowResponse>) {
                if (response.isSuccessful)
                Toasty.custom(
                    this@ClubDetails,
                    response.body()!!.message,
                    R.drawable.ic_error_outline_white_24dp,
                    R.color.movento,
                    Toasty.LENGTH_SHORT,
                    false,
                    true
                ).show()
            }

        })

    }

    private fun getClubInfos(id: Int) {
        val service = UniclubApi.create()
        service.getClub(id).enqueue(object : Callback<ClubDetailsResponse> {
            override fun onFailure(call: Call<ClubDetailsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ClubDetailsResponse>, response: Response<ClubDetailsResponse>) {
                if (response.isSuccessful) {
                    val clubx = response.body()!!.club
                    tvClubTitle.text = clubx.name
                    tvClubDesc.text = clubx.description
                    Picasso.get().load(clubx.photo).into(pic)


                }
            }

        })
    }

}

