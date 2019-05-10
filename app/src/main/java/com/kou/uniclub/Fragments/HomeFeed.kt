package com.kou.uniclub.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.Stories
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Extensions.BuilderSearchFilter
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeFeed : Fragment() {
    private var page: String? = null
    private var upEvents: ArrayList<EventX> = arrayListOf()



//TODO("onDetaach close web service calls")


    companion object {

        fun newInstance(): HomeFeed = HomeFeed()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(R.id.rvHome)
        val fab = v.findViewById<FloatingActionButton>(R.id.fabSearch)
        val liveAnim = v.findViewById<LottieAnimationView>(R.id.btnStory)
        val imProfile=v.findViewById<CircularImageView>(R.id.settings)
        val btnUpcoming = v.findViewById<Button>(R.id.btnUpcoming)
        val btnToday = v.findViewById<Button>(R.id.btnToday)
        val btnLive=v.findViewById<Button>(R.id.btnLive)

        if (PrefsManager.geToken(activity!!)!=null)
        imProfile.setImageURI(Uri.parse(PrefsManager.getPicture(activity!!)))




        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        allDates(rvHome)
        /********Settings ******/
        imProfile.setOnClickListener {
            BuilderSettings.showSettings(activity!!)
        }
        /********Floating button ******/
        fab.setOnClickListener { BuilderSearchFilter.showDialog(activity!!) }
        /***Buttons****/

        btnUpcoming.setOnClickListener {
            upcoming(rvHome)
        }

        btnToday.setOnClickListener {
            today(rvHome)
        }

        btnLive.setOnClickListener {
            startActivity(Intent(activity!!,Stories::class.java))
        }

        return v
    }


    private fun upcoming(rv: RecyclerView) {
        upEvents.clear()

        val service = UniclubApi.create()
        service.getUpcomingEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(activity!!, "Network Faillure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    if (response.body()!!.pagination.events.size > 0) {
                        upEvents.addAll(response.body()!!.pagination.events)
                        page = response.body()!!.pagination.nextPageUrl
                        rv.adapter = RvHomeFeedAdapter(upEvents, activity!!)

                    }


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No more events",
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.black,
                        Toasty.LENGTH_SHORT,
                        false,
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

                    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!rv.canScrollVertically(1)) {
                                if (page != null)
                                    getMoreItems(adapter)

                            }
                        }
                    })


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No more events",
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.black,
                        Toasty.LENGTH_SHORT,
                        false,
                        true
                    ).show()
            }


        })


    }

    private fun today(rv: RecyclerView) {
        val service = UniclubApi.create()
        service.getTodayEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                null
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
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


                } else if (response.code() == 404)
                    Toasty.custom(
                        activity!!,
                        "No events today",
                        R.drawable.ic_error_outline_white_24dp,
                        R.color.black,
                        Toasty.LENGTH_SHORT,
                        false,
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
                null
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful && isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvHomeFeedAdapter(response.body()!!.pagination.events, activity!!)
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

                    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!rv.canScrollVertically(1)) {
                                if (page != null)
                                    getMoreItems(adapter)

                            }
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
            service.paginate(page!!).enqueue(object : Callback<EventListResponse> {
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>) {
                    if (response1.isSuccessful && isAdded) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            page = response1.body()!!.pagination.nextPageUrl

                        } else Toasty.custom(
                            activity!!,
                            "No more items",
                            R.drawable.ic_error_outline_white_24dp,
                            R.color.black,
                            Toasty.LENGTH_SHORT,
                            false,
                            true
                        ).show()
                    }

                }

            })

    }
}