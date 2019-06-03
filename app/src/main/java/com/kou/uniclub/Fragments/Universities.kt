package com.kou.uniclub.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import com.kou.uniclub.Activities.Notifications
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Adapter.SearchUnivAdapter
import com.kou.uniclub.Model.Club.NoPagination.ClubsByUnivResponse
import com.kou.uniclub.Model.Club.Pagination.ClubsResponse
import com.kou.uniclub.Model.University.University
import com.kou.uniclub.Model.University.NoPagination.UniversitiesResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Universities : Fragment() {
    private var page: String? = null
    private var adapterClubs: RvClubsAdapter? = null


    companion object {
        fun newInstance(): Universities = Universities()
    }

    //TODO("u fudging dumb findview by id all recycler views")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_clubs, container, false)
        val rvClubs = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvClubs)
        val searchUniv = v.findViewById<AutoCompleteTextView>(com.kou.uniclub.R.id.searchFilter)
        val imNotifs = v.findViewById<ImageView>(R.id.imNotifs)
        val token = PrefsManager.geToken(activity!!)
        rvClubs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /***** List of all clubs ****/
        getClubs(rvClubs)

        /***** Feeding Autocomplete ****/
        feedAutocomplete(searchUniv)
        /********** Notifications  ****************/
        if (token != null)
            imNotifs.setOnClickListener {
                startActivity(Intent(activity!!, Notifications::class.java))
            }


        return v
    }


    private fun getClubs(rv: RecyclerView) {
        val uniclub = UniclubApi.create()
        uniclub.getClubs().enqueue(object : Callback<ClubsResponse> {
            override fun onFailure(call: Call<ClubsResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ClubsResponse>, response: Response<ClubsResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    adapterClubs = RvClubsAdapter(response.body()!!.pagination.clubs, activity!!)
                    rv.adapter = adapterClubs


                    //Pagination

                    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!rv.canScrollVertically(1)) {
                                if (page != null)
                                    getMoreClubs(adapterClubs!!)

                            }

                        }
                    })


                }
            }

        })
    }

    private fun filterClubs(adapter: RvClubsAdapter, univID: Int) {
        val service = UniclubApi.create()
        service.getClubsByUniv(univID).enqueue(object : Callback<ClubsByUnivResponse> {
            override fun onFailure(call: Call<ClubsByUnivResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ClubsByUnivResponse>, response: Response<ClubsByUnivResponse>) {
                if (response.isSuccessful && isAdded)
                    adapter.removeData(response.body()!!.clubs)

            }

        })
    }


    private fun getMoreClubs(adapter: RvClubsAdapter) {
        val service = UniclubApi.create()
        if (page != null)
            service.paginateClubs(page!!).enqueue(object : Callback<ClubsResponse> {
                override fun onFailure(call: Call<ClubsResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<ClubsResponse>, response1: Response<ClubsResponse>) {
                    if (response1.isSuccessful && isAdded) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.clubs)
                            page = response1.body()!!.pagination.nextPageUrl

                        } else Toasty.custom(
                            activity!!,
                            "No more items",
                            com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                            com.kou.uniclub.R.color.black,
                            Toasty.LENGTH_SHORT,
                            true,
                            true
                        ).show()
                    }
                }


            })

    }

    private fun feedAutocomplete(searchUniv: AutoCompleteTextView) {
        val service = UniclubApi.create()
        service.getUniversities().enqueue(object : Callback<UniversitiesResponse> {
            override fun onFailure(call: Call<UniversitiesResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<UniversitiesResponse>, response: Response<UniversitiesResponse>) {
                if (response.isSuccessful && isAdded) {
                    val adapter = SearchUnivAdapter(activity!!, response.body()!!.univs)
                    searchUniv.setAdapter(adapter)
                    searchUniv.setOnItemClickListener { parent, view, position, id ->
                        val item = parent.getItemAtPosition(position) as University
                        filterClubs(adapter = adapterClubs!!, univID = item.id)

                    }
                }
            }

        })
    }
}