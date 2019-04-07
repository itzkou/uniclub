package com.kou.uniclub.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_day.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val dates:ArrayList<Date>,context: Context) : RecyclerView.Adapter<CalendarAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_day, parent, false))
    }

    override fun getItemCount(): Int {
    return dates.size    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val testing =Calendar.getInstance().time
        val current_day=DateFormat.format("dd", testing) as String
        val date=dates[position]
        val day = DateFormat.format("dd", date) as String
        val day_st=DateFormat.format("EEE",date) as String
        holder.day_str.text=day_st
        holder.day.text=day
        if (day.toInt()==current_day.toInt())
            holder.cardow.setCardBackgroundColor(Color.parseColor("#000000"))





    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val day=view.day_number
        val day_str=view.day_string
        val cardow=view.card_dow

    }
}