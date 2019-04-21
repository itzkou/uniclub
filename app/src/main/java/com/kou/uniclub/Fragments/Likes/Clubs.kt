package com.kou.uniclub.Fragments.Likes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvFollowedClubsAdapter
import com.kou.uniclub.Model.FeedResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Clubs: Fragment() {
    companion object {

        fun newInstance():Clubs=Clubs()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_liked_clubs,container,false)
        val rvFollowerClubs=v.findViewById<RecyclerView>(R.id.rvFollowedClubs)
        val service= UniclubApi.create()
        service.getEventFeed().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    rvFollowerClubs.layoutManager= LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                   rvFollowerClubs.adapter= RvFollowedClubsAdapter(response.body()!!.data,activity!!)
                }
            }

        })


        return v
    }



}