package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Activities.ClubDetails
import com.kou.uniclub.Activities.ClubDetails.Companion.club_id
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Club.ClubX
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_followed_clubs.view.*

class RvFollowedClubsAdapter(val clubs :ArrayList<ClubX>, val context: Context): RecyclerView.Adapter<RvFollowedClubsAdapter.Holder>() {
    private var onBottomReachedListener: OnBottomReachedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int):RvFollowedClubsAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_followed_clubs, parent, false))
    }

    override fun getItemCount(): Int {
        return clubs.size    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val club: ClubX = clubs[position]
        holder.title.text=club.name
        holder.desc.text=club.location
        Picasso.get().load(club.photo).into(holder.pic)

        holder.root.setOnClickListener {
            club_id = club.id
            context.startActivity(Intent(context, ClubDetails::class.java))
        }

        if (position == clubs.size - 1 && onBottomReachedListener != null) {
            onBottomReachedListener!!.onBottomReached(position)

        }
    }

    class Holder( view: View):RecyclerView.ViewHolder(view){
        val  title= view.foName!!
        val pic=view.foPic!!
        val desc=view.foDesc!!
        val root=view.rootClub!!

    }
    fun addData(listItems: java.util.ArrayList<ClubX>) {
        val size = this.clubs.size
        val sizeNew = listItems.size

        if (size < sizeNew + size) {
            this.clubs.addAll(listItems)
            notifyItemRangeInserted(size, sizeNew)
        }


    }
    fun setOnBottomReachedListener(listener: OnBottomReachedListener) {

        this.onBottomReachedListener = listener
    }
}