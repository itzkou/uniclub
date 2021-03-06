package com.kou.uniclub.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
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
import com.kou.uniclub.Activities.Notifications
import com.kou.uniclub.Adapter.RvCalendarAdapter
import com.kou.uniclub.Adapter.RvMyEventsAdapter
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Model.Event.Pagination.EventListResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import es.dmoral.toasty.Toasty
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Calendar : Fragment() {


    companion object {
        fun newInstance(): Calendar = Calendar()
    }

    private lateinit var mCalendar: MaterialCalendarView
    private lateinit var rvMyevents: RecyclerView
    private var page: String? = null

    //TODO("RecyclerView not addding all data only first page is added")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        val rvCalendar = v.findViewById<RecyclerView>(R.id.rvCalendar)
        val miniCal = v.findViewById<ConstraintLayout>(R.id.miniCal)
        val appBar = v.findViewById<AppBarLayout>(R.id.appBar)
        val imSettings = v.findViewById<ImageView>(R.id.imSettings)
        val eventPH = v.findViewById<ConstraintLayout>(R.id.eventPH)
        val token = PrefsManager.geToken(activity!!)
        val imNotifs = v.findViewById<ImageView>(R.id.imNotifs)


        rvMyevents = v.findViewById(R.id.rvMyEvents)
        rvMyevents.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        val show = v.findViewById<View>(R.id.showCal)
        val dialogView = LayoutInflater.from(activity!!).inflate(R.layout.builder_my_calendar, null)
        mCalendar = dialogView.findViewById(R.id.mCalendar)

        val hide = dialogView.findViewById<ImageView>(R.id.hideCal)
        val builder = AlertDialog.Builder(activity!!, R.style.CalendarDialog)
        builder.setView(dialogView)
        val dialogMiniCal = builder.create()


        /********** Custom mini calendar  ****************/
        miniCalendar(rvCalendar)

        if (token != null) {
            /********** My participations  ****************/

            myEvents(rvMyevents)
            /********** Settings  ****************/
            imSettings.setOnClickListener {
                BuilderSettings.showSettings(activity!!)
            }
            /********** MiniCalendar  ****************/
            show.setOnClickListener {
                dialogMiniCal.show()
                hide.setOnClickListener { dialogMiniCal.dismiss() }
            }
            /********** Notifications  ****************/
            imNotifs.setOnClickListener {
                startActivity(Intent(activity!!,Notifications::class.java))
            }
        } else
            eventPH.visibility = View.VISIBLE

        /**********Blurring appBAr****************/
        blurAppBar(appBar)








        return v
    }


    override fun onResume() {
        super.onResume()
        if (PrefsManager.geToken(activity!!) != null)
            myEvents(rvMyevents)

    }


    private fun myEvents(rv: RecyclerView) {
        val service = UniclubApi.create()
        service.getParticipations("Bearer " + PrefsManager.geToken(activity!!))
            .enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                    if (response.isSuccessful && isAdded) {
                        page = response.body()!!.pagination.nextPageUrl
                        if (response.body()!!.pagination.events.size > 0)
                            eventPH.visibility = View.INVISIBLE
                        val adapter = RvMyEventsAdapter(response.body()!!.pagination.events, activity!!)
                        rv.adapter = adapter
                        decoration(response.body()!!.pagination.events, activity!!, mCalendar)
                        //Pagination
                        adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                            override fun onBottomReached(position: Int) {
                                if (page != null)
                                    getMoreItems(adapter)
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
            service.paginateEvents(page!!).enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>) {
                    if (response1.isSuccessful && isAdded) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            decoration(response1.body()!!.pagination.events, activity!!, mCalendar)

                            page = response1.body()!!.pagination.nextPageUrl
                            if (page == null)
                                Toasty.custom(
                                    activity!!,
                                    "Load more",
                                    R.drawable.ic_error_outline_white_24dp,
                                    R.color.black,
                                    Toasty.LENGTH_SHORT,
                                    false,
                                    true
                                ).show()


                        }

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

    private fun blurAppBar(appBar: AppBarLayout) {
        var blurred = false

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val alpha = (p0.totalScrollRange + p1).toFloat() / p0.totalScrollRange
            if ((alpha == 0f || alpha == 1f)) {
                Blurry.delete(miniCal as ViewGroup)
                blurred = false
            } else if ((alpha > 0 && alpha < 1) && !blurred) {
                blurred = true
                Blurry.with(activity!!)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(250)
                    .onto(miniCal as ViewGroup)

            }

        })
    }

}