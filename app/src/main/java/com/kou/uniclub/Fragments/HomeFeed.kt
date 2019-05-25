package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Extensions.BuilderSearchFilter
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class HomeFeed : Fragment() {
    private var page: String? = null


    companion object {

        fun newInstance(): HomeFeed = HomeFeed()
    }

    //TODO("this is the pagination model")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvHome)
        val fab = v.findViewById<FloatingActionButton>(com.kou.uniclub.R.id.fabSearch)
        val imProfile = v.findViewById<ImageView>(com.kou.uniclub.R.id.settings)
        val spRegion = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spRegion)
        val spTiming = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spTiming)
        val token = PrefsManager.geToken(activity!!)

        val resID = resources.getIdentifier(
            "builder_round",
            "drawable", activity!!.packageName
        )
        Log.d("myDrawable",resID.toString())


        //TODO("when user sign in without foto set place holder")
        if (token != null)
            Glide.with(activity!!).load(PrefsManager.getPicture(activity!!)).apply(RequestOptions.circleCropTransform()).into(
                imProfile
            )

        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /********Settings ******/
        imProfile.setOnClickListener {
            if (token != null)
                BuilderSettings.showSettings(activity!!)
            else
                BuilderAuth.showDialog(activity!!)
        }
        /********Floating button ******/
        fab.setOnClickListener { BuilderSearchFilter.showDialog(activity!!) }
        /***Buttons****/


        filters(spTiming, spRegion, rvHome)
        allDates(rvHome)



        return v
    }


    private fun upcoming(rv: RecyclerView) {


        val service = UniclubApi.create()
        service.getUpcomingEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(activity!!, "Network Faillure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
                    rv.adapter = adapter

                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter)
                        }

                    })


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No upcoming events",
                        com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                        com.kou.uniclub.R.color.movento,
                        Toasty.LENGTH_SHORT,
                        true,
                        true
                    ).show()
            }


        })


    }

    private fun allDates(rv: RecyclerView) {
        // All Dates
        val service = UniclubApi.create()
        service.getEventFeed().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
                    rv.adapter = adapter


                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter)
                        }

                    })


                }
            }


        })


    }

    private fun today(rv: RecyclerView) {
        val service = UniclubApi.create()
        service.getTodayEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
                    rv.adapter = adapter

                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter)
                        }

                    })


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No events today",
                        com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                        com.kou.uniclub.R.color.movento,
                        Toasty.LENGTH_SHORT,
                        true,
                        true
                    ).show()
            }


        })
    }

    private fun passed(rv: RecyclerView) {
        // All Dates
        val service = UniclubApi.create()
        service.getPassedEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
                    rv.adapter = adapter

                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter)
                        }

                    })


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No passed events",
                        com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                        com.kou.uniclub.R.color.black,
                        Toasty.LENGTH_SHORT,
                        false,
                        true
                    ).show()
            }


        })
    }

    private fun regionFilter(rv: RecyclerView, city: String) {
        val service = UniclubApi.create()
        service.showByRegion(city).enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
                    rv.adapter = adapter


                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter)
                        }

                    })
                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No events in $city",
                        com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                        com.kou.uniclub.R.color.black,
                        Toasty.LENGTH_SHORT,
                        false,
                        true
                    ).show()

            }

        })
    }

    private fun getMoreItems(adapter: RvHomeFeedAdapter) {
        val service = UniclubApi.create()
        if (page != null)
            service.paginateEvents(page!!).enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>) {
                    if (response1.isSuccessful && isAdded) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            page = response1.body()!!.pagination.nextPageUrl
                            if (page == null)
                                Toasty.custom(
                                    activity!!,
                                    "Load more",
                                    com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                                    com.kou.uniclub.R.color.black,
                                    Toasty.LENGTH_SHORT,
                                    false,
                                    true
                                ).show()


                        }

                    }

                }

            })

    }

    private fun filters(
        spTiming: MaterialSpinner,
        spRegion: MaterialSpinner,
        rv: RecyclerView
    ) {
        spRegion.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.builder_round))
        spTiming.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.builder_round))
        spRegion.setBackgroundResource(com.kou.uniclub.R.drawable.btn_feed)
        spTiming.setBackgroundResource(com.kou.uniclub.R.drawable.btn_feed)
        spTiming.setItems("Today", "Upcoming", "Passed")
        spRegion.setItems(
            "Tunis",
            "Ariana",
            "Ben Arous",
            "Manouba",
            "Nabeul",
            "Zaghouan",
            "Bizerte",
            "Béja",
            "Jendouba",
            "Kef",
            "Siliana",
            "Sousse",
            "Monastir",
            "Mahdia",
            "Sfax",
            "Kairouan",
            "Kasserine",
            "Bouzid",
            "Gabès",
            "Mednine",
            "Tataouine",
            "Gafsa",
            "Tozeur",
            "Kebili"
        )

        spTiming.setOnItemSelectedListener { view, position, id, item ->

            when (position) {
                0 -> today(rv)
                1 -> upcoming(rv)
                2 -> passed(rv)


            }

        }


        //region
        spRegion.setOnItemSelectedListener { view, position, id, item ->
            regionFilter(rv, item.toString())
        }
    }
}