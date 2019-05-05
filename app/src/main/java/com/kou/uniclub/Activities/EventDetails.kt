package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kou.uniclub.Adapter.RvHomeFeedAdapter.Companion.event_id
import com.kou.uniclub.Model.Event.EventDetailsResponse
import com.kou.uniclub.Model.User.ParticipateResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EventDetails : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<EventDetailsResponse>, response: Response<EventDetailsResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()!!.event
                    collapse.title = event.name
                    collapse.setExpandedTitleColor(ContextCompat.getColor(this@EventDetails,
                        R.color.trans
                    ))
                    if(event.photo!="")
                    Picasso.get().load(event.photo).into(imEvent)

                    tvTime.text = event.startTime
                    tvEventDesc.text = event.description

                    btnParticipate.setOnClickListener {
                        participate(event.id)
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
