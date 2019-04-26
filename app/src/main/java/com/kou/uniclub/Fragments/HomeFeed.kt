package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Extensions.PaginationListener
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFeed : Fragment() {
    private var cities = arrayOf("Tozeur", "Ariana", "Tunis", "Bizerte")
    private var timings = arrayOf("All dates", "Today", "Upcoming")
    private var page: String? = null
    private var city: String? = null
    private var upEvents: ArrayList<EventX> = arrayListOf()




    companion object {

        fun newInstance(): HomeFeed = HomeFeed()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(R.id.rvHome)
        val spTiming = v.findViewById<Spinner>(R.id.sp_timing)
        val spRegion = v.findViewById<Spinner>(R.id.sp_region)



            rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)



        spRegion.adapter= ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item,cities)
        spRegion.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | ic_settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city=cities[position]
            }

        }

        //filtre date
        spTiming.adapter=ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item,timings)
        spTiming.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                    null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                when(position)
                {
                    0-> AllDates(rvHome)


                    2-> Upcoming(rvHome)



                }
            }

        }
        return v
    }


            private fun Upcoming(rv:RecyclerView)
            {upEvents.clear()

                val service = UniclubApi.create()
                service.getUpcomingEvents().enqueue(object : Callback<EventListResponse> {
                    override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                        null            }

                    override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                        if (response.isSuccessful) {
                            upEvents.addAll(response.body()!!.pagination.events)
                            page = response.body()!!.pagination.nextPageUrl
                            rv.adapter=RvHomeFeedAdapter(upEvents,activity!!)


                        }
                    }


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
            private fun AllDates(rv:RecyclerView) {


                // All Dates
                val service = UniclubApi.create()
                service.getEventFeed().enqueue(object : Callback<EventListResponse> {
                    override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                        null
                    }

                    override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                        if (response.isSuccessful) {
                             page = response.body()!!.pagination.nextPageUrl
                            val adapter=RvHomeFeedAdapter(response.body()!!.pagination.events,activity!!)
                            rv.adapter=adapter

                            //Pagination

                            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                    super.onScrollStateChanged(recyclerView, newState)
                                    if (!rv.canScrollVertically(1)) {
                                        if (page!=null )
                                            getMoreItems(adapter)

                                    }
                                }
                            })


                        }
                    }


                })



            }

    fun getMoreItems(adapter:RvHomeFeedAdapter) {
        val service=UniclubApi.create()
            if (page!=null)
            service.paginate(page!!).enqueue(object :Callback<EventListResponse>{
                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<EventListResponse>, response1: Response<EventListResponse>) {
                               if (response1.isSuccessful){

                                   if (page!="null") {
                                       Log.d("paginatos","${response1.body()!!.pagination.nextPageUrl}")
                                        adapter.addData(response1.body()!!.pagination.events)
                                       page= response1.body()!!.pagination.nextPageUrl

                                   }
                                   else Toast.makeText(activity!!,"No more items",Toast.LENGTH_SHORT).show()
                               }
                                }

            })

    }
}