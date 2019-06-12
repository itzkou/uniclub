package com.kou.uniclub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Toast
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_search_filter.view.*
import kotlinx.android.synthetic.main.row_university.view.*
import java.util.*

class SearchEventAdapter(context: Context, events: ArrayList<EventX>) :
    ArrayAdapter<EventX>(context, com.kou.uniclub.R.layout.row_search_filter, events) {


    var filtered = ArrayList<EventX>()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position==5)
            Toast.makeText(context,"5",Toast.LENGTH_SHORT).show()

        return convertView ?: createView(position, parent)

    }

    private fun createView(position: Int, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.row_search_filter, parent, false)
        view?.tiEvent?.text = filtered[position].name
        if (filtered[position].photo!="")
        Picasso.get().load(filtered[position].photo).into(view.imEvent)
        return view


    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        convertView ?: LayoutInflater.from(context).inflate(R.layout.row_university, parent, false)
        convertView?.tvUniv?.text = filtered[position].name
        Picasso.get().load(filtered[position].photo).into(convertView?.imUniv)
        return super.getDropDownView(position, convertView, parent!!)
    }

    override fun getCount() = filtered.size

    override fun getItem(position: Int) = filtered[position]


    override fun getFilter() = filter

    private var filter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = FilterResults()

            val query = if (constraint != null && constraint.isNotEmpty()) autocomplete(constraint.toString())
            else arrayListOf()

            results.values = query
            results.count = query.size

            return results
        }

        private fun autocomplete(input: String): ArrayList<EventX> {
            val results = arrayListOf<EventX>()
            //TODO("Review this logic")
            for (event in events) {

                when {
                    event.name.toLowerCase().contains(input.toLowerCase()) -> results.add(event)
                    event.location.toLowerCase().contains(input.toLowerCase())&&input.length>4 -> results.add(event)
                    event.description.toLowerCase().contains(input.toLowerCase())&&input.length>4 -> results.add(event)
                }

            }



            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            filtered = results.values as ArrayList<EventX>
            notifyDataSetChanged()
        }

        override fun convertResultToString(result: Any) = (result as EventX).name
    }


}