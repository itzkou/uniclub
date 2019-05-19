package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Activities.SearchByUniv
import com.kou.uniclub.Model.University.University
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.row_university.view.*

class RvUnivsAdapter(val universities :ArrayList<University>, val context: Context) : RecyclerView.Adapter<RvUnivsAdapter.Holder>(){

    companion object {
        var mID: Int? = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_university, parent, false))

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val univ: University = universities[position]

        holder.title.text = univ.name


        holder.card.setOnClickListener {
            mID=univ.id
            context.startActivity(Intent(context, SearchByUniv::class.java))


        }


    }




    override fun getItemCount(): Int {
        return universities.size
    }


    class Holder(view: View) : RecyclerView.ViewHolder(view)  {
        val card=view.cardUniv!!
        val image=view.imUniv!!
        val title =view.tvUniv!!


    }
}