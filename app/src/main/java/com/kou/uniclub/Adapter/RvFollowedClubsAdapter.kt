package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Model.Event.EventX
import kotlinx.android.synthetic.main.row_event_feed.view.*

class RvFollowedClubsAdapter(val events :ArrayList<EventX>, val context: Context): RecyclerView.Adapter<RvFollowedClubsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int):RvFollowedClubsAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_liked_event, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val event: EventX = events[position]
        holder.title.text=event.name
    }

    class Holder( view: View):RecyclerView.ViewHolder(view){
        val  title= view.title!!
    }
}