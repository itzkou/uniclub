package com.kou.uniclub.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import com.arlib.floatingsearchview.FloatingSearchView
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Adapter.RvUnivsAdapter
import com.kou.uniclub.Adapter.SearchUnivAdapter
import com.kou.uniclub.Extensions.BuilderUnivs
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Model.University.UniversityResponse
import com.kou.uniclub.Network.UniclubApi
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Universities : Fragment() {
    private var page: String? = null
    private var mSearchAdapter: RvUnivsAdapter? = null


    companion object {
        fun newInstance(): Universities = Universities()
    }

    //TODO("u fudging dumb findview by id all recycler views")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_clubs, container, false)
        val rvClubs = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvClubs)
        val searchUniv = v.findViewById<AutoCompleteTextView>(com.kou.uniclub.R.id.searchUnivs)
        rvClubs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /***** List of all clubs ****/
        getClubs(rvClubs)

        /*** Show dialog fragment for univ filter*****/

        val service = UniclubApi.create()
        service.getUniversities().enqueue(object : Callback<UniversityResponse> {
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if (response.isSuccessful && isAdded) {
                    val adapter = SearchUnivAdapter(activity!!, response.body()!!.pagination.universities)
                    searchUniv.setAdapter(adapter)
                }
            }

        })

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
                    val adapter = RvClubsAdapter(response.body()!!.pagination.clubs, activity!!)
                    rv.adapter = adapter

                    //Pagination

                    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!rv.canScrollVertically(1)) {
                                if (page != null)
                                    getMoreClubs(adapter)

                            }

                        }
                    })

                }
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


/* val dialog = BuilderUnivs()
        val ft = childFragmentManager.beginTransaction()
        dialog.show(ft, BuilderUnivs.TAG)*/
}