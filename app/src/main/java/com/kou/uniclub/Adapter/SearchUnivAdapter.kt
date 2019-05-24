package com.kou.uniclub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.kou.uniclub.Model.University.University
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_university.view.*

//TODO("image loading BUG  4th element")
class SearchUnivAdapter(context: Context, univs: ArrayList<University>) : ArrayAdapter<University>(
    context,
    com.kou.uniclub.R.layout.row_university, univs
) {

    var filtered = ArrayList<University>()



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView ?: createView(position, parent)
    }

    private fun createView(position: Int, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_university, parent, false)
        view?.tvUniv?.text = filtered[position].name
        Picasso.get().load(filtered[position].photo).into(view.imUniv)


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

        private fun autocomplete(input: String): ArrayList<University> {
            val results = arrayListOf<University>()

            for (specie in univs) {
                if (specie.name.toLowerCase().contains(input.toLowerCase())) results.add(specie)
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            filtered = results.values as ArrayList<University>
            notifyDataSetInvalidated()
        }

        override fun convertResultToString(result: Any) = (result as University).name
    }
}