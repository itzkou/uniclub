package com.kou.uniclub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kou.uniclub.Adapter.HomeFeedAdapter.Companion.event_id
import com.kou.uniclub.Model.EventResponse
import com.kou.uniclub.Network.UniclubApi
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

        val service=UniclubApi.create()
        service.getEvent(event_id!!).enqueue(object : Callback<EventResponse> {
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                if(t  is IOException)
                    Toast.makeText(this@EventDetails,"Network faillure ALLunivs", Toast.LENGTH_SHORT).show()
                else   Toast.makeText(this@EventDetails,"Conversion error", Toast.LENGTH_SHORT).show()              }

            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()!!.data[0]
                    tv_title.title = event.libele
                    tv_program.text = event.date
                    tv_eventDesc.text = event.description
                }

            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Uniclub Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}
