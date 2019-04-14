package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.EventDetails
import com.kou.uniclub.Model.Event
import kotlinx.android.synthetic.main.row_event_feed.view.*
import java.text.SimpleDateFormat
import java.util.*


class RvHomeFeed (val events :List<Event>, val context: Context): RecyclerView.Adapter<RvHomeFeed.Holder>() {
    companion object {
        var event_id: Int? = null

    }




    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_event_feed, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val event: Event = events[position]
            //date stuff
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(event.date)
        val day = DateFormat.format("dd", date) as String
        val month=DateFormat.format("MMM",date) as String


        holder.title.text=event.libele
        holder.place.text=event.lieu
        holder.month.text=month
        holder.day.text=day

        //TODO("token null show alert")
        holder.fav.setOnClickListener {
        }


        //Event details
        holder.pic.setOnClickListener {

            event_id=event.id
            Log.d("id_ev", event_id.toString())
            context.startActivity(Intent(context,EventDetails::class.java))

        }




    }









    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.title
        val day = view.day
        val month = view.month
        val place = view.place
        val fav=view.favorite
        //TODO("don forget parsing image with picasso in On bindViewholder")
        val pic=view.im_event

    }


}
