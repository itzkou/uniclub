package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Model.University.University
import com.kou.uniclub.Model.University.UniversityResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Universities : Fragment() {
    private var page: String? = null
    private var mList:ArrayList<University>?=null
    lateinit var searchUniv: android.support.v7.widget.SearchView
    lateinit var autoComplete: android.support.v7.widget.SearchView.SearchAutoComplete

    companion object {

        fun newInstance(): Universities = Universities()
        var suggestions = arrayListOf<String>("ensi", "essted")
    }

    //TODO("u fudging dumb findview by id all recycler views")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_clubs, container, false)
        val rvClubs = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvClubs)
        searchUniv = v.findViewById(com.kou.uniclub.R.id.searchUnivs)
        autoComplete = searchUniv.findViewById(android.support.v7.appcompat.R.id.search_src_text)
        rvClubs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        getClubs(rvClubs)
        val service=UniclubApi.create()

        service.getUniversities().enqueue(object :Callback<UniversityResponse>{
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if (response.isSuccessful&&isAdded)
                {  val x=response.body()!!.pagination.universities
                    val arr= arrayListOf<String>()
                    for (i in 0 until x.size)
                        arr.add(x[i].name)


                    val dataAdapter = ArrayAdapter<String>(activity!!, R.layout.search_autocomplete,R.id.tvHint,arr)
                   autoComplete.setAdapter(dataAdapter)






                }
            }

        })






        /***** List of all clubs ****/


        /***** search university by name ****/


        return v
    }

    private fun getClubs(rv:RecyclerView){
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
                                    getMoreItems(adapter)

                            }

                        }
                    })

                }
            }

        })
    }
    private fun getMoreItems(adapter: RvClubsAdapter) {
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


}