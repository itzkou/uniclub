package com.kou.uniclub.Fragments.UserBehaviour

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvFollowedClubsAdapter
import com.kou.uniclub.Model.Event.EventListResponse
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
        service.getEventFeed().enqueue(object: Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if(response.isSuccessful)
                {
                    rvFollowerClubs.layoutManager= LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                    rvFollowerClubs.adapter= RvFollowedClubsAdapter(response.body()!!.pagination.events,activity!!)
                }            }

        })


        return v
    }



}