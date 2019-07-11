package com.kou.uniclub.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kou.uniclub.Activities.EventDetails
import com.kou.uniclub.Activities.EventDetails.Companion.eventId
import com.kou.uniclub.Activities.Notifications
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Adapter.SearchEventAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Model.Event.NoPagination.EventsResponse
import com.kou.uniclub.Model.Event.Pagination.EventListResponse
import com.kou.uniclub.Model.categorty
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class HomeFeed : Fragment() {
    private var page: String? = null
    private var searchFilter: AutoCompleteTextView? = null


    companion object {

        fun newInstance(): HomeFeed = HomeFeed()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvHome)
        val fab = v.findViewById<FloatingActionButton>(com.kou.uniclub.R.id.fabSearch)
        val imSettings = v.findViewById<ImageView>(com.kou.uniclub.R.id.imSettings)
        val spRegion = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spRegion)
        val spTiming = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spTiming)
        val token = PrefsManager.geToken(activity!!)
        val imNotifs = v.findViewById<ImageView>(R.id.imNotifs)







        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /********Settings ******/
        imSettings.setOnClickListener {
            if (token != null)
                BuilderSettings.showSettings(activity!!)
            else
                BuilderAuth.showDialog(activity!!)
        }
        /********** Notifications  ****************/
        if (token != null)
            imNotifs.setOnClickListener {
                startActivity(Intent(activity!!, Notifications::class.java))
            }
        /********Floating button ******/
        fab.setOnClickListener {
            showSearchFilter(activity!!)

        }

        /*** Filters ****/
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
                        com.kou.uniclub.R.color.toasty,
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
        service.getEventsP().enqueue(object : Callback<EventListResponse> {
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
                        com.kou.uniclub.R.color.toasty,
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
                        true,
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
                        com.kou.uniclub.R.color.toasty,
                        Toasty.LENGTH_SHORT,
                        true,
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
                                    com.kou.uniclub.R.color.toasty,
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
        spTiming.setItems("All dates", "Today", "Upcoming", "Passed")
        spRegion.setItems(
            "All regions",
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
                0 -> allDates(rv)
                1 -> today(rv)
                2 -> upcoming(rv)
                3 -> passed(rv)


            }

        }


        //region
        spRegion.setOnItemSelectedListener { view, position, id, item ->
            if (position != 0)
                regionFilter(rv, item.toString())

        }
    }


    private fun cards(card: CardView, context: Context, c: categorty) {


        card.setOnClickListener {
            c.isClicked = if (!c.isClicked) {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                true
            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.darkGray))
                false

            }
        }


    }



    private fun radio(rd: RadioButton, c: categorty, context: Context,arr: ArrayList<CardView>) {
        var ischecked=false
        rd.setOnClickListener {
            if (!ischecked) {
                c.isClicked = true
                rd.setButtonDrawable(R.drawable.tick_orange)
                for (i in 0 until arr.size)
                    arr[i].setCardBackgroundColor(ContextCompat.getColor(context,R.color.orange))



            } else {
                c.isClicked = false
                rd.setButtonDrawable(R.drawable.ellipse)
                for (i in 0 until arr.size)
                    arr[i].setCardBackgroundColor(ContextCompat.getColor(context,R.color.darkGray))

            }
            ischecked=!ischecked
        }

    }

    private fun showSearchFilter(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_search_filter, null)
        val back = dialogView.findViewById<View>(R.id.back)

        val busi = dialogView.findViewById<CardView>(R.id.busi)
        val learni = dialogView.findViewById<CardView>(R.id.learning)
        val culturi = dialogView.findViewById<CardView>(R.id.culture)
        val sociali = dialogView.findViewById<CardView>(R.id.social)
        val phototi = dialogView.findViewById<CardView>(R.id.photography)
        val techi = dialogView.findViewById<CardView>(R.id.tech)
        val sporti = dialogView.findViewById<CardView>(R.id.sports)
        val desi = dialogView.findViewById<CardView>(R.id.design)
        val gami = dialogView.findViewById<CardView>(R.id.gaming)
        val rd = dialogView.findViewById<RadioButton>(R.id.rdPrefs)

        val c1 = categorty()
        val c2 = categorty()
        val c3 = categorty()
        val c4 = categorty()
        val c5 = categorty()
        val c6 = categorty()
        val c7 = categorty()
        val c8 = categorty()
        val c9 = categorty()



        searchFilter = dialogView.findViewById(R.id.searchFilter)


        val builder = AlertDialog.Builder(context, R.style.FullScreenDialogStyle)

        builder.setView(dialogView)


        val dialog = builder.create()


        dialog.show()
        cards(busi, context, c1)
        cards(learni, context, c2)
        cards(culturi, context, c3)
        cards(sociali, context, c4)
        cards(phototi, context, c5)
        cards(techi, context, c6)
        cards(sporti, context, c7)
        cards(desi, context, c8)
        cards(gami, context, c9)

        radio(rd, c1, context, arrayListOf(busi,learni,culturi,sociali,phototi,techi,sporti,desi,gami))


        back.setOnClickListener {
            dialog.dismiss()
        }
        feedAutocomplete(searchFilter!!)


    }

    //TODO("When server performs updates events I want to update data")
    private fun feedAutocomplete(sv: AutoCompleteTextView?) {
        val service = UniclubApi.create()
        service.getEvents().enqueue(object : Callback<EventsResponse> {
            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                if (response.isSuccessful && isAdded) {
                    val adapter = SearchEventAdapter(activity!!, response.body()!!.events)

                    sv!!.setAdapter(adapter)
                    sv.setOnItemClickListener { parent, view, position, id ->
                        val item = parent.getItemAtPosition(position) as EventX
                        eventId = item.id
                        startActivity(Intent(activity!!, EventDetails::class.java))


                    }


                }
            }

        })
    }

    private fun showWiz(context: Context) {

        val wizView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_wiz_prefs, null)
        val wizBuilder = AlertDialog.Builder(context, R.style.TransparentAlertDialog)
        wizBuilder.setView(wizView)
        val wizDialog = wizBuilder.create()
        wizDialog.show()
        Handler().postDelayed({
            wizDialog.dismiss()

        }, 5000)

        PrefsManager.setWizPrefs(context, true)


    }
}