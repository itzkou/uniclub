package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.University.University
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_university.view.*

class RvUnivsAdapter(val universities: ArrayList<University>, val context: Context) :
    RecyclerView.Adapter<RvUnivsAdapter.Holder>() {
    private var onBottomReachedListener: OnBottomReachedListener? = null

    companion object {
        var univID: Int? = null

    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_university, parent, false))

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val univ: University = universities[position]

        holder.title.text = univ.name
        Picasso.get().load(univ.photo).into(holder.image)


        holder.card.setOnClickListener {
            univID = univ.id
            //context.startActivity(Intent(context, ClubsFiltered::class.java))

        }

        if (position == universities.size - 1 && onBottomReachedListener != null) {
            onBottomReachedListener!!.onBottomReached(position)

        }
    }


    override fun getItemCount(): Int {
        return universities.size
    }


    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.cardUniv!!
        val image = view.imUniv!!
        val title = view.tvUniv!!


    }

    fun addData(listItems: java.util.ArrayList<University>) {
        val size = this.universities.size
        val sizeNew = listItems.size

        if (size < sizeNew + size) {
            this.universities.addAll(listItems)
            notifyItemRangeInserted(size, sizeNew)
        }

    }

    fun setOnBottomReachedListener(listener: OnBottomReachedListener) {

        this.onBottomReachedListener = listener
    }
}