package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.CalendarAdapter
import com.kou.uniclub.Adapter.MyeventsAdapter
import com.kou.uniclub.Model.FeedResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Calendar: Fragment() {

    companion object{
        fun newInstance():Calendar = Calendar()

    }
    private  var state:Boolean=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     val v=inflater.inflate(R.layout.fragment_calendar,container,false)
        val rvCalendar=v.findViewById<RecyclerView>(R.id.rvCalendar)
        val rvMyevents=v.findViewById<RecyclerView>(R.id.rvMyevents)
        val cv=v.findViewById<CardView>(R.id.calendarView)
        val stripe=v.findViewById<ImageView>(R.id.stripe)
        MyEvents(rvMyevents)
        Days(rvCalendar)

            cv.visibility=View.INVISIBLE
        stripe. setOnClickListener {
            cv.visibility=View.VISIBLE
        }




        return v
    }





    fun MyEvents(recyclerView: RecyclerView){
        val service= UniclubApi.create()
        service.getEventFeed().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    recyclerView.layoutManager=LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                   recyclerView.adapter= MyeventsAdapter(response.body()!!.data,activity!!)
                }
            }

        })
    }

    fun Days(recyclerView: RecyclerView){
        val calendar=java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_MONTH,-2)

        var currenTime=calendar.time

        val dates= arrayListOf<Date>()
        for(i in 1..10)
        { dates.add(currenTime)
            calendar.add(java.util.Calendar.DAY_OF_MONTH,1)
            currenTime=calendar.time


        }
        recyclerView.layoutManager=LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)
        recyclerView .adapter=CalendarAdapter(dates,activity!!)
    }

}