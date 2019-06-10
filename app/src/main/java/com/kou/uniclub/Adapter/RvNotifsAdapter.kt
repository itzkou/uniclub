package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kou.uniclub.Activities.EventDetails
import com.kou.uniclub.Activities.EventDetails.Companion.eventId
import com.kou.uniclub.Model.Notification.Notification
import com.kou.uniclub.Model.Notification.NotifsActionsResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.row_notifs.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RvNotifsAdapter(private val notifs: ArrayList<Notification>, val context: Context) :
    RecyclerView.Adapter<RvNotifsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
        return RvNotifsAdapter.VH(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_notifs, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val notif = notifs[position]
        holder.title.text = notif.details.eventName
        holder.root.setCardBackgroundColor(if (notif.readAt==null) Color.WHITE else ContextCompat.getColor(context,R.color.grayBar))
        holder.root.setOnClickListener {
            markAsRead(notif)
            eventId=notif.details.eventId
            context.startActivity(Intent(context,EventDetails::class.java))


            //call mark as read api
            //notif.selected = !notif.selected
            //holder.root.setCardBackgroundColor(if (notif.selected) ContextCompat.getColor(context,R.color.orange) else Color.WHITE)
        }



    }

    override fun getItemCount(): Int = notifs.size

    fun addItem(notif: Notification) {
        notifs.add(notif)
        notifyItemInserted(notifs.size)
    }

    fun selectAll() {
        for (i in 0 until notifs.size)
            notifs[i].selected = true

        notifyDataSetChanged()



    }

    fun deselectAll() {
        for (i in 0 until notifs.size)
            notifs[i].selected = false
        notifyDataSetChanged()
    }

    fun removeAll() {
       val iterator=notifs.iterator()
        for (i in iterator)
        if (i.selected)
        {   iterator.remove()


        }
        notifyDataSetChanged()

    }


    fun removeAt(position: Int) {
        notifs.removeAt(position)
        notifyItemRemoved(position)
    }

    fun markAsRead(notif:Notification)
    {val service=UniclubApi.create()
        service.markAsRead("Bearer "+PrefsManager.geToken(context),notif.id).enqueue(object :
            Callback<NotifsActionsResponse> {
            override fun onFailure(call: Call<NotifsActionsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<NotifsActionsResponse>, response: Response<NotifsActionsResponse>) {
                if (response.isSuccessful)
                {
                    Log.d("markAsRead",response.message())
                }
            }

        })
    }



    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val root = view.rootNotif!!
        val title = view.tiNotif!!

    }

}