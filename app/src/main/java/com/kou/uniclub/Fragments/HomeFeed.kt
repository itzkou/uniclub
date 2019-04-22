package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.kou.uniclub.R

class HomeFeed: Fragment() {
    private var cities = arrayOf("Tozeur","Ariana", "Tunis", "Bizerte")
    private var timings = arrayOf("All dates","Today","Upcoming")

    private var city:String?=null

    companion object {

        fun newInstance():HomeFeed=HomeFeed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_homefeed,container,false)
        val sp_timing=v.findViewById<Spinner>(R.id.sp_timing)
        val sp_region=v.findViewById<Spinner>(R.id.sp_region)
        /*FeedAlldates()
        sp_region.adapter=ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item,cities)
        sp_region.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | ic_settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city=cities[position]
            }

        }

        //filtre date
        sp_timing.adapter=ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item,timings)
        sp_timing.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | ic_settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                when(position)
                {0->FeedAlldates()
                    1-> Today()
                        2-> Upcoming()

                }
            }

        }*/
        return v
    }

    /*fun FeedAlldates(){
        val service=UniclubApi.create()
        service.getEventFeed().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(activity!!,"Network faillure",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    rvHome.layoutManager=LinearLayoutManager(activity!!,LinearLayout.VERTICAL,false)
                    rvHome.adapter=RvHomeFeedAdapter(response.body()!!.data,activity!!)
                }
            }

        })
    }

    fun Today(){
        val service=UniclubApi.create()
        service.getEvenToday().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Toast.makeText(activity!!,"There are no events Today",Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    rvHome.layoutManager=LinearLayoutManager(activity!!,LinearLayout.VERTICAL,false)
                    rvHome.adapter=RvHomeFeedAdapter(response.body()!!.data,activity!!)

                }
            }

        })

    }

    fun Upcoming(){
        val service=UniclubApi.create()
        service.getUpcomingEvents().enqueue(object:Callback<FeedResponse>{
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Toast.makeText(activity!!,"There are no upcoming events",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                rvHome.layoutManager=LinearLayoutManager(activity!!,LinearLayout.VERTICAL,false)
                rvHome.adapter=RvHomeFeedAdapter(response.body()!!.data,activity!!)            }

        })
    }
*/
}