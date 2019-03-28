package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Model.Club
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_club.view.*

//TODO("hethy les clubs par universite")
class ClubsAdapter(val clubs :List<Club>, val context: Context): RecyclerView.Adapter<ClubsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_club, parent, false))
    }

    override fun getItemCount(): Int {
        return clubs.size   }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val club: Club = clubs[position]
            holder.title.text=club.nom
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)
    { val title= view.club_title
        //val image= view.im_club
    }


}