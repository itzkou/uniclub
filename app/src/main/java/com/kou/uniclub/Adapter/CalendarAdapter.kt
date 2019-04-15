package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_day.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val dates:ArrayList<Date>, context: Context) : RecyclerView.Adapter<CalendarAdapter.Holder>() {
    private val context=context
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_day, parent, false))
    }

    override fun getItemCount(): Int {
    return dates.size    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val calendar=java.util.Calendar.getInstance()


        //data
        val date=dates[position]
        val day = DateFormat.format("dd", date) as String
        val dayName=DateFormat.format("EEE",date) as String
        holder.dayName.text=dayName
        holder.day.text=day
        Log.d("testo",Calendar.DAY_OF_MONTH.toString())

        if(day.toInt() == calendar.get(Calendar.DAY_OF_MONTH)) {
            //holder.card.shadowColor=ContextCompat.getColor(context,R.color.cyan)
            holder.card.setImageResource(R.drawable.calendaro)

        }
        else{
            holder.card.setImageResource(R.drawable.calendaro_greyed)


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