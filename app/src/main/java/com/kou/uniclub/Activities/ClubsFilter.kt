package com.kou.uniclub.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Adapter.RvUnivsAdapter.Companion.mID
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_clubs_filter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClubsFilter : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs_filter)
        back_black.setOnClickListener {
            startActivity(Intent(this@ClubsFilter, Home::class.java))
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
        val service= UniclubApi.create()
        service.getClubsByUniv(mID!!).enqueue(object: Callback<ClubsResponse> {
            override fun onFailure(call: Call<ClubsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ClubsResponse>, response: Response<ClubsResponse>) {
                if (response.isSuccessful) {
                    rvClubs.layoutManager = LinearLayoutManager(this@ClubsFilter,LinearLayout.VERTICAL,false)
                    rvClubs.adapter = RvClubsAdapter(response.body()!!.clubs, this@ClubsFilter)

                }            }


        })
    }
    }

