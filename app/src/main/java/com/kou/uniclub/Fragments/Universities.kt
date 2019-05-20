package com.kou.uniclub.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.kou.uniclub.Activities.ClubsFilter
import com.kou.uniclub.Adapter.RvClubsAdapter
import com.kou.uniclub.Adapter.RvUnivsAdapter.Companion.univID
import com.kou.uniclub.Model.Club.ClubsByUnivResponse
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Universities : Fragment() {
    private var page: String? = null


    companion object {

        fun newInstance(): Universities = Universities()
    }

    //TODO("u fudging dumb findview by id all recycler views")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_clubs, container, false)
        val rvClubs = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvClubs)
        val searchUniv = v.findViewById<ImageView>(R.id.searchUnivs)
        rvClubs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /***** List of all clubs ****/
            getClubs(rvClubs)



        searchUniv.setOnClickListener {
            startActivity(Intent(activity!!, ClubsFilter::class.java))
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