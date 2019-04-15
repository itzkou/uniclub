package com.kou.uniclub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.kou.uniclub.Adapter.ClubsAdapter
import com.kou.uniclub.Adapter.UnivsAdapter.Companion.mID
import com.kou.uniclub.Model.ClubResponse
import com.kou.uniclub.Network.UniclubApi
import kotlinx.android.synthetic.main.activity_clubs_filter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ClubsFilter : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs_filter)
        back_black.setOnClickListener {
            startActivity(Intent(this@ClubsFilter,Home::class.java))
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
        val service= UniclubApi.create()
        service.getClubsByUniv(mID!!).enqueue(object: Callback<ClubResponse> {
            override fun onFailure(call: Call<ClubResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(this@ClubsFilter,"Network faillure ClubsByuniv",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ClubResponse>, response: Response<ClubResponse>) {
                if (response.isSuccessful) {
                    rvClubs.layoutManager = LinearLayoutManager(this@ClubsFilter,LinearLayout.VERTICAL,false)
                    rvClubs.adapter = ClubsAdapter(response.body()!!.data, this@ClubsFilter)

                }
            }

        })
    }
    }

