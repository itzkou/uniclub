package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
        val v = inflater.inflate(R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(R.id.rvHome)
        val fab = v.findViewById<FloatingActionButton>(R.id.fabSearch)
        val imProfile = v.findViewById<ImageView>(R.id.settings)
        val spRegion = v.findViewById<MaterialSpinner>(R.id.spRegion)
        val spTiming = v.findViewById<MaterialSpinner>(R.id.spTiming)
        val token = PrefsManager.geToken(activity!!)


        val region = arrayOf(
            " ",
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
        val arrRegion = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, region)


        //TODO("when user sign in without foto set place holder")
        if (token != null)
            Glide.with(activity!!).load(PrefsManager.getPicture(activity!!)).apply(RequestOptions.circleCropTransform()).into(
                imProfile
            )

        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        allDates(rvHome)
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


        filters(spTiming, spRegion, arrRegion, rvHome)



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
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.movento,
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
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.movento,
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
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.black,
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
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.black,
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
                                    R.drawable.ic_error_outline_white_24dp,
                                    R.color.black,
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
        arrRegion: ArrayAdapter<String>,
        rv: RecyclerView
    ) {
        spRegion.setBackgroundResource(R.drawable.btn_feed)
        spTiming.setBackgroundResource(R.drawable.btn_feed)
        spTiming.setItems("", "Today", "Upcoming", "Passed")
        spRegion.setItems(
            " ",
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
            if (position != 0)
                when (position) {
                    1 -> today(rv)
                    2 -> upcoming(rv)
                    3 -> passed(rv)


                }

        }


        //region
        spRegion.setOnItemSelectedListener { view, position, id, item ->
            if (position != 0)
                regionFilter(rv, arrRegion.getItem(position)!!)
        }
    }
}