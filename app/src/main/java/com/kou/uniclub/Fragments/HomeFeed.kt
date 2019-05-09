package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Extensions.BuilderSearchFilter
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeFeed : Fragment() {
    private var cities = arrayOf("Region", "Ariana", "Tunis", "Bizerte")
    private var timings = arrayOf("All dates", "today", "upcoming", "passed")
    private var page: String? = null
    private var city: String? = null
    private var upEvents: ArrayList<EventX> = arrayListOf()


//TODO("onDetaach close web service calls")


    companion object {

        fun newInstance(): HomeFeed = HomeFeed()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(R.id.rvHome)
        val settings = v.findViewById<ImageView>(R.id.settings)
        val fab=v.findViewById<FloatingActionButton>(R.id.fabSearch)


        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /********Settings ******/
        settings.setOnClickListener {
            BuilderSettings.showSettings(activity!!)
        }
        /********Floating button ******/
        fab.setOnClickListener {BuilderSearchFilter.showDialog(activity!!) }

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
                if (response.isSuccessful) {
                    if (response.body()!!.pagination.events.size > 0) {
                        upEvents.addAll(response.body()!!.pagination.events)
                        page = response.body()!!.pagination.nextPageUrl
                        rv.adapter = RvHomeFeedAdapter(upEvents, activity!!)

                    }


                } else if (response.code() == 404)
                    Toasty.custom(activity!!, "No more events", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                        true).show()           }


        })


        //Pagination
        /*  rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
               override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                   super.onScrollStateChanged(recyclerView, newState)
                   if (!rv.canScrollVertically(1) ) {
                       if(page!=null)
                       service.paginate(page!!).enqueue(object : Callback<EventListResponse> {
                           override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                               Log.d("bo", "boo")
                           }

                           override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>)
                           {
                               if (response1.isSuccessful) {
                                   newSize = response1.body()!!.pagination.events.size


                                   if (page != "null" && (upEvents.size < oldSize!! + newSize!!)) {
                                       upEvents.addAll(response1.body()!!.pagination.events)
                                       rv.adapter!!.notifyItemRangeInserted(oldSize!!, response1.body()!!.pagination.events.size
                                       )
                                   }

                                   else Toast.makeText(activity!!, "next page null", Toast.LENGTH_SHORT).show()

                               }

                           }

                       })

                   }

               }
           })*/
    }

    private fun allDates(rv: RecyclerView) {
        // All Dates
        val service = UniclubApi.create()
        service.getEventFeed().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
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
                    Toasty.custom(activity!!, "No more events", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                        true).show()
            }


        })


    }

    private fun today(rv: RecyclerView) {
        // All Dates
        val service = UniclubApi.create()
        service.getTodayEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                null
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
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
                    Toasty.custom(activity!!, "No events today", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                        true).show()
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
                if (response.isSuccessful) {
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
                    Toasty.custom(activity!!, "No passed events", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                        true).show()
            }


        })
    }

    private fun regionFilter(rv: RecyclerView, city: String) {
        val service = UniclubApi.create()
        service.showByRegion(city).enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
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
                    Toasty.custom(activity!!, "No events in $city", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                        true).show()

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
                    if (response1.isSuccessful) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            page = response1.body()!!.pagination.nextPageUrl

                        } else Toasty.custom(activity!!, "No more items", R.drawable.ic_error_outline_white_24dp, R.color.black, Toasty.LENGTH_SHORT,false,
                            true).show()
                    }

                }

            })

    }
}