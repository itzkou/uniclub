package com.kou.uniclub.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Adapter.RvUnivsAdapter
import com.kou.uniclub.Model.Club.ClubsByUnivResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_clubs_filtered.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClubsFiltered : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs_filtered)
        rvClubs.layoutManager=LinearLayoutManager(this@ClubsFiltered, LinearLayout.VERTICAL,false)
    }

    override fun onStart() {
        super.onStart()

        getClubsByUniv(rvClubs)
    }

    private fun getClubsByUniv(rv: RecyclerView) {
        val service = UniclubApi.create()
        service.getClubsByUniv(RvUnivsAdapter.univID!!).enqueue(object : Callback<ClubsByUnivResponse> {
            override fun onFailure(call: Call<ClubsByUnivResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ClubsByUnivResponse>, response: Response<ClubsByUnivResponse>) {
                rv.adapter = RvClubsAdapter(response.body()!!.clubs, this@ClubsFiltered)
            }

        })
    }
}
