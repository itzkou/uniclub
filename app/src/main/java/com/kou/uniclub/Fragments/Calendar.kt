package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Adapter.CalendarAdapter
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Calendar: Fragment() {

    companion object{
        fun newInstance():Calendar = Calendar()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v=inflater.inflate(R.layout.fragment_calendar,container,false)
        val rvCalendar=v.findViewById<RecyclerView>(R.id.rvCalendar)
        val calendar=java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_MONTH,-2)

        var currenTime=calendar.time




        val dates= arrayListOf<Date>()
        for(i in 1..7)
        { dates.add(currenTime)
            calendar.add(java.util.Calendar.DAY_OF_MONTH,1)
            currenTime=calendar.time


        }




        rvCalendar.layoutManager=LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)
        rvCalendar.adapter=CalendarAdapter(dates,activity!!)

        return v
    }
}