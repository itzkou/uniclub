package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kou.uniclub.Activities.ClubDetails
import com.kou.uniclub.Model.Club.ClubX
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_club.view.*

class RvClubsAdapter(val clubs :ArrayList<ClubX>, val context: Context): RecyclerView.Adapter<RvClubsAdapter.Holder>() {
    companion object {
        var club_id:Int?=null
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_club, parent, false))
    }

    override fun getItemCount(): Int {
        return clubs.size   }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val club: ClubX = clubs[position]

        holder.title.text=club.name

        holder.root.setOnClickListener {
            club_id=club.id
            context.startActivity(Intent(context, ClubDetails::class.java))
        }
        Picasso.get().load(club.photo).into(holder.image)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)
    { val title= view.club_title!!
        val root= view.rootClub!!
        val image=view.cardPic!!
    }
    fun addData(listItems: java.util.ArrayList<ClubX>) {
        val size = this.clubs.size
        val sizeNew = listItems.size

        if (size < sizeNew + size) {
            this.clubs.addAll(listItems)
            notifyItemRangeInserted(size, sizeNew)
        }

    }

    fun removeData(list:java.util.ArrayList<ClubX>){
        val size = this.clubs.size
        val sizeNew = list.size

        this.clubs.clear()
        notifyItemRangeRemoved(0,size)





    }

}