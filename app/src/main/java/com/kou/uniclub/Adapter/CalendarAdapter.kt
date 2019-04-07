package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val dates:ArrayList<Date>,context: Context) : RecyclerView.Adapter<CalendarAdapter.Holder>() {
    private val context=context
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_day, parent, false))
    }

    override fun getItemCount(): Int {
    return dates.size    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val calendar=java.util.Calendar.getInstance()

        val currentDate= SimpleDateFormat("yyyy/MM/dd").format(calendar.time)
        val myDate=SimpleDateFormat("yyyy/MM/dd").format(dates[position])
        //data
        val date=dates[position]
        val day = DateFormat.format("dd", date) as String
        val dayName=DateFormat.format("EEE",date) as String
        holder.dayName.text=dayName
        holder.day.text=day
        Log.d("damn","current date: $currentDate   mydate:$myDate   position : $position ")

        if(myDate.equals(currentDate)) {
            holder.card.setImageResource(R.drawable.calendaro)
            holder.card.shadowColor=ContextCompat.getColor(context,R.color.cyan)

        }

        //TODO("When I click a date I do something..")
        holder.card.setOnClickListener {

        }






    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val day= view.day_number!!
        val dayName= view.day_string!!
        val card= view.card_dow!!

    }
}