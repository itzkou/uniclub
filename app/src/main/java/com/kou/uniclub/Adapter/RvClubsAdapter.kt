package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Activities.FiliterByUniv
import com.kou.uniclub.Model.Club.Club
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_club.view.*

class RvClubsAdapter(val clubs :ArrayList<Club>, val context: Context): RecyclerView.Adapter<RvClubsAdapter.Holder>() {
    companion object {
        var club_id:Int?=null
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_club, parent, false))
    }

    override fun getItemCount(): Int {
        return clubs.size   }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val club: Club = clubs[position]

        holder.title.text=club.name

        holder.root.setOnClickListener {
            club_id=club.id
            context.startActivity(Intent(context, FiliterByUniv::class.java))
        }
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)
    { val title= view.club_title!!
        val root= view.rootClub!!
        //val image= view.im_club
    }


}