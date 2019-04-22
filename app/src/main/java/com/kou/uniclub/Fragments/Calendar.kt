package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvCalendarAdapter
import com.kou.uniclub.Adapter.RvMyEventsAdapter
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Calendar: Fragment() {


    companion object {
        fun newInstance(): Calendar = Calendar()

    }

    lateinit var mCalendar:MaterialCalendarView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        val rvCalendar = v.findViewById<RecyclerView>(R.id.rvCalendar)
        val rvMyevents = v.findViewById<RecyclerView>(R.id.rvMyEvents)
        val cv = v.findViewById<CardView>(R.id.calendarView)
        val show = v.findViewById<ImageView>(R.id.showCal)
        val hide = v.findViewById<ImageView>(R.id.hideCal)
         mCalendar = v.findViewById(R.id.mCalendar)
        MyEvents(rvMyevents)
        Days(rvCalendar)

        cv.visibility = View.INVISIBLE
        cv.alpha = 0F
        show.setOnClickListener {
            cv.animate().alpha(1F).duration = 1000
            cv.visibility = View.VISIBLE

        }


        hide.setOnClickListener {
            cv.visibility = View.INVISIBLE
        }







        return v
    }





    fun MyEvents(recyclerView: RecyclerView){
        val service= UniclubApi.create()
        service.getEventFeed().enqueue(object: Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if(response.isSuccessful)
                {   val participations=response.body()!!.pagination.events
                    recyclerView.layoutManager=LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                    recyclerView.adapter= RvMyEventsAdapter(participations,activity!!)

                    for (i in 0 until participations.size)

                    {   val format = SimpleDateFormat("yyyy-MM-dd")
                        val current=CalendarDay.from(format.parse(participations[i].startTime))

                        mCalendar.addDecorator(object : DayViewDecorator {
                            override fun shouldDecorate(day: CalendarDay?): Boolean {

                                return day!!.equals(current)
                            }

                            override fun decorate(view: DayViewFacade?) {
                                view!!.setSelectionDrawable(ContextCompat.getDrawable(activity!!,R.drawable.orange_circle)!!)



                            }

                        })

                    }
                }            }


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
        recyclerView .adapter=RvCalendarAdapter(dates,activity!!)
    }


}