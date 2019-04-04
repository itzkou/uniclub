package com.kou.uniclub.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.EventDetails
import com.kou.uniclub.Model.Event
import kotlinx.android.synthetic.main.row_event_feed.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.kou.uniclub.Authentication.Auth


class HomeFeedAdapter (val events :List<Event>, val context: Context): RecyclerView.Adapter<HomeFeedAdapter.Holder>() {
    companion object {
        var event_id: Int? = null
        const val PERMIS_REQUEST=1997

    }
    private val appPermissions= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION)

   var activity=context as Activity


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_event_feed, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val event: Event = events[position]
            //date stuff
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(event.date)
        val day = DateFormat.format("dd", date) as String
        val month=DateFormat.format("MMM",date) as String


        holder.title.text=event.libele
        holder.place.text=event.lieu
        holder.month.text=month
        holder.day.text=day

        //if (PrefsManager.geToken(context)==null)TODO("else case = use private services")
        holder.fav.setOnClickListener {
               checkPermis()

        }


        //Event details
        holder.pic.setOnClickListener {

            event_id=event.id
            Log.d("id_ev", event_id.toString())
            context.startActivity(Intent(context,EventDetails::class.java))

        }




    }









    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.title
        val day = view.day
        val month = view.month
        val place = view.place
        val fav=view.favorite
        val pic=view.im_event

    }
    fun checkPermis():Boolean{
        val listPermis=ArrayList<String>()

        for (i in appPermissions){
            if (ContextCompat.checkSelfPermission(context,i)!= PackageManager.PERMISSION_GRANTED){
                listPermis.add(i)

            }
        }

        if (listPermis.isNotEmpty())
        {
            ActivityCompat.requestPermissions(activity,listPermis.toArray(arrayOfNulls(listPermis.size)), PERMIS_REQUEST)
            return false
        }

        return true
    }


}
