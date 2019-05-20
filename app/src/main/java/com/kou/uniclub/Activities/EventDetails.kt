package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kou.uniclub.Adapter.RvHomeFeedAdapter.Companion.event_id
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Model.Event.EventDetailsResponse
import com.kou.uniclub.Model.User.ParticipateResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_event_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EventDetails : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var blurred = false
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val alpha = (p0.totalScrollRange + p1).toFloat() / p0.totalScrollRange
            if ((alpha==0f || alpha==1f)) {
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


        getDetails()





    }

        override fun onMapReady(googleMap: GoogleMap?) {
            mMap = googleMap!!

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Uniclub Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

    private fun getDetails()
    {
        val service = UniclubApi.create()
        service.getEvent(event_id!!).enqueue(object:Callback<EventDetailsResponse>{
            override fun onFailure(call: Call<EventDetailsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventDetailsResponse>, response: Response<EventDetailsResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()!!.event
                    evenTitle.text = event.name

                    if(event.photo!="") {
                        Picasso.get().load(event.photo).into(imEvent)
                        progress.visibility= View.GONE
                    }
                    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                    val startDate = format.parse(event.startTime)
                    val day = DateFormat.format("EEEE", startDate) as String
                    val dayNum=DateFormat.format("dd",startDate) as String
                    val month = DateFormat.format("MMM", startDate) as String
                    val timeline=DateFormat.format("HH:mm", startDate) as String
                    tvOrganizer.text= "Par ${event.animatedBy}"
                    tvTime.text = "$day , $dayNum $month  $timeline"


                    tvLocation.text=event.location
                    tvEventDesc.text = event.description

                    btnParticipate.setOnClickListener {
                        if(PrefsManager.geToken(this@EventDetails)==null)
                            BuilderAuth.showDialog(this@EventDetails)
                        else participate(event.id)
                    }

                }               }

        })
    }

    private fun participate(id:Int)
    {
        val service=UniclubApi.create()
        service.participate("Bearer "+PrefsManager.geToken(this@EventDetails)!!,id).enqueue(object:Callback<ParticipateResponse>{
            override fun onFailure(call: Call<ParticipateResponse>, t: Throwable) {
                if (t is IOException)
                    Toast.makeText(this@EventDetails,t.message.toString(),Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<ParticipateResponse>, response: Response<ParticipateResponse>) {
            if (response.isSuccessful) {
                Toast.makeText(this@EventDetails,response.body()!!.message,Toast.LENGTH_SHORT).show()
            }

            }

        })
    }

    }
