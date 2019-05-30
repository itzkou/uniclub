package com.kou.uniclub.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.User.UserX
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.Network.UniclubApi.Factory.imageURL
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class HomeFeed : Fragment() {
    private var page: String? = null
    private var picture: String? = null
    var myPrefs = ArrayList<String?>()


    companion object {

        fun newInstance(): HomeFeed = HomeFeed()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.kou.uniclub.R.layout.fragment_homefeed, container, false)
        val rvHome = v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvHome)
        val fab = v.findViewById<FloatingActionButton>(com.kou.uniclub.R.id.fabSearch)
        val imProfile = v.findViewById<ImageView>(com.kou.uniclub.R.id.imProfile)
        val spRegion = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spRegion)
        val spTiming = v.findViewById<MaterialSpinner>(com.kou.uniclub.R.id.spTiming)
        val token = PrefsManager.geToken(activity!!)





        if (token != null) {
            getUser(imProfile)


        }

        rvHome.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        /********Settings ******/
        imProfile.setOnClickListener {
            if (token != null)
                BuilderSettings.showSettings(activity!!)
            else
                BuilderAuth.showDialog(activity!!)
        }
        /********Floating button ******/
        fab.setOnClickListener {
            showDialog(activity!!)



        }
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

    private fun getUser(im: ImageView) {
        val service = UniclubApi.create()
        service.getUser("Bearer " + PrefsManager.geToken(activity!!)).enqueue(object : Callback<UserX> {
            override fun onFailure(call: Call<UserX>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {

                if (response.isSuccessful) {
                    picture = response.body()!!.image
                    if (picture.equals("/storage/Student/Profile_Picture/"))
                        Glide.with(activity!!).load(PrefsManager.getPicture(activity!!)).apply(RequestOptions.circleCropTransform())
                            .into(
                                im
                            )
                    else
                        Glide.with(activity!!).load(imageURL+ response.body()!!.image).apply(
                            RequestOptions.circleCropTransform()
                        )
                            .into(
                                im
                            )
                }
            }

        })
    }

    private fun cards(card: CardView, context: Context, arr: ArrayList<String?>) {


        val category = (card.getChildAt(0) as TextView).text.toString()
        var isClicked = false
        card.setOnClickListener {
            isClicked = if (!isClicked) {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                arr.add(category)



                true


            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.darkGray))
                arr.remove(category)

                false

            }


        }


    }


    fun showDialog(context: Context) {

        val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_search_filter, null)
        val busi = dialogView.findViewById<CardView>(R.id.busi)
        val learni = dialogView.findViewById<CardView>(R.id.learning)
        val culturi = dialogView.findViewById<CardView>(R.id.culture)
        val sociali = dialogView.findViewById<CardView>(R.id.social)
        val phototi = dialogView.findViewById<CardView>(R.id.photography)
        val techi = dialogView.findViewById<CardView>(R.id.tech)
        val sporti = dialogView.findViewById<CardView>(R.id.sports)
        val desi = dialogView.findViewById<CardView>(R.id.design)
        val gami = dialogView.findViewById<CardView>(R.id.gaming)


        val builder = AlertDialog.Builder(context, R.style.FullScreenDialogStyle)

        builder.setView(dialogView)
        myPrefs.clear()
        cards(busi, context, myPrefs)
        cards(learni, context, myPrefs)
        cards(culturi, context, myPrefs)
        cards(sociali, context, myPrefs)
        cards(phototi, context, myPrefs)
        cards(techi, context, myPrefs)
        cards(sporti, context, myPrefs)
        cards(desi, context, myPrefs)
        cards(gami, context, myPrefs)









            //TODO("Web service categories filter")
        builder.setPositiveButton("CONFIRM") { dialog, which ->
            for (i in 0 until myPrefs.size)
                Log.d("myPrefs", myPrefs[i])
            dialog?.dismiss()

            if (!PrefsManager.getWizPrefs(activity!!)!!)
            showWiz(context)



        }
        builder.setNegativeButton(
            "CANCEL"
        ) { dialog, which ->
            dialog?.dismiss()
        }
        val dialog = builder.create()



        dialog.show()


    }

    private fun showWiz(context:Context){

            val wizView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_wiz_prefs, null)
            val wizBuilder = AlertDialog.Builder(context, R.style.TransparentAlertDialog)
            wizBuilder.setView(wizView)
            val wizDialog = wizBuilder.create()
            wizDialog.show()
            Handler().postDelayed({
                wizDialog.dismiss()

            }, 5000)

            PrefsManager.setWizPrefs(context,true)


    }
}