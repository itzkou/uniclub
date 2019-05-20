package com.kou.uniclub.Fragments.ClubTimeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.fragment_passed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Passed:Fragment() {

    companion object {

        fun newInstance(): Passed = Passed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_passed,container,false)

        val service= UniclubApi.create()
        service.getEventFeed().enqueue(object: Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if(response.isSuccessful&&isAdded)
                {
                    rvPassed.layoutManager= LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                    rvPassed.adapter= RvHomeFeedAdapter(response.body()!!.pagination.events,activity!!)
                }            }
        })
        return v
    }


}