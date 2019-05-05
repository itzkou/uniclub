package com.kou.uniclub.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.kou.uniclub.Adapter.RvCalendarAdapter
import com.kou.uniclub.Adapter.RvMyEventsAdapter
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Calendar : Fragment() {


    companion object {
        fun newInstance(): Calendar = Calendar()
    }

    private lateinit var mCalendar: MaterialCalendarView
    private  lateinit var rvMyevents:RecyclerView
    private var eventList:ArrayList<EventX>?=null
    private var page: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        val rvCalendar = v.findViewById<RecyclerView>(R.id.rvCalendar)
        rvMyevents = v.findViewById(R.id.rvMyEvents)
        val show = v.findViewById<ImageView>(R.id.showCal)


        myEvents(rvMyevents)

        miniCalendar(rvCalendar)
        show.setOnClickListener {
            val dialogView = LayoutInflater.from(activity!!).inflate(R.layout.builder_my_calendar, null)
            mCalendar = dialogView.findViewById(R.id.mCalendar)
            val hide = dialogView.findViewById<ImageView>(R.id.hideCal)
            val builder = AlertDialog.Builder(activity!!)
            builder.setView(dialogView)
            val dialog = builder.create()
            //TODO("optimize decoration")
            decoration(eventList!!,activity!!,mCalendar)
            dialog.show()

            hide.setOnClickListener { dialog.dismiss() }

        }


        return v
    }

    override fun onResume() {
        super.onResume()
        myEvents(rvMyevents)
    }

    private fun myEvents(recyclerView: RecyclerView) {
        val service = UniclubApi.create()
        service.getParticipations("Bearer " + PrefsManager.geToken(activity!!))
            .enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                    if (response.isSuccessful) {
                        page = response.body()!!.pagination.nextPageUrl
                        val participations = response.body()!!.pagination.events
                        recyclerView.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
                        val adapter = RvMyEventsAdapter(participations, activity!!)
                        recyclerView.adapter = adapter
                        eventList=response.body()!!.pagination.events

                        //Pagination

                        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (!recyclerView.canScrollVertically(1)) {
                                    if (page != null)
                                        getMoreItems(adapter)

                                }
                            }
                        })


                    }
                }


            })
    }


    private fun miniCalendar(recyclerView: RecyclerView) {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_MONTH, -2)

        var currenTime = calendar.time

        val dates = arrayListOf<Date>()
        for (i in 1..10) {
            dates.add(currenTime)
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            currenTime = calendar.time


        }
        recyclerView.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = RvCalendarAdapter(dates, activity!!)
    }

    private fun getMoreItems(adapter: RvMyEventsAdapter) {
        val service = UniclubApi.create()
        if (page != null)
            service.paginate(page!!).enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>) {
                    if (response1.isSuccessful) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            page = response1.body()!!.pagination.nextPageUrl

                        } else Toast.makeText(activity!!, "No more items", Toast.LENGTH_SHORT).show()
                    }
                }

            })

    }

    private fun decoration(events: ArrayList<EventX>, context: Context, calendarView: MaterialCalendarView) {
        for (i in 0 until events.size) {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val current = CalendarDay.from(format.parse(events[i].startTime))

            calendarView.addDecorator(object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay?): Boolean {

                    return day!! == current
                }

                override fun decorate(view: DayViewFacade?) {
                    view!!.setSelectionDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.orange_circle
                        )!!
                    )
                    view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)))


                }

            })

        }

    }


}