package com.kou.uniclub.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Model.Notif.Notification
import kotlinx.android.synthetic.main.row_notifs.view.*


class NotifsAdapter(private val notifs: ArrayList<Notification>, val context: Context) :
    RecyclerView.Adapter<NotifsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
        return NotifsAdapter.VH(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_notifs, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val notif = notifs[position]
        holder.title.text = notif.msg
        holder.root.setCardBackgroundColor(if (notif.selected) Color.CYAN else Color.WHITE)
        holder.root.setOnClickListener {
            notif.selected = !notif.selected
            holder.root.setCardBackgroundColor(if (notif.selected) Color.CYAN else Color.WHITE)
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



    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val root = view.rootNotif!!
        val title = view.tiNotif!!

    }

}