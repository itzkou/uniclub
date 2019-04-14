package com.kou.uniclub.Fragments.ClubDetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.kou.uniclub.Adapter.RvHomeFeed
import com.kou.uniclub.Model.FeedResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.fragment_homefeed.*
import kotlinx.android.synthetic.main.fragment_today.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Today:Fragment() {

    companion object {

        fun newInstance(): Today = Today()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(R.layout.fragment_today,container,false)
        val service= UniclubApi.create()

        service.getEvenToday().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Toast.makeText(activity!!,"There are no events Today",Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    rvToday.layoutManager= LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                    rvToday.adapter= RvHomeFeed(response.body()!!.data,activity!!)

                }
            }

        })
        return v    }


}