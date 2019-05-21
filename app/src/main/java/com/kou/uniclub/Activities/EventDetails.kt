package com.kou.uniclub.Activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kou.uniclub.Adapter.RvHomeFeedAdapter
import com.kou.uniclub.Adapter.RvHomeFeedAdapter.Companion.event_id
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Model.Event.EventDetailsResponse
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Model.User.ParticipateResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_event_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EventDetails : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var page: String? = null
//TODO("when user reparticipates I want an eror code different than 200")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var blurred = false
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val alpha = (p0.totalScrollRange + p1).toFloat() / p0.totalScrollRange
            if ((alpha == 0f || alpha == 1f)) {
                Blurry.delete(blurro as ViewGroup)
                blurred = false
            } else if ((alpha > 0 && alpha < 1) && !blurred) {
                blurred = true
                Blurry.with(this@EventDetails)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(125)
                    .onto(blurro as ViewGroup)

            }

        })

        rvMoreE.layoutManager = LinearLayoutManager(this@EventDetails, LinearLayout.HORIZONTAL, false)
        getDetails()


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Uniclub Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getDetails() {
        val service = UniclubApi.create()
        service.getEvent(event_id!!).enqueue(object : Callback<EventDetailsResponse> {
            override fun onFailure(call: Call<EventDetailsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventDetailsResponse>, response: Response<EventDetailsResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()!!.event

                    if (event.photo != "") {
                        Picasso.get().load(event.photo).into(imEvent)
                        progress.visibility = View.GONE
                    }
                    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                    val startDate = format.parse(event.startTime)
                    val day = DateFormat.format("EEEE", startDate) as String
                    val dayNum = DateFormat.format("dd", startDate) as String
                    val month = DateFormat.format("MMM", startDate) as String
                    val timeline = DateFormat.format("HH:mm", startDate) as String
                    evenTitle.text = event.name
                    tvOrganizer.text = "Par ${event.animatedBy}"
                    tvTime.text = "$day , $dayNum $month  $timeline"
                    tvLocation.text = event.location
                    tvEventDesc.text = event.description

                    moreEvents(event.clubId, rvMoreE,event.id)


                    readMo.setOnClickListener {
                        tvEventDesc.maxLines = 200
                    }
                    btnParticipate.setOnClickListener {
                        if (PrefsManager.geToken(this@EventDetails) == null)
                            BuilderAuth.showDialog(this@EventDetails)
                        else participate(event.id)
                    }

                }
            }

        })
    }

    private fun participate(id: Int) {
        val service = UniclubApi.create()
        service.participate("Bearer " + PrefsManager.geToken(this@EventDetails)!!, id)
            .enqueue(object : Callback<ParticipateResponse> {
                override fun onFailure(call: Call<ParticipateResponse>, t: Throwable) {
                    if (t is IOException)
                        Toast.makeText(this@EventDetails, t.message.toString(), Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<ParticipateResponse>, response: Response<ParticipateResponse>) {
                    if (response.isSuccessful) {
                      val snacko=  Snackbar.make(btnParticipate,"Your participation is confirmed",Snackbar.LENGTH_INDEFINITE)
                            .setAction("CANCEL") {
                                leave(id)
                                btnParticipate.animation=AnimationUtils.loadAnimation(this@EventDetails,R.anim.abc_slide_in_bottom)
                                btnParticipate.visibility=View.VISIBLE                        }

                        snacko.config(this@EventDetails)
                        snacko.show()

                        btnParticipate.animation=AnimationUtils.loadAnimation(this@EventDetails,R.anim.btn_participate)
                        btnParticipate.visibility=View.INVISIBLE


                    }




                }

            })
    }

    private fun leave(id: Int){
        val service=UniclubApi.create()
        service.leave("Bearer ${PrefsManager.geToken(this@EventDetails)}",id).enqueue(object:Callback<ParticipateResponse>{
            override fun onFailure(call: Call<ParticipateResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ParticipateResponse>, response: Response<ParticipateResponse>) {
                if (response.isSuccessful)
                {
                    Log.d("Leave",response.body()!!.message)
                }



            }

        })
    }


    private fun moreEvents(clubID: Int, rv: RecyclerView,evenID:Int) {
        val service = UniclubApi.create()
        service.getClubUpcomingE(clubID).enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {

                    page = response.body()!!.pagination.nextPageUrl
                    val events = response.body()!!.pagination.events
                    val filtered:List<EventX>

                    filtered=events.filter { it.id!=evenID }

                    val adapter = RvHomeFeedAdapter(filtered as java.util.ArrayList<EventX>, this@EventDetails)
                    rv.adapter = adapter

                    //Pagination

                    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!rv.canScrollHorizontally(1)) {
                                if (page != null)
                                    getMoreItems(adapter)

                            }
                        }
                    })

                }
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
                    if (response1.isSuccessful) {

                        if (page != null) {
                            adapter.addData(response1.body()!!.pagination.events)
                            page = response1.body()!!.pagination.nextPageUrl


                        } else
                            Toasty.custom(
                                this@EventDetails,
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
    fun Snackbar.config(context: Context){
        val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(12, 24, 12, 12)
        this.view.layoutParams = params
        this.view.background = ContextCompat.getDrawable(context,R.drawable.bg_snackbar)

        this.setActionTextColor(ContextCompat.getColor(context,R.color.orange))
        val text=this.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(context,R.color.white))
        text.maxLines=1
        text.textSize=12f

        ViewCompat.setElevation(this.view, 6f)
    }

}
